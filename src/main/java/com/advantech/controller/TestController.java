/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.job.SendWhReports;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.joda.time.DateTime;
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
@RequestMapping("/TestController")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/readNetworkDriveFile", method = {RequestMethod.GET})
    public String readNetworkDriveFile() throws FileNotFoundException, IOException {
        String fileLocation = "\\\\MFG-OAPC-019B\\MFG-Server\\之前的舊資料\\產線個人資料夾\\產線個人資料夾(N槽)\\尚芸\\6樓~2018年新表格\\2017不良品&良品表單.xlsx";
        FileInputStream is = new FileInputStream(new File(fileLocation));
        is.close();
        return "OK";
    }

    @ResponseBody
    @RequestMapping(value = "/testGetUtf8", method = {RequestMethod.GET})
    public String testGetUtf8(@RequestParam String testString) {
        HibernateObjectPrinter.print(testString);
        return testString;
    }

    @Autowired
    private SendWhReports job;

    @ResponseBody
    @RequestMapping(value = "/testSendWhReport", method = {RequestMethod.GET})
    public String testSendWhReport(@RequestParam int targetUserId, @RequestParam int year,
            @RequestParam int month, @RequestParam int day) throws Exception {
        DateTime dt = new DateTime(year, month, day, 0, 0, 0);
        job.testSendMail(targetUserId, dt);
        return "success";
    }

}
