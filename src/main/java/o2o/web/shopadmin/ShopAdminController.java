package o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shopadmin",method = RequestMethod.GET)
public class ShopAdminController {

    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        /*
        *  <property name="prefix" value="/WEB-INF/html/"></property>
            <property name="suffix" value=".html"></property>
            与spring-web中配置的prefix和suffix组合成页面的路径
        * */
        return "shop/shopoperation";
    }
}
