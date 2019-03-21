package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class IndexController {
	@Value("${CONTINUE_LUNBO_ID}")
	private Long CONTINUE_LUNBO_ID;
	
	@Autowired
	private ContentService ContentService;
	// 首页的跳转
	@RequestMapping("/index")
	public String index(Model model){
		// 查询内容列表
		List<TbContent> list = ContentService.getListContentByCid(CONTINUE_LUNBO_ID);
		// 传递结果给页面
		model.addAttribute("ad1List",list);
		return "index";
	}
}
