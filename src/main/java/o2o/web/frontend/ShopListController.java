package o2o.web.frontend;

import o2o.dto.ShopExecution;
import o2o.entity.Area;
import o2o.entity.Shop;
import o2o.entity.ShopCategory;
import o2o.service.AreaService;
import o2o.service.ShopCategoryService;
import o2o.service.ShopService;
import o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {

    @Autowired
    private AreaService areaService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private ShopService shopService;


    /**
     * 获取店铺类别和区域信息列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId!=-1){
            //取出该一级shopCategory下的耳机ShopCategory列表
            try{
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parent = new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList =
                        shopCategoryService.getShopCategoryList(shopCategoryCondition);

            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else{
            try{
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }

        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try{
            //获取区域列表信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
            return modelMap;
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }

        return modelMap;

    }


    /**
     * 获取指定查询条件下的店铺列表
     */
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listShops(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if((pageIndex > -1) && (pageSize > -1)){
            //尝试获取一级类别id
            long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //尝试获取二级类别id
            long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //尝试获取区域id
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            //尝试获取模糊查询的名字
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            //获取组合之后的查询条件

            Shop shopCondition = compactShopCondition(parentId, shopCategoryId, areaId, shopName);

            ShopExecution se =
                    shopService.getShopList(shopCondition, pageIndex, pageSize);

            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty pageSize or pageIndex");
        }

        return modelMap;
    }

    /**
     * 组装查询店铺的筛选条件
     * @param parentId
     * @param shopCategoryId
     * @param areaId
     * @param shopName
     * @return
     */
    private Shop compactShopCondition(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if(parentId != -1L){
            ShopCategory parentShopCategory = new ShopCategory();
            ShopCategory shopCategory = new ShopCategory();
            parentShopCategory.setShopCategoryId(parentId);
            shopCategory.setParent(parentShopCategory);
            shopCondition.setShopCategory(shopCategory);
        }

        if(shopCategoryId != -1L){
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }

        if(areaId != -1L){
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if(shopName != null){
            //查询名字中包含shopName的店铺列表
            shopCondition.setShopName(shopName);
        }

        //通过审核的店铺才会展示
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }
}
