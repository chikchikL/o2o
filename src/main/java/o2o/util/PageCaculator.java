package o2o.util;

public class PageCaculator {

    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex>0)?(pageIndex-1)*pageSize:0;
    }
}
