package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page, int rows);
	E3Result addItem(TbItem tbItem, String desc);
	E3Result deleteItem(List<Long> ids);
	E3Result soldOutItem(List<Long> ids);
	E3Result putAway(List<Long> ids);
	TbItemDesc geTbItemDescById(Long id);
}
