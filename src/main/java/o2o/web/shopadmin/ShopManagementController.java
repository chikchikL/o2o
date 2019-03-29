package o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import o2o.dto.ShopExecution;
import o2o.entity.Area;
import o2o.entity.PersonInfo;
import o2o.entity.Shop;
import o2o.entity.ShopCategory;
import o2o.enums.ShopStateEnum;
import o2o.exceptions.ShopOperationException;
import o2o.service.AreaService;
import o2o.service.ShopCategoryService;
import o2o.service.ShopService;
import o2o.util.CodeUtil;
import o2o.util.HttpServletRequestUtil;
import o2o.util.ImageUtil;
import o2o.util.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    Logger logger = LoggerFactory.getLogger(ShopManagementController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopbyid", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        HashMap<String, Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {

            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();

                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }

        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        HashMap<String, Object> modelMap = new HashMap<>();

        try {
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            List<Area> areaList = areaService.getAreaList();

            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }

        return modelMap;

    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) {
        //1.接收并转化相应的参数，包括店铺信息以及图片信息


        Map<String, Object> modelMap = new HashMap<>();
        //判断验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {

            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver resolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            //没有成功获取图片
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        //店铺json和图片都不为空,才执行插入店铺操作
        if (shop != null && shopImg != null) {
            PersonInfo owner = new PersonInfo();
            //TODO:Session完善
            owner.setUserId(1L);
            shop.setOwner(owner);

            ShopExecution shopExecution = null;
            try {
                shopExecution = shopService.addShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    //将该用户可以操作的店铺列表加入到会话中
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null || shopList.size() == 0) {
                        shopList = new ArrayList<>();
                        shopList.add(shopExecution.getShop());
                        request.getSession().setAttribute("shopList", shopList);

                    } else {
                        shopList.add(shopExecution.getShop());
                        request.getSession().setAttribute("shopList", shopList);
                    }
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "请输入店铺信息");
            return modelMap;
        }

        //3.返回结果


    }


    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        //1.接收并转化相应的参数，包括店铺信息以及图片信息


        Map<String, Object> modelMap = new HashMap<>();
        //判断验证码
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误");
            return modelMap;
        }

        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {

            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver resolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (resolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //2.修改店铺，图片允许为空
        //店铺json和图片都不为空,才执行插入店铺操作
        if (shop != null && shop.getShopId() != null) {

            //Session,一个浏览器独占一个Session对象，将cookie返回客户机，session在tomcat中默认30min有效
            //这里的user是用户登录时我们主动放入的attribute
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");

            shop.setOwner(owner);

            ShopExecution shopExecution;
            try {

                if (shopImg == null) {
                    shopExecution = shopService.modifyShop(shop, null, null);
                } else {
                    shopExecution = shopService.modifyShop(shop, shopImg.getInputStream(), shopImg.getOriginalFilename());
                }

                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errorMsg", "请输入店铺id");
            return modelMap;
        }

        //3.返回结果


    }

    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //如session中有这个Attribute代表有访问该shop的权限
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId<=0){
            Object currentShop = request.getSession().getAttribute("currentShop");
            if(currentShop == null){
                modelMap.put("redirect",true);
                modelMap.put("url","/o2o/shop/shoplist.css");
            }else{
                Shop current = (Shop)currentShop;
                modelMap.put("redirect",false);
                modelMap.put("shopId",current.getShopId());
            }
        }else{
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);

        }

        return modelMap;
    }

    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> getShopList(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        request.getSession().setAttribute("user",user);
        user = (PersonInfo) request.getSession().getAttribute("user");

        //todo:登录
        user.setUserId(1L);
        Long userId = user.getUserId();
        List<Shop> shopList = null;
        try{
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 0, 100);

            modelMap.put("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);

        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }

        return modelMap;
    }

//    private static void inputStreamToFile(InputStream ins, File file){
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while((bytesRead = ins.read(buffer))!=-1){
//                fos.write(buffer,0,bytesRead);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
