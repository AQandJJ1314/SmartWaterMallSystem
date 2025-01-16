package com.atcode.watermall.watermallsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

    //TODO thtleaf依赖冲突问题需要解决
    @GetMapping("/list.html")
    public String listPage() {
        System.out.println("==========测试是否进入页面跳转=============");

        return "list";
    }

    @GetMapping({"/","/index.html"})
    public String indexPage() {
        System.out.println("==========测试是否进入首页页面跳转=============");

        return "index";
    }
}
