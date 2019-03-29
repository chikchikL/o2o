import o2o.dao.ShopDao;
import o2o.entity.Area;
import o2o.entity.PersonInfo;
import o2o.entity.Shop;
import o2o.entity.ShopCategory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {

    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShop(){

        Shop shop = shopDao.queryByShopId(1L);
        System.out.println(shop.getArea().getAreaName());

    }

    @Test
    @Ignore//使得junit忽略这个方法的测试
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试店铺");
        shop.setShopDesc("test");
        shop.setShopAddr("test");
        shop.setPhone("test");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(1);
        shop.setAdvice("审核中");
        int effectedNum = shopDao.insertShop(shop);
        assertEquals(1,effectedNum);
    }

    @Test
    public void testUpdateShop() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        shop.setShopDesc("测试描述");
        shop.setShopAddr("测试地址");
        int effectedNum = shopDao.updateShop(shop);
        assertEquals(1, effectedNum);
    }


    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        ShopCategory childCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();
        parentCategory.setShopCategoryId(3L);
        childCategory.setShopCategoryId(6L);
        childCategory.setParent(parentCategory);

        shopCondition.setShopCategory(childCategory);


//        PersonInfo owner = new PersonInfo();
//        owner.setUserId(1L);
//        shopCondition.setOwner(owner);
        List<Shop> shops = shopDao.queryShopList(shopCondition, 0, 5);
        System.out.println(shops);

        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(2L);
        shopCondition.setShopCategory(sc);
        int i = shopDao.queryShopCount(shopCondition);
        System.out.println(i);
    }

}
