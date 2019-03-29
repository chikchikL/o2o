package o2o.web.frontend;

import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.entity.ProductCategory;
import o2o.entity.Shop;
import o2o.enums.EnableStatusEnum;
import o2o.enums.ShopStateEnum;
import o2o.service.ProductCategoryService;
import o2o.service.ProductService;
import o2o.service.ShopService;
import o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 获取默认店铺下的店铺详情及商品列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopdetailpageinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listShopDetailPageInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取shopId
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop;
        List<ProductCategory> productCategoryList;
        if (shopId != -1) {
            // 获取店铺信息
            shop = shopService.getByShopId(shopId);
            // 获取店铺下的商品列表
            productCategoryList = productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList", productCategoryList);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", ShopStateEnum.NULL_SHOPID.getStateInfo());
        }
        return modelMap;
    }


    /**
     * 根据查询条件获取该店铺下的所有商品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "listproductsbyshop", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> listProductsByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // 获取页码信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 获取店铺Id
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && shopId > -1) {
            // 获取店铺类别Id
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            // 获取模糊查询的店铺名
            String productName = HttpServletRequestUtil.getString(request, "productName");
            // 构建查询条件
            Product productCondition = compactProductCondition4Search(shopId, productCategoryId, productName);
            // 查询列表
            ProductExecution productExecution = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("success", true);
            modelMap.put("productList", productExecution.getProductList());
            modelMap.put("count", productExecution.getCount());
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }
        return modelMap;
    }


    private Product compactProductCondition4Search(long shopId, long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        // 查询某个商品类别下的商品
        if (productCategoryId > -1) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        // 根据名称模糊查询商品信息
        if (productName != null) {
            productCondition.setProductName(productName);
        }
        // 只允许显示状态为上架的商品
        productCondition.setEnableStatus(EnableStatusEnum.AVAILABLE.getState());
        return productCondition;
    }


}
