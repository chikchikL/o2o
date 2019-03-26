package o2o.enums;

/**
 *
 */

/**
 * @Description: 商品类别状态枚举
 *
 */

public enum ProductCategoryStateEnum {
    SUCCESS(1,"创建成功"),INNER_ERROR(-1001,"操作失败"),EMPTY_LIST(-1002,"添加数量为空"),
    NULL_SHOP(-2001, "Shop信息为空"), DELETE_ERROR(-2003, "商品类别删除失败"), EDIT_ERROR(-2004, "商品类别编辑失败");

    private int state;
    private String stateInfo;

    /**
     * @Description:构造函数
     *
     * @param state
     * @param stateInfo
     */
    private ProductCategoryStateEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    /**
     * @Description: 通过state获取productCategoryStateEnum,从而可以调用ProductCategoryStateEnum
     *               #getStateInfo()获取stateInfo
     *
     * @param index
     */
    public static ProductCategoryStateEnum stateOf(int index) {
        for (ProductCategoryStateEnum productCategoryStateEnum : values()) {
            if (productCategoryStateEnum.getState() == index) {
                return productCategoryStateEnum;
            }
        }
        return null;
    }

}
