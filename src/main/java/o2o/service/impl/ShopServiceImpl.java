package o2o.service.impl;

import o2o.dao.ShopDao;
import o2o.dto.ShopExecution;
import o2o.entity.Shop;
import o2o.enums.ShopStateEnum;
import o2o.exceptions.ShopOperationException;
import o2o.service.ShopService;
import o2o.util.ImageUtil;
import o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    @Override
    @Transactional//添加事务支持
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationException{
        if(shop == null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }

        try{
            //给店铺信息赋初始值,不能改变的属性
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            //添加店铺信息

            int effectedNum = shopDao.insertShop(shop);
            if(effectedNum <= 0 ){
                throw new ShopOperationException("店铺创建失败");
            }else{
                //添加店铺成功，存储图片，存储成功后将图片地址url存储到shop
                if(shopImgInputStream!=null){

                    try{
                        addShopImg(shop,shopImgInputStream,fileName);
                    }catch (Exception e)
                    {
                        throw new ShopOperationException("addShopImg error:" + e.getMessage());
                    }

                    //更新店铺图片地址
                    effectedNum = shopDao.updateShop(shop);
                    if(effectedNum<=0){
                        throw new ShopOperationException("更新图片地址失败");
                    }

                }
            }
        }catch(Exception e){
            throw new ShopOperationException("addShop error:" + e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }


    private void addShopImg(Shop shop, InputStream shopImgInputStream,String fileName) {
        //获取shop图片目录的相对值路径，父路径额外生成
        String dest = PathUtil.getShopImagePath(shop.getShopId());

        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream,fileName,dest);

        shop.setShopImg(shopImgAddr);

    }
}
