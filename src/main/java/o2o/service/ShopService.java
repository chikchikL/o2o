package o2o.service;

import o2o.dto.ShopExecution;
import o2o.entity.Shop;
import o2o.exceptions.ShopOperationException;

import java.io.InputStream;

public interface ShopService {

    /**
     *
     * @param shop 店铺信息
     * @param shopImgInputStream 图片字节流
     * @param fileName 文件名
     * @return
     * @throws ShopOperationException
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName)
            throws ShopOperationException;


    ShopExecution modifyShop(Shop shop,InputStream shopImgInputStream,String fileName)
            throws ShopOperationException;

    /**
     * 根据id查询店铺详情
     *
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);
}
