package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreeMarker {
	
	@Test
	public void testFreeMarker() throws Exception{
		// 创建模板文件
		
		// 创建一个Configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 设置模板文件的保存目录
		configuration.setDirectoryForTemplateLoading(new File("D:/workspaces-itcast/template/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		// 模板文件的编码格式
		configuration.setDefaultEncoding("utf-8");
		// 加载模板文件
		Template template = configuration.getTemplate("hello.ftl");
		// 创建一个数据集Pojo/Map
		Map data = new HashMap<>();
		data.put("Hello", "hello freemarker");
		// 创建Write对象
		Writer out = new FileWriter(new File("D:/workspaces-itcast/freemarker/hello.html"));
		// 生成静态页面
		template.process(data, out);
		// 关闭流
		out.close();
	}	
}
