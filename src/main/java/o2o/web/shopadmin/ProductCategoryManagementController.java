package o2o.web.shopadmin;

import o2o.dto.ProductCategoryExecution;
import o2o.dto.Result;
import o2o.entity.ProductCategory;
import o2o.entity.Shop;
import o2o.enums.ProductCategoryStateEnum;
import o2o.exceptions.ProductCategoryOperationException;
import o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request){

        //todo:模拟从session中取出当前店铺
        Shop shop = new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        //成功
        if(currentShop!=null && currentShop.getShopId()>0){
            List<ProductCategory> productCategoryList =
                    productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<>(true,productCategoryList);
        }
        //没有当前店铺
        else{
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.NULL_SHOP;
            return new Result<>(false,ps.getStateInfo(),ps.getState());
        }
    }

    @RequestMapping(value = "/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategorys(@RequestBody List<ProductCategory> list,
                                                   HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        //根据当前的shopId获取该店铺对应的商品类别列表
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc:list) {
            pc.setShopId(currentShop.getShopId());
        }

        if(list != null && list.size() > 0){
            try{
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(list);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }

            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个商品类别");
        }

        return modelMap;
    }

}
