/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.db1.ScrappedDetail;
import com.advantech.model.db1.ScrappedDetail_;
import com.advantech.service.db1.ScrappedDetailService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wei.Cheng
 */
@RestController
@RequestMapping("/ScrappedDetailController")
public class ScrappedDetailController {

    @Autowired
    private ScrappedDetailService scrapService;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.POST})
    protected DataTablesOutput<ScrappedDetail> findAll(
            @Valid DataTablesInput input,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate) {

        if (startDate != null && endDate != null) {
            final Date sD = startDate.toDate();
            final Date eD = endDate.toDate();

            return scrapService.findAll(input, (Root<ScrappedDetail> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
                Path<Date> dateEntryPath = root.get(ScrappedDetail_.createDate);
                return cb.between(dateEntryPath, sD, eD);
            });
        } else {
            return scrapService.findAll(input);
        }

    }

}
