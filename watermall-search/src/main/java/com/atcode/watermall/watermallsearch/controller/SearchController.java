package com.atcode.watermall.watermallsearch.controller;

import com.atcode.watermall.watermallsearch.service.MallSearchService;
import com.atcode.watermall.watermallsearch.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam) {
        System.out.println("==========测试是否进入页面跳转=============");

        Object result = mallSearchService.search(searchParam);

        return "list";
    }

    @GetMapping({"/index.html"})
    public String indexPage() {
        System.out.println("==========测试是否进入首页页面跳转=============");

        return "list";
    }
}
