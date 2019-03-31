import o2o.dto.WechatAuthExecution;
import o2o.entity.PersonInfo;
import o2o.entity.WechatAuth;
import o2o.service.WechatAuthService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class WechatAuthServiceTest extends BaseTest {


    @Autowired
    WechatAuthService wechatAuthService;

    @Test
    public void testRegister(){
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        String openId = "erqerqewrw";

        //给微信账号设置用户信息但不设置id，希望创建微信账号时自动创建用户信息
        personInfo.setCreateTime(new Date());
        personInfo.setName("测试费大幅度");
        personInfo.setUserType(1);
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId(openId);
        wechatAuth.setCreateTime(new Date());
        WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
        System.out.println(wae.getWechatAuthList());


        WechatAuth auth = wechatAuthService.getWechatAuthByOpenId(openId);
        System.out.println(auth.getPersonInfo().getName());
    }
}
