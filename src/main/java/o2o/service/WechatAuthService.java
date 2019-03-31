package o2o.service;

import o2o.dto.WechatAuthExecution;
import o2o.entity.WechatAuth;
import o2o.exceptions.WechatAuthException;

public interface WechatAuthService {

    /**
     * 通过openId查找平台对应的微信账号
     * @param openId
     * @return
     */
    WechatAuth getWechatAuthByOpenId(String openId);

    /**
     * 注册本平台的微信账号
     */
    WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthException;

}
