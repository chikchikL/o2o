import o2o.dao.ProductDao;
import o2o.entity.Product;
import o2o.entity.ProductCategory;
import o2o.entity.Shop;
import o2o.enums.EnableStatusEnum;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class ProductDaoTest extends BaseTest{
    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsertProduct() {
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(2L);
        Product product = new Product();
        product.setCreateTime(new Date());

        product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        product.setProductName("羊肉串");
        product.setProductDesc("11");
        product.setImgAddr("111");
        product.setNormalPrice("1111");
        product.setPromotionPrice("11111");
        product.setPriority(1);
        product.setProductCategory(productCategory);
        product.setShop(shop);
        int effectNum = productDao.insertProduct(product);
        System.out.println("effectNum:" + effectNum);
    }

    @Test
    @Ignore
    public void testQueryProductByProductId() throws Exception {
        Long productId = 1L;
        Product product = productDao.selectProductByProductId(productId);
        System.out.println("productImgSize：" + product.getProductImgList().size());
    }

    @Test
    @Ignore
    public void testUpdateProduct() throws Exception {
        Product product = new Product();
        product.setProductId(1L);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(1L);
        Shop shop = new Shop();
        shop.setShopId(1L);
        product.setShop(shop);
        product.setProductCategory(productCategory);
        product.setProductName("测试修改商品");
        int effectNum = productDao.updateProduct(product);
        System.out.println("effectNum:" + effectNum);
    }

    @Test
    @Ignore
    public void testQueryProductList() throws Exception {
        Product productCondition = new Product();
        // 分页查询
        List<Product> list = productDao.selectProductList(productCondition, 0, 5);
        System.out.println("list.size:" + list.size());
        // 查询总数
        int productCount = productDao.selectProductCount(productCondition);
        System.out.println("productCount:" + productCount);

        // 使用条件查询
        productCondition.setProductName("秋");
        // 条件分页查询
        List<Product> conditionList = productDao.selectProductList(productCondition, 0, 5);
        System.out.println("conditionList.size:" + conditionList.size());
        // 条件查询总数
        int conditionProductCount = productDao.selectProductCount(productCondition);
        System.out.println("conditionProductCount:" + conditionProductCount);
    }

    @Test
    public void testUpdateProductCategoryToNull() throws Exception {
        int effectNum = productDao.updateProductCategoryToNull(15L);
        System.out.println("effectNum:" + effectNum);
    }

}
