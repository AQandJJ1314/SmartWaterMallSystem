package com.atcode.watermall.watermallsearch.service;

import com.atcode.watermall.watermallsearch.vo.SearchParam;

public interface MallSearchService {

    /**
     * @param searchParam   检索的所有参数
     * @return  返回检索的结果
     */
    Object search(SearchParam searchParam);
}
