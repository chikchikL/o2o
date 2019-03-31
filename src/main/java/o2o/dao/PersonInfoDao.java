package o2o.dao;

import o2o.entity.PersonInfo;

public interface PersonInfoDao {

    /**
     * 查询用户
     * @param userId
     * @return
     */
    PersonInfo queryPersonInfoById(long userId);

    /**
     * 添加用户
     * @param personInfo
     * @return
     */
    int insertPersonInfo(PersonInfo personInfo);
}
