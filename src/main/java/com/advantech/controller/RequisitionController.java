/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Requisition;
import com.advantech.service.RequisitionService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import javax.validation.Valid;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wei.Cheng
 */
@RestController
@RequestMapping("/RequisitionController")
public class RequisitionController {

    @Autowired
    private RequisitionService service;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.POST})
    protected DataTablesOutput<Requisition> findAll(
            @Valid DataTablesInput input,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        return service.findAll(input);

    }

    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    protected String save(@ModelAttribute Requisition requisition) {

        service.save(requisition);
        return "success";

    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    protected String save(@RequestBody List<Requisition> requisitions) {

        service.deleteAll(requisitions);
        return "success";

    }

}
