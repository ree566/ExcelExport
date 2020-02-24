/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.FqcKanBan;
import com.advantech.model.User;
import com.advantech.service.FqcKanBanService;
import com.advantech.webservice.port.FqcKanBanQueryPort;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wei.Cheng
 */
@RestController
@RequestMapping("/FqcKanBanController")
public class FqcKanBanController extends CrudController<FqcKanBan> {

    @Autowired
    private FqcKanBanService service;

    @Autowired
    private FqcKanBanQueryPort kanBanQueryPort;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/findAll", method = {RequestMethod.POST})
    @Override
    protected DataTablesOutput<FqcKanBan> findAll(HttpServletRequest request, DataTablesInput input) {
        return service.findAll(input);
    }

    @ResponseBody
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    @Override
    protected ResponseEntity insert(@Valid FqcKanBan pojo, BindingResult bindingResult) throws Exception {
        service.save(pojo);
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @Override
    protected ResponseEntity update(@Valid FqcKanBan pojo, BindingResult bindingResult) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        pojo.setLastEditor(user);
        service.save(pojo);
        reSyncFromMes();
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @Override
    protected ResponseEntity delete(int id) throws Exception {
        FqcKanBan pojo = service.getOne(id);
        service.delete(pojo);
        return serverResponse(SUCCESS_MESSAGE);
    }

    private void reSyncFromMes() {
        try {
            List<FqcKanBan> mesData = kanBanQueryPort.query();
            List<FqcKanBan> dbData = service.findAll();

            List<FqcKanBan> delData = (List<FqcKanBan>) CollectionUtils.subtract(dbData, mesData);
            service.deleteInBatch(delData);
            System.out.println("Delete data cnt " + delData.size());

            List<FqcKanBan> newData = (List<FqcKanBan>) CollectionUtils.subtract(mesData, dbData);
            service.saveAll(newData);
            System.out.println("New data cnt " + newData.size());

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
