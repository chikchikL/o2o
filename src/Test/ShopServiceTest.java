import o2o.dto.ShopExecution;
import o2o.entity.Area;
import o2o.entity.PersonInfo;
import o2o.entity.Shop;
import o2o.entity.ShopCategory;
import o2o.enums.ShopStateEnum;
import o2o.service.ShopService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest{
    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop(){
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
        shop.setShopName("测试的店铺1");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("C:\\Users\\刘洋\\Desktop\\harden.jpg");
        ShopExecution se = null;
        try {
            se = shopService.addShop(shop, new FileInputStream(shopImg),shopImg.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
    }

    @Test
    public void testModifyShop() throws FileNotFoundException {
        Shop shop = new Shop();
        shop.setShopId(12L);
        shop.setShopName("修改后的店铺名称");
        File file = new File("C:\\Users\\刘洋\\Desktop\\test\\dabai.jpg");
        FileInputStream fis = new FileInputStream(file);
        ShopExecution shopExecution = shopService.modifyShop(shop, fis, "dabai.jpg");
        System.out.println("你好"+shopExecution.getShop().getShopImg());

    }

}
