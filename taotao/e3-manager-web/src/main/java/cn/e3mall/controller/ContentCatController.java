package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;

/*
 * 商品内容分类管理
 */
@Controller
public class ContentCatController {
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(name="id",defaultValue="0")Long parentId){
		return contentCategoryService.getContentCatList(parentId);
	}
	
	/*
	 * 添加分类节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCatrgory(Long parentId, String name){
		return contentCategoryService.addContentCatgory(parentId, name);
	}
	
	/*
	 * 删除分类节点
	 */
	@RequestMapping(value="/content/category/delete/",method=RequestMethod.POST)
	@ResponseBody
	public E3Result deleteContentCatrgory(Long id){
		System.out.println(id);
		return contentCategoryService.deleteContentCatgory(id);
	}
	
	/*
	 * 重命名节点名称
	 */
	@RequestMapping(value="/content/category/update",method=RequestMethod.POST)
	@ResponseBody
	public E3Result renameContentCatrgory(Long id, String name){
		return contentCategoryService.renameContentCatgory(id, name);
	}
	
}
