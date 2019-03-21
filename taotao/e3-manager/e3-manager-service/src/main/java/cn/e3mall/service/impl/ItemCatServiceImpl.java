package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private TbItemCatMapper TbItemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(long parentId) {
		// 根据parebtId查询节点列表
		TbItemCatExample tbItemCatExample = new TbItemCatExample();
		// 设置查询条件
		Criteria createCriteria = tbItemCatExample.createCriteria();
		createCriteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = TbItemCatMapper.selectByExample(tbItemCatExample);
		// 转化为EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbItemCat tbItemCat:list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			// 添加到列表
			resultList.add(node);
		}
		// 返回list:EasyUITreeNode节点
		return resultList;
	}

}
