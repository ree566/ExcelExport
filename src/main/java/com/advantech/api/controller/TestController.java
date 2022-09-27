/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.api.controller;

import com.advantech.helper.HibernateObjectPrinter;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Wei.Cheng
 */
@Api(tags = "Controller API")
@Tag(name = "Controller API", description = "This controller performs API operations")
@RestController
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/testGetUtf8", method = {RequestMethod.GET})
    public String testGetUtf8(@RequestParam String testString) {
        HibernateObjectPrinter.print(testString);
        return testString;
    }

}
