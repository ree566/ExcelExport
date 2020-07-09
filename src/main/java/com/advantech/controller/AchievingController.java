/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.db1.Achieving;
import com.advantech.service.db1.AchievingService;
import com.fasterxml.jackson.annotation.JsonView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wei.Cheng
 */
@RestController
@RequestMapping("/AchievingController")
public class AchievingController {

    @Autowired
    private AchievingService service;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    protected DataTablesOutput<Achieving> findAll(
            HttpServletRequest request,
            @Valid DataTablesInput input) {

        return service.findAll(input);

    }

    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    protected String save(@ModelAttribute Achieving pojo) {
        service.save(pojo);
        return "success";
    }
    
    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    protected String delete(@ModelAttribute Achieving pojo) {
        service.delete(pojo);
        return "success";
    }

}
