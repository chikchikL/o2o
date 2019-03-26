package o2o.service.impl;

import o2o.dao.ProductCategoryDao;
import o2o.dto.ProductCategoryExecution;
import o2o.entity.ProductCategory;
import o2o.enums.ProductCategoryStateEnum;
import o2o.exceptions.ProductCategoryOperationException;
import o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryDao dao;

    @Override
    public List<ProductCategory> getProductCategoryList(long shopId) {


        return dao.queryProductCategoryList(shopId);
    }


    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> list)
            throws ProductCategoryOperationException {
//        list为空或者size为0直接返回空状态
//        如果size>0,批量插入，返回的effectNum<=0则插入失败，抛出商品类别操作异常
//        否则返回插入成功的dto对象
        if(list != null && list.size()>0){
            try{
                int effectedNum = dao.batchInsertProductCategory(list);
                if(effectedNum <= 0){
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }

            }catch (Exception e){
                throw new ProductCategoryOperationException("batchAddProductCategory");
            }
        }else{
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(@RequestParam long productCategoryId, long shopId)
            throws ProductCategoryOperationException{
        //TODO 将此商品类别下的商品类别Id置为空后再删除，应该是因为外键约束
        try{
            int effectedNum = dao.deleteProductCategory(productCategoryId,shopId);
            if(effectedNum <= 0){
                throw new ProductCategoryOperationException("商品类别删除失败");
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (ProductCategoryOperationException e){
            throw new ProductCategoryOperationException("delete product category error:"+e.getMessage());
        }

    }
}
