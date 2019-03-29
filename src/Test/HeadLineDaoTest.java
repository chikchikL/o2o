import o2o.dao.HeadLineDao;
import o2o.entity.HeadLine;
import o2o.enums.EnableStatusEnum;
import o2o.util.ImageUtil;
import o2o.util.PathUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HeadLineDaoTest extends BaseTest {

	@Autowired
	private HeadLineDao headLineDao;

	@Test
	public void testInsertHeadLine() throws Exception {
		HeadLine headLine = new HeadLine();
		headLine.setCreateTime(new Date());

		headLine.setLineLink("链接4");
		headLine.setLineName("头图4");
		headLine.setPriority(4);
		int effectNum = headLineDao.insertHeadLine(headLine);
		System.out.println("insertNum:" + effectNum);
	}

	@Test
	public void testModifyHeadLine() throws Exception {
		HeadLine currHeadLine = new HeadLine();
		currHeadLine.setLastEditTime(new Date());
		currHeadLine.setLineId(1L);
		// 删除原有图片
		HeadLine origHeadLine = headLineDao.selectHeadLineById(1L);
		ImageUtil.deleteFileOrPath(origHeadLine.getLineImg());
		currHeadLine.setLineLink("链接2");
		currHeadLine.setLineName("头图2");
		String filePath = "D:\\eclipse\\pic\\头图2.jpg";

		currHeadLine.setPriority(2);
		int effectNum = headLineDao.updateHeadLine(currHeadLine);
		System.out.println("insertNum:" + effectNum);
	}

	@Test
	public void testDeleteHeadLine() throws Exception {
		HeadLine headLine = new HeadLine();
		headLine.setLineId(1L);

		headLine.setLastEditTime(new Date());
		int effectNum = headLineDao.updateHeadLine(headLine);
		System.out.println("insertNum:" + effectNum);
	}

	@Test
	public void testSelectHeadLine() throws Exception {
		List<HeadLine> headLineList = headLineDao.selectHeadLineList(new HeadLine());
		assertEquals(1, headLineList.size());
	}

}