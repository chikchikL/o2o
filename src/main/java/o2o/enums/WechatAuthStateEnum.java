package o2o.enums;

public enum WechatAuthStateEnum {
    LOGIN_FAIL(-1,"openId输入有误"),SUCCESS(0,"操作成功"),
    NULL_AUTH(1001,"空auth info");


    private int state;
    private String stateInfo;

    //构造器私有是默认的
    WechatAuthStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 根据state值获取相应的Enum
     */
    public static WechatAuthStateEnum stateOf(int state){
        for(WechatAuthStateEnum stateEnum:values()){
            if(stateEnum.getState() == state){
                return stateEnum;
            }
        }
        return null;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
