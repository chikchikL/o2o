package o2o.service.impl;

import o2o.dao.ProductDao;
import o2o.dao.ProductImgDao;
import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.entity.ProductImg;
import o2o.enums.ProductStateEnum;
import o2o.exceptions.ProductOperationException;
import o2o.service.ProductService;
import o2o.util.ImageUtil;
import o2o.util.PageCalculator;
import o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    @Override
    @Transactional
//   1.处理缩略图，将相对路径赋值给product
//    2.写入商品信息，获取productId
//    3.通过productId批量处理商品详情图
//    4.将商品详情图列表批量插入数据库
    public ProductExecution addProduct(Product product,
                                       InputStream thumbnail,
                                       String thumbnailName,
                                       List<InputStream> productImgList,
                                       List<String> productImgNameList)
            throws ProductOperationException {

        if (product != null && product.getShop() != null && product.getShop().getShopId() != null) {
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            product.setEnableStatus(1);
            if (thumbnail != null && thumbnailName != null) {
                addThumbnail(product, thumbnail, thumbnailName);

            }

            try {
                int effectedNum = productDao.insertProduct(product);
                if (effectedNum < 0) {
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败" + e.toString());
            }

            //若商品详情图不为空则添加
            if (productImgList != null && productImgList.size() > 0
                    && productImgNameList != null && productImgNameList.size() > 0) {
                addProductImgList(product, productImgList, productImgNameList);
            }
            return new ProductExecution(ProductStateEnum.SUCCESS, product);
        } else {
            return new ProductExecution(ProductStateEnum.EMPTY);
        }

    }

    @Override
    public ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize) {

        // 将页码转换为数据库的行数
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        // 获取商品列表分页信息
        List<Product> productList = productDao.selectProductList(productCondition, rowIndex, pageSize);
        // 获取商品总数
        int productCount = productDao.selectProductCount(productCondition);
        // 构建返回对象,并设值
        ProductExecution productExecution = new ProductExecution();
        productExecution.setCount(productCount);
        productExecution.setProductList(productList);
        return productExecution;
    }

    @Override
    public Product getProductById(long productId) {
        Product product = productDao.selectProductByProductId(productId);
        return product;
    }

    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product, InputStream thumbnail, String thumbnailName, List<InputStream> productImgList, List<String> productImgNameList) throws ProductOperationException {
        //1.若缩略图参数有值，则处理缩略图
        //若原先存在缩略图，则先删除再添加新图，之后获取缩略图相对路径并赋值给product
        //2.若商品详情图列表参数有值，对商品详情图片列表进行同样的操作
        //3.将tb_product_img下面的该商品原先商品详情图记录全部清除
        //4.更新tb_product信息
        if(product!=null && product.getShop()!=null && product.getShop().getShopId()!=null){
            product.setLastEditTime(new Date());
            //删除原缩略图并添加新缩略图
            if(thumbnail != null){
                //先获取一遍原有信息，因为原来的信息里有原图片地址
                Product p = productDao.selectProductByProductId(product.getProductId());
                String imgAddr = p.getImgAddr();
                if(imgAddr != null){
                    ImageUtil.deleteFileOrPath(imgAddr);
                }
                addThumbnail(product,thumbnail,thumbnailName);

            }

            //如果有新的详情图，删除旧的，添加新的
            if(productImgList !=null && productImgList.size()>0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product,productImgList,productImgNameList);
            }

            //更新填写的文字信息
            try{
                int effectedNum = productDao.updateProduct(product);
                if(effectedNum<=0){
                    throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(ProductStateEnum.SUCCESS,product);
            }catch (Exception e){
                throw new ProductOperationException("创建商品详情图片失败"+e.toString());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }



    }

    /**
     * 删除某个商品所有的详情图，注意！包含文件系统中的和数据库中两部分
     * @param productId
     */
    private void deleteProductImgList(Long productId) {
        //根据productId获取原来的图
        List<ProductImg> productImgs = productImgDao.selectProductImgListByProductId(productId);

        for (ProductImg pi:productImgs) {
            ImageUtil.deleteFileOrPath(pi.getImgAddr());
        }

        productImgDao.deleteProductImgByProductId(productId);

    }

    private void addProductImgList(Product product, List<InputStream> productImgInputStreamList, List<String> productImgNameList) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        ArrayList<ProductImg> productImgs = new ArrayList<>();
        int size = productImgInputStreamList.size();
        for (int i = 0; i < size; ++i) {
            String imgAddr = ImageUtil.generateNormalImg(productImgInputStreamList.get(i), productImgNameList.get(i), dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgs.add(productImg);
        }
        //如果确实有图片需要添加，就执行批量添加操作
        if(productImgs.size()>0){
            try{
                int effectedNum = productImgDao.batchInsertProductImg(productImgs);
                if(effectedNum <= 0){
                    throw new ProductOperationException("创建商品详情图片失败:");
                }
            }catch(Exception e){
                throw new ProductOperationException("创建商品详情图片失败:" + e.toString());
            }

        }

    }

    private void addThumbnail(Product product, InputStream thumbnail, String thumbnailName) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String addr = ImageUtil.generateThumbnail(thumbnail, thumbnailName, dest);
        product.setImgAddr(addr);
    }
}
