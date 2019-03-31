import o2o.dao.PersonInfoDao;
import o2o.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PersonInfoDaoTest extends BaseTest{

    @Autowired
    private PersonInfoDao personInfoDao;

    @Test
    public void testInsert(){
        PersonInfo personInfo = new PersonInfo();
        personInfo.setName("我是谁");
        personInfo.setGender("男");
        personInfo.setUserType(0);
        personInfo.setCreateTime(new Date());
        personInfo.setLastEditTime(new Date());
        personInfo.setEnableStatus(1);
        personInfoDao.insertPersonInfo(personInfo);
    }


    @Test
    public void testQuery(){
        PersonInfo personInfo = personInfoDao.queryPersonInfoById(2L);
        System.out.println(personInfo.getName());
    }
}
