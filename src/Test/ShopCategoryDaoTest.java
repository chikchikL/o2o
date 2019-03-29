import o2o.dao.ShopCategoryDao;
import o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest{


    @Autowired
    ShopCategoryDao dao;

    @Test
    public void queryShopCategoryTest(){
        List<ShopCategory> sc = dao.queryShopCategory(new ShopCategory());
        ShopCategory testCategory = new ShopCategory();
        ShopCategory parentCategory = new ShopCategory();

        parentCategory.setShopCategoryId(1L);
        testCategory.setParent(parentCategory);
        List<ShopCategory> shopCategories = dao.queryShopCategory(testCategory);
        assertEquals(1,shopCategories.size());

    }

    @Test
    public void testQueryShopCategory(){
        List<ShopCategory> shopCategories = dao.queryShopCategory(null);
        System.out.println(shopCategories);
    }



}
