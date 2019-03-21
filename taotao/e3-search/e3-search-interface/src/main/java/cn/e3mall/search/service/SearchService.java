package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {
	SearchResult search(String keyWords, int page, int rows) throws Exception;
}
