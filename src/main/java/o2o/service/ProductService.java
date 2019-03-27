package o2o.service;

import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.exceptions.ProductOperationException;

import java.io.InputStream;
import java.util.List;

public interface ProductService {
    /**
     *
     * @param product 商品
     * @param thumbnail 缩略图
     * @param thumbnailName 缩略图名
     * @param productImgList 详情图列表
     * @param productImgNameList 详情图名字列表
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product,
                                InputStream thumbnail,
                                String thumbnailName,
                                List<InputStream> productImgList,
                                List<String> productImgNameList) throws ProductOperationException;

}
