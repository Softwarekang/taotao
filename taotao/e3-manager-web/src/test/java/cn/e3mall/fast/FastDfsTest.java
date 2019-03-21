package cn.e3mall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import cn.e3mall.common.untils.FastDFSClient;

public class FastDfsTest {
	@Test
	public void testUpload() throws Exception{
		// 全局对象加载配置文件
		ClientGlobal.init("D:/workspaces-itcast/e3-manager-web/src/main/resources/conf/client.conf");
		// 创建TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		// 通过TrackerCient获得TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		// 创建StorageServer对象
		StorageServer server = null;
		// 创建StorageClient对象
		StorageClient storageClient = new StorageClient(trackerServer, server);
		// 使用StorageClent上传图片
		String[] strings = storageClient.upload_appender_file("C:/Users/安康/Pictures/Saved Pictures/A.jpg", "jpg", null);
		for(String string:strings){
			System.out.println(string);
		}
		
	}
	
	@Test
	public void test() throws Exception{
		FastDFSClient fastDFSClient = new FastDFSClient("D:/workspaces-itcast/e3-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("C:/Users/安康/Pictures/Saved Pictures/A.jpg");
		System.out.println(string);
	}

}
