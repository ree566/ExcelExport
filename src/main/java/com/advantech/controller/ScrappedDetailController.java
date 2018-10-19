/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.ScrappedDetail;
import com.advantech.service.ExceptionService;
import com.advantech.service.ScrappedDetailService;
import static com.google.common.collect.Lists.newArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/ScrappedDetailController")
public class ScrappedDetailController {
    
    @Autowired
    private ScrappedDetailService scrapService;
    
    @Autowired
    private ExceptionService service;
    
    @ResponseBody
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    protected Map findAll() {
        Map m = new HashMap();
        m.put("data", newArrayList(scrapService.findAll()));
        return m;
    }
    
    @ResponseBody
    @RequestMapping(value = "/findById", method = {RequestMethod.GET})
    protected Map findById(@RequestParam Integer id) {
        Map m = new HashMap();
        m.put("data", newArrayList(scrapService.findById(id).get()));
        return m;
    }
    
    @ResponseBody
    @RequestMapping(value = "/testService", method = {RequestMethod.GET})
    protected String testService() {
        service.testTransaction();
        return "OK";
    }
}
