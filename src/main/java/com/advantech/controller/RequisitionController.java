/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Requisition;
import com.advantech.model.RequisitionEvent;
import com.advantech.model.RequisitionEvent_;
import com.advantech.model.Requisition_;
import com.advantech.service.RequisitionEventService;
import com.advantech.service.RequisitionService;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    @Autowired
    private RequisitionEventService eventService;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.POST})
    protected DataTablesOutput<Requisition> findAll(
            @Valid DataTablesInput input,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        if (startDate != null && endDate != null) {
            final Date sD = startDate.toDate();
            final Date eD = endDate.toDate();

            return service.findAll(input, (Root<Requisition> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
                Path<Date> dateEntryPath = root.get(Requisition_.createDate);
                return cb.between(dateEntryPath, sD, eD);
            });
        } else {
            return service.findAll(input);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    protected String save(@ModelAttribute Requisition requisition) {

        service.save(requisition);
        return "success";

    }

    @ResponseBody
    @RequestMapping(value = "/updateState", method = {RequestMethod.POST})
    protected String updateState(@RequestParam int requisition_id, @RequestParam int state_id,
            @RequestParam(required = false) String remark) {

        service.changeState(requisition_id, state_id);
        return "success";

    }

    @ResponseBody
    @RequestMapping(value = "/findEvent", method = {RequestMethod.POST})
    protected DataTablesOutput<RequisitionEvent> findEvent(@Valid DataTablesInput input, @RequestParam int requisition_id) {
        Requisition re = service.findById(requisition_id).get();
        return eventService.findAll(input, (Root<RequisitionEvent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Path<Integer> idEntryPath = root.get(RequisitionEvent_.REQUISITION);
            return cb.equal(idEntryPath, re);
        });
    }
}
