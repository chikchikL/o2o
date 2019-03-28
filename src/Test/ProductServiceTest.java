import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.entity.ProductCategory;
import o2o.entity.Shop;
import o2o.enums.EnableStatusEnum;
import o2o.enums.ProductStateEnum;
import o2o.service.ProductService;
import o2o.util.ImageUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductServiceTest extends BaseTest {

	@Autowired
	private ProductService productService;

	@Test
	public void testAddProduct() throws IOException {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setShop(shop);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(2L);
		product.setProductCategory(productCategory);
		product.setProductName("测试商品1");
		product.setProductDesc("测试商品1描述");
		product.setPriority(11);
		product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
		product.setLastEditTime(new Date());
		product.setCreateTime(new Date());

//		缩略图
		File thumb = new File("C:\\Users\\刘洋\\Desktop\\test\\chezi.png");
		FileInputStream fis = new FileInputStream(thumb);

//		详情图
		File productImg1 = new File("C:\\Users\\刘洋\\Desktop\\test\\dabai.jpg");
		FileInputStream fis1 = new FileInputStream(productImg1);
		File productImg2 = new File("C:\\Users\\刘洋\\Desktop\\test\\rocket.jpg");
		FileInputStream fis2 = new FileInputStream(productImg2);

		ArrayList<InputStream> productImgList = new ArrayList<>();
		productImgList.add(fis1);
		productImgList.add(fis2);

		ArrayList<String> productImgNameList = new ArrayList<>();
		productImgNameList.add(productImg1.getName());
		productImgNameList.add(productImg2.getName());

		ProductExecution pe = productService.addProduct(product,
				fis,
				thumb.getName(),
				productImgList,
				productImgNameList);

		Assert.assertEquals(ProductStateEnum.SUCCESS.getState(),pe.getState());
	}

	@Test
	public void testModifyProduct() throws IOException {
		Product product = new Product();
		Shop shop = new Shop();
		shop.setShopId(1L);
		product.setShop(shop);
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(2L);
		product.setProductId(6L);
		product.setProductCategory(productCategory);
		product.setProductName("测试商品2");
		product.setProductDesc("测试商品2描述");
		product.setPriority(22);
		product.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
		product.setLastEditTime(new Date());
		product.setCreateTime(new Date());


		File thumb = new File("C:\\Users\\刘洋\\Desktop\\test\\wo.jpg");
		FileInputStream thumbfis = new FileInputStream(thumb);

		File img = new File("C:\\Users\\刘洋\\Desktop\\test\\wo.jpg");
		FileInputStream imgFis = new FileInputStream(img);

		ArrayList<InputStream> iss = new ArrayList<>();
		iss.add(imgFis);
		ArrayList<String> names = new ArrayList<>();
		names.add(img.getName());

		productService.modifyProduct(product,thumbfis,thumb.getName(),iss,names);

	}
}