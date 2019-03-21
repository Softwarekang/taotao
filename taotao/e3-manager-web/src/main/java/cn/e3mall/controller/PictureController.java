package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.untils.FastDFSClient;
import cn.e3mall.common.untils.JsonUtils;
/*
 * 图片上传管理
 */
@Controller
public class PictureController {
	@Value("${IMAGE_URL}")
	private String IMAGE_URL;
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		try{
			// 图片上传到服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			// 取文件的扩展名
			String orginlName = uploadFile.getOriginalFilename();
			String extName = orginlName.substring(orginlName.lastIndexOf(".") + 1);
			// 得到一个图片的地址和文件名
			String filePath = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			// 补充url
			String url = IMAGE_URL + filePath;
			// 封装到map
			Map<Object,Object> result = new HashMap<>();
			result.put("error",0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		}catch(Exception e){
			Map<Object, Object> result = new HashMap<>();
			result.put("error",1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
