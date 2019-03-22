package o2o.dao;

import o2o.entity.Shop;

public interface ShopDao {

    /**
     * 新增店铺
     *
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     */
    int updateShop(Shop shop);
}
