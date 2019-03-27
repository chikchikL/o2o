package o2o.enums;

public enum ProductStateEnum {

	SUCCESS(1,"创建成功"),INNER_ERROR(-1001,"操作失败"),EMPTY_LIST(-1002,"添加数量为空"),
	PRODUCT_EMPTY(-2001, "请输入商品信息"), EDIT_ERROR(-2002, "商品编辑失败"), EMPTY(-2003, "商品为空"),
	PRODUCT_ID_EMPTY(-2004, "商品ID为空"),;

	private int state;
	private String stateInfo;

	/**
	 * @Description:私有构造函数,禁止外部初始化改变定义的常量
	 * 
	 * @param state
	 * @param stateInfo
	 */
	private ProductStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	/**
	 * @Description: 仅设置get方法,禁用set
	 */
	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	/**
	 * 
	 * @Description: 定义换成pulic static 暴漏给外部,通过state获取ShopStateEnum
	 *               values()获取全部的enum常量
	 * 
	 * @param state
	 * @return: ShopStateEnum
	 */
	public static ProductStateEnum stateOf(int state) {
		for (ProductStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

}