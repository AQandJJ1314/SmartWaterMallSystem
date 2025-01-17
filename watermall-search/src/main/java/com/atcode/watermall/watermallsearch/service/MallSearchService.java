package com.atcode.watermall.watermallsearch.service;

import com.atcode.watermall.watermallsearch.vo.SearchParam;
import com.atcode.watermall.watermallsearch.vo.SearchResult;

public interface MallSearchService {

    /**
     * @param searchParam   检索的所有参数
     * @return  返回检索的结果
     */
    SearchResult getSearchResult(SearchParam searchParam);
}
