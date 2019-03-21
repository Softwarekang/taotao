package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * 监听商品添加消息，生成静态页面
 */
public class HtmlGenListener implements MessageListener{
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	@Override
	public void onMessage(Message message) {
		try {
			// 创建一个模板
			System.out.println("进入");
			// 从消息中获取id
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getText();
			Long id = new Long(text);
			System.out.print(id);
			Thread.sleep(1000);
			// 根据商品Id查询商品信息
			TbItem tbItem = itemService.getItemById(id);
			Item item = new Item(tbItem);
			// 取商品描述信息
			TbItemDesc tbItemDesc = itemService.geTbItemDescById(id);
			System.out.print(item.getId());
			// 创建数据集，商品信息封装起来
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", tbItemDesc);
			// 加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建数据流，指出
			Writer out = new FileWriter(new File(HTML_GEN_PATH+id+".html"));
			template.process(data, out);
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

}
