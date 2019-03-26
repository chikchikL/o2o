import o2o.dao.ProductCategoryDao;
import o2o.entity.Product;
import o2o.entity.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductCategoryDaoTest extends BaseTest{

    @Autowired
    private ProductCategoryDao dao;

    @Test
    public void testQueryByShopId() {
        long shopId =1;
        List<ProductCategory> list = dao.queryProductCategoryList(shopId);
        System.out.println(list);
    }

    @Test
    public void testBatchInsert(){

        ArrayList<ProductCategory> categories = new ArrayList<>();
        ProductCategory c1 = new ProductCategory();
        c1.setShopId(1L);
        c1.setCreateTime(new Date());
        c1.setPriority(2);
        c1.setProductCategoryName("烧烤");
        categories.add(c1);
        ProductCategory category = new ProductCategory();
        category.setShopId(1L);
        category.setCreateTime(new Date());
        category.setPriority(2);
        category.setProductCategoryName("王老吉");
        categories.add(category);

        int i = dao.batchInsertProductCategory(categories);
        Assert.assertEquals(2,i);
    }

    @Test
    public void deleteProductCategoryById(){
        int i = dao.deleteProductCategory(5, 1);
        Assert.assertEquals(1,i);
    }
}
