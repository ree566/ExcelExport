/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.Floor;
import com.advantech.model.ScrappedDetail;
import com.advantech.model.ScrappedDetailWeekGroup;
import com.advantech.repo.FloorRepository;
import com.advantech.repo.ScrappedDetailRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.apache.commons.collections.CollectionUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
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
    private FloorRepository floorRepo;

    @Autowired
    private ExcelDataTransformer tr;

    @Test
    @Transactional
    @Rollback(false)
    public void testScrappedDetailRepo() throws Exception {

        List<Floor> floors = floorRepo.findAll();

        Floor five = floors.stream().filter(f -> f.getName().equals("5F")).findFirst().get();
        assertNotNull(five);

        Floor six = floors.stream().filter(f -> f.getName().equals("6F")).findFirst().get();
        assertNotNull(six);

        List<ScrappedDetail> excelFloorFiveData = tr.getFloorFiveExcelData();
        List<ScrappedDetail> excelFloorSixData = tr.getFloorSixExcelData();

        assertNotEquals(0, excelFloorFiveData.size());
        assertNotEquals(0, excelFloorSixData.size());

        List<ScrappedDetail> floorFiveDataInDb = scrappedRepo.findByFloor(five);
        List<ScrappedDetail> floorSixDataInDb = scrappedRepo.findByFloor(six);

        excelFloorFiveData.forEach(s -> s.setFloor(five));
        excelFloorSixData.forEach(s -> s.setFloor(six));

        List<ScrappedDetail> newData1 = (List<ScrappedDetail>) CollectionUtils.subtract(excelFloorFiveData, floorFiveDataInDb);
        List<ScrappedDetail> newData2 = (List<ScrappedDetail>) CollectionUtils.subtract(excelFloorSixData, floorSixDataInDb);

        System.out.printf("Saving floor five data: %d, floor six data: %d \n", newData1.size(), newData2.size());

        scrappedRepo.saveAll(newData1);
        scrappedRepo.saveAll(newData2);

        //注意最後check兩邊size是否一致, 不一致可能是db有多的資料(使用者delete過), 必須做刪除
        floorFiveDataInDb = scrappedRepo.findByFloor(five);
        floorSixDataInDb = scrappedRepo.findByFloor(six);

        if (floorFiveDataInDb.size() + floorSixDataInDb.size() != excelFloorFiveData.size() + excelFloorSixData.size()) {
            System.out.println("Detect different data, begin remove...");

            List<ScrappedDetail> delData1 = (List<ScrappedDetail>) CollectionUtils.subtract(floorFiveDataInDb, excelFloorFiveData);
            List<ScrappedDetail> delData2 = (List<ScrappedDetail>) CollectionUtils.subtract(floorSixDataInDb, excelFloorSixData);

            System.out.printf("Remove floor five data: %d, floor six data: %d \n", delData1.size(), delData2.size());

            scrappedRepo.deleteAll(delData1);
            scrappedRepo.deleteAll(delData2);
        }

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testRepo() {
        ScrappedDetail d1 = scrappedRepo.findById(1484).get();

        HibernateObjectPrinter.print(d1);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlView() {
        List<ScrappedDetailWeekGroup> l = scrappedRepo.findAllGroupByWeek();

        HibernateObjectPrinter.print(l);
        
        
    }

}
