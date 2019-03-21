package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/*
 * 监听增加商品信息，进行索引库同步
 */
public class ItemAddMessageListener implements MessageListener {
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		try {
			// 从商品中获取id
			TextMessage textMessage = (TextMessage) message;
			String idText = textMessage.getText();
			Long id = new Long(idText);
			Thread.sleep(1000);
			// 根据id获得对象
			SearchItem searchItem = itemMapper.getItemById(id);
			// 创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			// 把文档写入索引库
			solrServer.add(document);
			// 提交
			solrServer.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
