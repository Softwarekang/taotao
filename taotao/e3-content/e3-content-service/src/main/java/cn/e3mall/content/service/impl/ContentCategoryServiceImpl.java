package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;
import cn.e3mall.pojo.TbContentExample;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper TbContentCategoryMapper;
	/*
	 * parentId获取子节点列表
	 * (non-Javadoc)
	 * @see cn.e3mall.content.service.ContentCategoryService#getContentCatList(java.lang.Long)
	 */
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		// 设置查询条件
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 进行查询
		List<TbContentCategory> lists = TbContentCategoryMapper.selectByExample(tbContentCategoryExample);
		// 生成list<easyUiTreeNode>
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for(TbContentCategory list:lists){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(list.getId());
			node.setText(list.getName());
			node.setState(list.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		return nodeList;
	}
	
	
	@Override
	public E3Result addContentCatgory(Long parentId, String name) {
		// 创建TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// 设置对象的属性
		tbContentCategory.setParentId(parentId);
		tbContentCategory.setName(name);
		// 设置状态信息1正常2删除
		tbContentCategory.setStatus(1);
		tbContentCategory.setSortOrder(1);
		// 新节点为叶子节点
		tbContentCategory.setIsParent(false);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// 插入到数据库
		TbContentCategoryMapper.insertSelective(tbContentCategory);
		// 根据id查询父亲节点
		TbContentCategory parent = TbContentCategoryMapper.selectByPrimaryKey(parentId);
		// 判断父亲节点是否为父节点(比如当插入第一个子节点时，父亲节点也是子节点)
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			// 更新到数据库中
			TbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		return E3Result.ok(tbContentCategory);
	}

	/*
	 * 删除分类节点
	 * (non-Javadoc)
	 * @see cn.e3mall.content.service.ContentCategoryService#deleteContentCatgory(java.lang.Long)
	 */
	@Override
	public E3Result deleteContentCatgory(Long id) {
		TbContentCategory tbContentCategory = TbContentCategoryMapper.selectByPrimaryKey(id);
		// 根据节点id删除节点
		TbContentCategoryMapper.deleteByPrimaryKey(id);
		// 判断父节点下是否还有子节点
		TbContentCategory parent = TbContentCategoryMapper.selectByPrimaryKey(tbContentCategory.getParentId());
		TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
		Criteria criteria = tbContentCategoryExample.createCriteria();
		criteria.andParentIdEqualTo(parent.getId());
		System.out.println(TbContentCategoryMapper.selectByExample(tbContentCategoryExample) == null);
		if(TbContentCategoryMapper.selectByExample(tbContentCategoryExample) == null){
			parent.setIsParent(false);
			TbContentCategoryMapper.updateByPrimaryKeySelective(parent);
		}
		return E3Result.ok();
	}


	/*
	 * 重命名节点名称
	 * (non-Javadoc)
	 * @see cn.e3mall.content.service.ContentCategoryService#renameContentCatgory(java.lang.Long, java.lang.String)
	 */
	@Override
	public E3Result renameContentCatgory(Long id, String name) {
		TbContentCategory needChangedContentCategory = new TbContentCategory();
		needChangedContentCategory.setName(name);
		// 设置更新条件
		TbContentCategoryExample tbContentExample = new TbContentCategoryExample();
		Criteria createCriteria = tbContentExample.createCriteria();
		createCriteria.andIdEqualTo(id);
		// 数据库更新
		TbContentCategoryMapper.updateByExampleSelective(needChangedContentCategory, tbContentExample);
		return E3Result.ok();
	}

}
