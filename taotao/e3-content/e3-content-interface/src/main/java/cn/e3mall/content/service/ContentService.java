package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	E3Result addContent(TbContent tbContent);
	List<TbContent> getListContentByCid(Long cid);
}	
