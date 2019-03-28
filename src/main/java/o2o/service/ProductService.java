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

    /**
     * 查询商品列表并分页，可输入条件有：商品名(模糊)，商品状态，店铺Id，商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);


    Product getProductById(long productId);

    /**
     * 修改商品信息
     * @param product
     * @param thumbnail
     * @param thumbnailName
     * @param productImgList
     * @param productImgNameList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution modifyProduct(Product product,
                                   InputStream thumbnail,
                                   String thumbnailName,
                                   List<InputStream> productImgList,
                                   List<String> productImgNameList) throws ProductOperationException;
}
