package o2o.service.impl;

import o2o.dao.HeadLineDao;
import o2o.dto.HeadLineExecution;
import o2o.entity.HeadLine;
import o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    HeadLineDao headLineDao;

    @Override
    public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {

        List<HeadLine> headLines = headLineDao.selectHeadLineList(headLineCondition);
        return headLines;
    }

    @Override
    public HeadLineExecution addHeadLine(HeadLine headLine, MultipartFile headLineImg) {
        return null;
    }

    @Override
    public HeadLineExecution modifyHeadLine(HeadLine headLine, MultipartFile headLineImg) {
        return null;
    }

    @Override
    public HeadLineExecution removeHeadLine(long headLineId) {
        return null;
    }

    @Override
    public HeadLineExecution removeHeadLineList(List<Long> headLineIdList) {
        return null;
    }
}
