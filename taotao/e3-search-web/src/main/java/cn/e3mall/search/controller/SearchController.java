package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/*
 * 商品搜索Controller
 */
@Controller
public class SearchController {
	@Value("${SEARCH_RESULT_COUNT}")
	private Integer SEARCH_RESULT_COUNT;
	@Autowired
	private SearchService searchService;

	@RequestMapping("/search")
	public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model)
			throws Exception {
		// 需要转码
		keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_COUNT);
		// 结果返回给当前页
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalCount());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());
		//返回逻辑视图
		return "search";
	}
}
