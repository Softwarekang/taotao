package cn.e3mall.search.service.Impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;

/*
 * 索引库维护service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public E3Result importAllItems() {

		try {
			// 查询列表
			List<SearchItem> itemList = itemMapper.getItemList();
			// 遍历商品列表
			for (SearchItem searchItem : itemList) {
				// 创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				// 像文档中添加域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				// 把对象也入索引库
				solrServer.add(document);
			}
			solrServer.commit();
			return E3Result.ok();
		} catch (SolrServerException | IOException e) {
			return E3Result.build(500, "服务器处理错误");
		}

	}

}
