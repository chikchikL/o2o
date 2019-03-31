package o2o.service.impl;

import o2o.dao.PersonInfoDao;
import o2o.dao.WechatAuthDao;
import o2o.dto.WechatAuthExecution;
import o2o.entity.PersonInfo;
import o2o.entity.WechatAuth;
import o2o.enums.WechatAuthStateEnum;
import o2o.exceptions.WechatAuthException;
import o2o.service.WechatAuthService;
import o2o.web.wechat.WechatLoginController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
    private static Logger log = LoggerFactory.getLogger(WechatAuthService.class);


    @Autowired
    private WechatAuthDao  wechatAuthDao;

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthException {

        if(wechatAuth == null || wechatAuth.getOpenId() == null){
            return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH);
        }
        try{
            wechatAuth.setCreateTime(new Date());
            //如果微信账号中有用户信息且id为空，认为该用户第一次使用平台并通过微信登录
            //自动创建用户信息
            if(wechatAuth.getPersonInfo()!=null && wechatAuth.getPersonInfo().getUserId() == null){
                try{
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    if(effectedNum <= 0){
                        throw new WechatAuthException("添加用户信息失败");

                    }
                }catch (Exception e){
                    log.error("insertPersonInfo error:" +e.toString());
                    throw new WechatAuthException("inserPersonINfo error" + e.toString());
                }
            }

            //创建专属于本平台的微信账号
            int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
            if(effectedNum<0){
                throw new WechatAuthException("账号创建失败");
            }else{
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
            }
        }catch (Exception e){
            log.error("insertWechatAuth error"+e.toString());
            throw new WechatAuthException("insertWechatAuth error");
        }

    }
}
