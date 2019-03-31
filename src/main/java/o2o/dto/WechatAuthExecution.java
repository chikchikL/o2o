package o2o.dto;

import o2o.entity.Product;
import o2o.entity.WechatAuth;
import o2o.enums.ProductStateEnum;
import o2o.enums.WechatAuthStateEnum;

import java.util.ArrayList;
import java.util.List;

public class WechatAuthExecution {
    // 结果状态
    private int state;

    // 状态标识
    private String stateInfo;

    private int count;


    private List<WechatAuth> wechatAuthList = new ArrayList<>();


    public WechatAuthExecution() {
    }

    // 商品操作失败的时候使用的构造器
    public WechatAuthExecution(WechatAuthStateEnum stateEnum) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
    }

    // 商品操作成功的时候使用的构造器

    public WechatAuthExecution(WechatAuthStateEnum stateEnum, WechatAuth wechatAuth) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        wechatAuthList.add(wechatAuth);

    }

    public WechatAuthExecution(WechatAuthStateEnum stateEnum, List<WechatAuth> wechatAuthList) {
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.wechatAuthList = wechatAuthList;

    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<WechatAuth> getWechatAuthList() {
        return wechatAuthList;
    }

    public void setWechatAuthList(List<WechatAuth> wechatAuthList) {
        this.wechatAuthList = wechatAuthList;
    }
}
