package o2o.web.wechat;

import o2o.dto.UserAccessToken;
import o2o.dto.WechatAuthExecution;
import o2o.dto.WechatUser;
import o2o.entity.PersonInfo;
import o2o.entity.WechatAuth;
import o2o.enums.WechatAuthStateEnum;
import o2o.service.PersonInfoService;
import o2o.service.WechatAuthService;
import o2o.util.wechat.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("wechatlogin")
/**
 * 获取关注公众号之后的微信用户信息的接口，如果在微信浏览器里访问
 * https://open.weixin.qq.com/connect/oauth2/authorize?appid=您的appId&redirect_uri=http://o2o.yitiaojieinfo.com/o2o/wechatlogin/logincheck&role_type=1&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
 * 则这里将会获取到code,之后再可以通过code获取到access_token 进而获取到用户信息
 * 
 * @author xiangze
 *
 */
public class WechatLoginController {

    private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);

    private static final String FRONTEND="1";

    private static final String SHOPEND="2";


    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
    public String doGet(HttpServletRequest request, HttpServletResponse response) {
        log.debug("weixin login get...");
        // 获取微信公众号传输过来的code,通过code可获取access_token,进而获取用户信息
        String code = request.getParameter("code");
        // 这个state可以用来传我们自定义的信息，方便程序调用，这里也可以不用
        String roleType = request.getParameter("state");
        log.debug("weixin login code:" + code);
        WechatUser user = null;
        String openId = null;
        WechatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            try {
                // 通过code获取access_token
                token = WechatUtil.getUserAccessToken(code);
                log.debug("weixin login token:" + token.toString());
                // 通过token获取accessToken
                String accessToken = token.getAccessToken();
                // 通过token获取openId
                openId = token.getOpenId();
                // 通过access_token和openId获取用户昵称等信息
                user = WechatUtil.getUserInfo(accessToken, openId);
                log.debug("weixin login user:" + user.toString());
                request.getSession().setAttribute("openId", openId);

                auth = wechatAuthService.getWechatAuthByOpenId(openId);


            } catch (IOException e) {
                log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
                e.printStackTrace();
            }
        }
        // ======todo begin======
        // 前面咱们获取到openId后，可以通过它去数据库判断该微信帐号是否在我们网站里有对应的帐号了，
        // 没有的话这里可以自动创建上，直接实现微信与咱们网站的无缝对接。

        if(auth == null){

            PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
            auth = new WechatAuth();
            auth.setOpenId(openId);

            if(FRONTEND.equals(roleType)){
                //1顾客
                personInfo.setUserType(1);
            }else{
                //2店家
                personInfo.setUserType(2);
            }




            auth.setPersonInfo(personInfo);
            WechatAuthExecution wae = wechatAuthService.register(auth);
            if(wae.getState() != WechatAuthStateEnum.SUCCESS.getState()){
                //未注册成功不转到任何页面
                return null;
            }else{
                //注册成功
                PersonInfo personInfoById =
                        personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
                //设置到Session中
                request.getSession().setAttribute("user",personInfo);
            }
        }


        //若用户点击的是前端展示系统按钮则进入前端展示系统
        if(FRONTEND.equals(roleType)){
            return "frontend/index";
        }else{
            return "shopadmin/shoplist";
        }
        // ======todo end======

    }
}
