/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public abstract class CrudController<T> {

    protected final String ADD = "add", EDIT = "edit", DELETE = "del";
    protected final String SUCCESS_MESSAGE = "SUCCESS", FAIL_MESSAGE = "FAIL";

    protected final String SELECT_URL = "/read", INSERT_URL = "/create", UPDATE_URL = "/update", DELETE_URL = "/delete";

//    protected abstract JqGridResponse read(@ModelAttribute PageInfo info);
    
    protected abstract DataTablesOutput<T> findAll(HttpServletRequest request, @Valid DataTablesInput input);

    protected abstract ResponseEntity insert(@ModelAttribute T pojo, BindingResult bindingResult) throws Exception;

    protected abstract ResponseEntity update(@ModelAttribute T pojo, BindingResult bindingResult) throws Exception;

    protected abstract ResponseEntity delete(@RequestParam int id) throws Exception;

    protected ResponseEntity serverResponse(Object message) {
        return ResponseEntity
                .status(SUCCESS_MESSAGE.equals(message) ? HttpStatus.CREATED : HttpStatus.FORBIDDEN)
                .body(message);
    }
}
