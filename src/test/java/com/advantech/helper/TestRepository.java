/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.ScrappedDetail;
import com.advantech.repo.ScrappedDetailRepository;
import java.util.List;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.reader.ReaderConfig;
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
public class TestRepository {

    @Autowired
    private ScrappedDetailRepository scrappedRepo;

    @Autowired
    private ExcelReader reader;

    @Test
    public void testScrappedDetailRepo() throws Exception {
        String fileLocation = System.getProperty("user.home") + "/Desktop/2018不良品&良品表單.xlsx";
        String xmlConfig = "\\excel-template\\ScrappedDetail_6F.xml";

        List<ScrappedDetail> l = reader.read(xmlConfig, fileLocation);
        ReaderConfig.getInstance().setSkipErrors(true);

        assertNotEquals(0, l.size());

        List<ScrappedDetail> dataInDb = scrappedRepo.findAll();

        l.removeAll(dataInDb);
        
        scrappedRepo.saveAll(l);
    }

//    @Test
    public void testDataExist() {
//        HibernateObjectPrinter.print(scrappedRepo.existByPo("PAI4004ZA"));
    }

}
