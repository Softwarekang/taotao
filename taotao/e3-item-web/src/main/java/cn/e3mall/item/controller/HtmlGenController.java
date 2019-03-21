package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

/*
 * 生成静态资源的Controller
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		// 加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		// 创建模板对象
		Map data = new HashMap<>();
		data.put("Hello", 123456);
		// 指定输入路径的文件路和文件名
		Writer out = new FileWriter(new File("D:/workspaces-itcast/freemarker/ww1.txt"));
		template.process(data, out);
		out.close();
		return "ok";
	}
}
