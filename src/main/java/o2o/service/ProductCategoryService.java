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
}
