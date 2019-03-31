import o2o.dao.WechatAuthDao;
import o2o.entity.PersonInfo;
import o2o.entity.WechatAuth;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class WechatAuthDaoTest extends BaseTest{
    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Test
    public void testInsert(){
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(2L);

        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setOpenId("fdfdsagdagads");
        wechatAuth.setCreateTime(new Date());



        int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);

    }

    @Test
    public void testQuery(){
        WechatAuth wechatAuth = wechatAuthDao.queryWechatInfoByOpenId("fdfdsagdagads");
        System.out.println(wechatAuth.getPersonInfo().getName());

    }

}
