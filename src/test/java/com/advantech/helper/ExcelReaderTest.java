/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.util.List;
import org.joda.time.DateTime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelReaderTest {

    @Autowired
    private ExcelDataTransformer t;

    @Test
    public void testRead() throws Exception {
        DateTime sDOW = new DateTime("2018-06-01");
        DateTime eDOW = new DateTime("2018-06-30");
        List list1 = t.getFloorSixExcelData();
        assertTrue(!list1.isEmpty());
        System.out.println(list1.size());
        List list2 = t.getFloorSixExcelData(sDOW, eDOW);
        assertTrue(!list2.isEmpty());
        System.out.println(list2.size());
    }
}
