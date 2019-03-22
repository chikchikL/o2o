package o2o.service.impl;

import o2o.dao.ShopCategoryDao;
import o2o.entity.ShopCategory;
import o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory condition) {
        List<ShopCategory> shopCategories = shopCategoryDao.queryShopCategory(condition);
        return shopCategories;
    }

}
