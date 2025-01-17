package com.atcode.watermall.watermallsearch.controller;

import com.atcode.watermall.watermallsearch.service.MallSearchService;
import com.atcode.watermall.watermallsearch.vo.SearchParam;
import com.atcode.watermall.watermallsearch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

//    @GetMapping({"/","list.html","/index.html"})
//    public String listPage(SearchParam searchParam) {
//        System.out.println("==========测试是否进入页面跳转=============");
//
//        Object result = mallSearchService.search(searchParam);
//
//        return "list";
//    }
    @GetMapping(value = {"/list.html","/","index.html"})
    public String getSearchPage(SearchParam searchParam, Model model, HttpServletRequest request) {
        searchParam.set_queryString(request.getQueryString());
        SearchResult result=mallSearchService.getSearchResult(searchParam);
        model.addAttribute("result", result);
        return "list";
    }

    @GetMapping("/search.html")
    public String indexPage() {
        System.out.println("==========测试是否进入首页页面跳转=============");

        return "search";
    }
}
