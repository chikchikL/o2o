package o2o.dto;

import o2o.entity.ProductCategory;
import o2o.enums.ProductCategoryStateEnum;

import java.util.List;

public class ProductCategoryExecution {
    //状态
    private int state;

    //状态信息
    private String stateInfo;

    //商品种类列表
    private List<ProductCategory> productCategoryList;

    /**
     * 操作成功的构造器

     */
    public ProductCategoryExecution(ProductCategoryStateEnum ps){
        this.state = ps.getState();
        this.stateInfo = ps.getStateInfo();
    }

    public ProductCategoryExecution(ProductCategoryStateEnum ps,List<ProductCategory> list){
        this.state = ps.getState();
        this.stateInfo = ps.getStateInfo();
        this.productCategoryList = list;
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

    public List<ProductCategory> getProductCategoryList() {
        return productCategoryList;
    }

    public void setProductCategoryList(List<ProductCategory> productCategoryList) {
        this.productCategoryList = productCategoryList;
    }
}
