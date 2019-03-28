package o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.internal.util.xml.impl.Input;
import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.entity.ProductCategory;
import o2o.entity.Shop;
import o2o.enums.ProductStateEnum;
import o2o.exceptions.ProductOperationException;
import o2o.service.ProductCategoryService;
import o2o.service.ProductService;
import o2o.util.CodeUtil;
import o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    private static final  int MAX_IMAGE_COUNT = 6;

    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //验证码校验
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        //接收前端参数初始化，包括商品，缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");


        MultipartHttpServletRequest multipartRequest = null;
        ArrayList<InputStream> productImgs = new ArrayList<>();
        ArrayList<String> productImgNames = new ArrayList<>();

        CommonsMultipartResolver resolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        CommonsMultipartFile thumbnailFile = null;
        try{
            if(resolver.isMultipart(request)){
                 multipartRequest = (MultipartHttpServletRequest) request;

                 //取出缩略图
                thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");

                //取出详情图
                for(int i =0;i<MAX_IMAGE_COUNT;i++){
                    CommonsMultipartFile productImgFile =
                            (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
                    if(productImgFile!=null){
                        productImgs.add(productImgFile.getInputStream());
                        productImgNames.add(productImgFile.getOriginalFilename());
                    }else{
                        break;
                    }
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }

        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }

        try {
            product = mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        if(product != null &&  thumbnailFile!= null && productImgs.size()>0){
            try{
                //从session中获取当前店铺id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

                product.setShop(currentShop);
                //执行添加操作
                ProductExecution pe = productService.addProduct(product,
                        thumbnailFile.getInputStream(),
                        thumbnailFile.getOriginalFilename(),
                        productImgs,
                        productImgNames);

                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入商品信息");
        }

        return modelMap;



    }


    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductById(@RequestParam Long productId){
        HashMap<String, Object> modelMap = new HashMap<>();

        if(productId > -1){
            //获取商品信息
            Product product = productService.getProductById(productId);
            //获取该店铺下商品类别
            List<ProductCategory> productCategoryList =
                    productCategoryService.getProductCategoryList(product.getShop().getShopId());

            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }

        return modelMap;
    }

    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> modifyProduct(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //通过表单编辑还是点击上架下架
        //表单编辑需要通过验证码判断，上下架直接操作商品不需要
        boolean statusChange = HttpServletRequestUtil.getBoolean(request,"statusChange");
        if(!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","输入了错误的验证码");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        ArrayList<InputStream> productImgStreams = new ArrayList<>();
        ArrayList<String> productImgNames = new ArrayList<>();
        CommonsMultipartResolver resolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        CommonsMultipartFile thumbnailFile =null;
        try{
            if(resolver.isMultipart(request)){
                MultipartHttpServletRequest multipartRequest
                        = (MultipartHttpServletRequest) request;
                //获取缩略图
                thumbnailFile = (CommonsMultipartFile)multipartRequest.getFile("thumbnail");

                //取出详情图列表
                //取出详情图
                for(int i =0;i<MAX_IMAGE_COUNT;i++){
                    CommonsMultipartFile productImgFile =
                            (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
                    if(productImgFile!=null){
                        productImgStreams.add(productImgFile.getInputStream());
                        productImgNames.add(productImgFile.getOriginalFilename());
                    }else{
                        break;
                    }
                }

            }
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }
        try{
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.toString());
            return modelMap;
        }

        if(product!=null){
            try{
                //从session中获取当前店铺id并赋值给product，减少对前端数据的依赖
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe =null;
                if(thumbnailFile !=null && productImgStreams != null){
                     pe = productService.modifyProduct(product,
                            thumbnailFile.getInputStream(),
                            thumbnailFile.getOriginalFilename(),
                            productImgStreams, productImgNames);
                }else{
//                  //仅将商品下架的逻辑
                    pe = productService.modifyProduct(product,
                            null,
                            null,
                            null, null);
                }

                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }

        return modelMap;
    }


    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getProductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();

        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");


        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        if((pageIndex>-1) && (pageSize > -1)
                && (currentShop !=null) && (currentShop.getShopId() != null)){
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");

            //组装查询条件
            Product productCondition = compactProductCondition(currentShop.getShopId(), productCategoryId, productName);


            //查询
            ProductExecution pe = productService.getProductList(productCondition, pageIndex, pageSize);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);


        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex or shopId");
        }

        return modelMap;


    }

    private Product compactProductCondition(Long shopId, long productCategoryId, String productName) {

        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        if(productCategoryId!=-1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }

        if(productName != null){
            productCondition.setProductName(productName);
        }

        return productCondition;

    }

}
