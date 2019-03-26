package o2o.service;

import o2o.dto.ProductCategoryExecution;
import o2o.entity.ProductCategory;
import o2o.exceptions.ProductCategoryOperationException;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductCategoryService {

    List<ProductCategory> getProductCategoryList(long shopId);


    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> list)
            throws ProductCategoryOperationException;

    /**
     * 将此类别下的商品里的类别id置为空，再删除掉该商品类别，因为类别id是Product表的外键
     * @param productCategoryId
     * @param shopId
     * @return
     */
    ProductCategoryExecution deleteProductCategory(long productCategoryId,long shopId)
            throws ProductCategoryOperationException;
}
