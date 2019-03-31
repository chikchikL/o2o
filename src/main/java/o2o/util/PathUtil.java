package o2o.util;

public class PathUtil {

//    获取系统文件分隔符，mac下为/，win下为\
    private static String seperator = System.getProperty("file.separator");

    public static String getImgBasePath(){
        String os = System.getProperty("os.name");
        String basePath = "";
        if(os.toLowerCase().startsWith("win")){
            basePath = "C:/projectdev/image/";

        }else{
            basePath = "/Users/work/image";
        }

        basePath = basePath.replace("/",seperator);
        return basePath;
    }

    public static String getShopImagePath(long shopId){

        String imagePath = "/upload/item/shop/"+shopId+"/";
        imagePath.replace("/",seperator);
        return imagePath;
    }
}
