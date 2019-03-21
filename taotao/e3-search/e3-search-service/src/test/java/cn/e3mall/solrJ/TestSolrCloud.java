package cn.e3mall.solrJ;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/*
 * solr集群搭建测试
 */

public class TestSolrCloud {
	@Test
	public void testAddDocument() throws Exception, IOException{
		// 创建一个集群的连接,CloudServer
		CloudSolrServer solrServer = new CloudSolrServer("112.74.166.230:2182,112.74.166.230:2183,112.74.166.230:2184");
		// zk:Host:zookeeper的地址列表
		// 设置defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		// 创建空白文档
		SolrInputDocument document = new SolrInputDocument();
		// 向文件中添加域
		document.setField("id", "solrclound01");
		document.setField("item_title", "测试商品");
		document.setField("item_price", 123);
		// 把文件写入索引库
		solrServer.add(document);
		// 提交
		solrServer.commit();
		
	}
	
	@Test
	public void testQueryDocument() throws Exception{
		// 创建一个CloundServer对象
		CloudSolrServer cloudSolrServer = new CloudSolrServer("112.74.166.230:2182,112.74.166.230:2183,112.74.166.230:2184");
		// 设置默认的collection
		cloudSolrServer.setDefaultCollection("collection2");
		// 创建一个查询
		SolrQuery query = new SolrQuery();
		// 创建查询条件
		query.setQuery("*:*");
		// 执行查询
		QueryResponse queryResponse = cloudSolrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数"+solrDocumentList.getNumFound());
		// 遍历
		for(SolrDocument solrDocument:solrDocumentList){
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("title"));
			System.out.println(solrDocument.get("item_title"));
		}
		
	}
	
	
}	
