package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCatList(Long parentId);
	E3Result addContentCatgory(Long parentId, String name);
	E3Result deleteContentCatgory(Long id);
	E3Result renameContentCatgory(Long id,String name);
}

