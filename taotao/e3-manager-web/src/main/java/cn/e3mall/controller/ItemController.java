package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId) {
		TbItem tbItem = itemService.getItemById(itemId);
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page, Integer rows){
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result addItem(TbItem tbItem, String desc){
		return itemService.addItem(tbItem, desc);
	}
	
	/*
	 * 删除商品
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteTbitem(@RequestParam(value="ids") List<Long> ids){
		return itemService.deleteItem(ids);
	}
	
	/*
	 * 商品下架
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result soldOutItem(@RequestParam(value="ids")List<Long> ids){
		return itemService.soldOutItem(ids);
	}
	
	/*
	 * 上架商品
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result putAwayItem(@RequestParam(value="ids")List<Long> ids){
		return itemService.putAway(ids);
	}
}  
