/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.job;

import com.advantech.helper.ExcelDataTransformer;
import com.advantech.model.Floor;
import com.advantech.model.ScrappedDetail;
import com.advantech.repo.FloorRepository;
import com.advantech.repo.ScrappedDetailRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng Sync back excel's data from "MFG-Server (MFG-OAPC-019B)"
 * Every day ※Only sync current years data
 */
@Component
public class SyncData {

    private static final Logger logger = LoggerFactory.getLogger(SyncData.class);

    @Autowired
    private ExcelDataTransformer excelTran;

    @Autowired
    private ScrappedDetailRepository scrappedRepo;

    @Autowired
    private FloorRepository floorRepo;

    @Transactional
    public void execute() {
        try {
            List<Floor> floors = floorRepo.findAll();

            Floor five = floors.stream().filter(f -> f.getName().equals("5F")).findFirst().get();

            Floor six = floors.stream().filter(f -> f.getName().equals("6F")).findFirst().get();

            List<ScrappedDetail> excelFloorFiveData = excelTran.getFloorFiveExcelData();
            List<ScrappedDetail> excelFloorSixData = excelTran.getFloorSixExcelData();

            List<ScrappedDetail> floorFiveDataInDb = scrappedRepo.findByFloor(five);
            List<ScrappedDetail> floorSixDataInDb = scrappedRepo.findByFloor(six);

            excelFloorFiveData.forEach(s -> s.setFloor(five));
            excelFloorSixData.forEach(s -> s.setFloor(six));

            //modelname & materialNumber are not format as string in excel
            excelFloorFiveData = fixMaterialNumAndModelname(excelFloorFiveData);
            excelFloorSixData = fixMaterialNumAndModelname(excelFloorSixData);

            //Find new data
            List<ScrappedDetail> newData1 = (List<ScrappedDetail>) CollectionUtils.subtract(excelFloorFiveData, floorFiveDataInDb);
            List<ScrappedDetail> newData2 = (List<ScrappedDetail>) CollectionUtils.subtract(excelFloorSixData, floorSixDataInDb);

            Date today = new Date();
            newData1 = getFromSameYear(newData1, today);
            newData2 = getFromSameYear(newData2, today);

            logger.info("Saving floor five data: {}, floor six data: {} \n", newData1.size(), newData2.size());

            scrappedRepo.saveAll(newData1);
            scrappedRepo.saveAll(newData2);

            //注意最後check兩邊size是否一致, 不一致可能是db有多的資料(使用者delete過), 必須做刪除
            //Don't remove data a year ago
            logger.info("Checking the data size again...");
            floorFiveDataInDb = scrappedRepo.findByFloor(five);
            floorSixDataInDb = scrappedRepo.findByFloor(six);

            if (floorFiveDataInDb.size() + floorSixDataInDb.size() != excelFloorFiveData.size() + excelFloorSixData.size()) {
                logger.info("Detect different data, begin remove...");

                List<ScrappedDetail> delData1 = (List<ScrappedDetail>) CollectionUtils.subtract(floorFiveDataInDb, excelFloorFiveData);
                List<ScrappedDetail> delData2 = (List<ScrappedDetail>) CollectionUtils.subtract(floorSixDataInDb, excelFloorSixData);

                delData1 = getFromSameYear(delData1, today);
                delData2 = getFromSameYear(delData2, today);
                logger.info("Remove floor five data: {}, floor six data: {} \n", delData1.size(), delData2.size());

                scrappedRepo.deleteAll(delData1);
                scrappedRepo.deleteAll(delData2);
            } else {
                logger.info("Nothing need to remove.");
            }

        } catch (IOException | SAXException | InvalidFormatException ex) {
            logger.error("Sync back excel's data fail.", ex);
        }
    }

    private List<ScrappedDetail> fixMaterialNumAndModelname(List<ScrappedDetail> l) {
        l = this.fixLongString(l);
        l = this.trimWhiteSpace(l);
        return l;
    }

    private List<ScrappedDetail> trimWhiteSpace(List<ScrappedDetail> l) {
        for (ScrappedDetail s : l) {
            s.setModelName(s.getModelName().trim());
            s.setMaterialNumber(s.getMaterialNumber().trim());
        }
        return l;
    }

    private List<ScrappedDetail> fixLongString(List<ScrappedDetail> l) {
        for (ScrappedDetail s : l) {
            if (s.getModelName().contains(".")) {
                s.setModelName(longToString(s.getModelName()));
            }
            if (s.getMaterialNumber().contains(".")) {
                s.setMaterialNumber(longToString(s.getMaterialNumber()));
            }
        }
        return l;
    }

    private String longToString(String val) {
        try {
            return Long.toString(new BigDecimal(val).longValue());
        } catch (Exception e) {
            return val;
        }
    }

    private List<ScrappedDetail> getFromSameYear(List<ScrappedDetail> l, Date currentDate) {
        return l.stream().filter(s -> getYearBetween(s.getCreateDate(), currentDate) < 1).collect(toList());
    }

    private int getYearBetween(Date d1, Date d2) {
        return Years.yearsBetween(new DateTime(d1), new DateTime(d2)).getYears();
    }

}
