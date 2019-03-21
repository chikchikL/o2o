package o2o.dao;

import o2o.entity.Area;

import java.util.List;

public interface AreaDao {
    /**
     * 列出区域列表
     * @return area list
     */
    List<Area> queryArea();

}
