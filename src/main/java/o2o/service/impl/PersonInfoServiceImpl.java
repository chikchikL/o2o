package o2o.service.impl;

import o2o.dao.PersonInfoDao;
import o2o.entity.PersonInfo;
import o2o.service.PersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public PersonInfo getPersonInfoById(Long userId) {


        return personInfoDao.queryPersonInfoById(userId);
    }
}
