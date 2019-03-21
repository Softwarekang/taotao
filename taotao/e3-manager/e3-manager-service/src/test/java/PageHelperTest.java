/*import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	@Test
	public void testPageHelper() throws Exception{
		// 初始化spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		// 获得mapper代理对象
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		// 执行语句前设置分页信息使用PageHelper的startPage方法
		PageHelper.startPage(1, 10);
		// 执行查询
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		// 取分页信息 1.总记录数 2.总页数...
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getPageSize());
		System.out.println(list.size());
		
		
		
	}
}
*/