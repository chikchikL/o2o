package o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import o2o.dto.ProductExecution;
import o2o.entity.Product;
import o2o.entity.Shop;
import o2o.enums.ProductStateEnum;
import o2o.exceptions.ProductOperationException;
import o2o.service.ProductService;
import o2o.util.CodeUtil;
import o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductManagementController {


    @Autowired
    private ProductService service;

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
                ProductExecution pe = service.addProduct(product,
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


}
