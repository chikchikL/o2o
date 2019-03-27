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
