/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.ScrappedDetail;
import java.math.BigDecimal;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
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
        List<ScrappedDetail> list1 = t.getFloorFiveExcelData();
        assertTrue(!list1.isEmpty());

        List<String> model = list1.stream().map(ScrappedDetail::getMaterialNumber).collect(toList());

        model.forEach(s -> {
            if (s.contains(".")) {
                System.out.println(Long.toString(new BigDecimal(s).longValue()));
            } else {
                System.out.println(s);
            }
        });

    }
}
