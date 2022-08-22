/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.db1.ScrappedDetail;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.annotation.PostConstruct;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.joda.time.DateTime;
import org.jxls.reader.ReaderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class ExcelDataTransformer {

    @Autowired
    private ExcelReader reader;

    @Value("${floor.five.fileLocation}")
    private String floorFiveFileLocation;

    @Value("${floor.six.fileLocation}")
    private String floorSixFileLocation;

    @PostConstruct
    private void init() {
        ReaderConfig.getInstance().setSkipErrors(true);
    }

    public List<ScrappedDetail> getFloorFiveExcelData() throws IOException, SAXException, InvalidFormatException {
        return this.getFloorFiveExcelData(null, null);
    }

    public List<ScrappedDetail> getFloorFiveExcelData(DateTime sDOW, DateTime eDOW) throws IOException, SAXException, InvalidFormatException {
        List<ScrappedDetail> l = readAndFitFromExcel(
                "\\excel-template\\ScrappedDetail_5F.xml", floorFiveFileLocation,
                sDOW, eDOW
        );
        l.removeIf(p -> "工單".equals(p.getPo()));
        return l;
    }

    public List<ScrappedDetail> getFloorSixExcelData() throws IOException, SAXException, InvalidFormatException {
        return this.getFloorSixExcelData(null, null);
    }

    public List<ScrappedDetail> getFloorSixExcelData(DateTime sDOW, DateTime eDOW) throws IOException, SAXException, InvalidFormatException {
        return readAndFitFromExcel(
                "\\excel-template\\ScrappedDetail_6F.xml", floorSixFileLocation,
                sDOW, eDOW
        );
    }

    private List<ScrappedDetail> readAndFitFromExcel(String conf, String resource, DateTime sDOW, DateTime eDOW) throws IOException, SAXException, InvalidFormatException {
        List<ScrappedDetail> l = reader.read(conf, resource);
        if (sDOW != null && eDOW != null) {
            l = filterResult(l, sDOW, eDOW);
        }
        return l;
    }

    private List<ScrappedDetail> filterResult(List<ScrappedDetail> l, DateTime sDOW, DateTime eDOW) {
        return l.stream().filter(p -> isDateBetweens(p.getCreateDate(), sDOW, eDOW)).collect(toList());
    }

    private boolean isDateBetweens(Date date, DateTime sDOW, DateTime eDOW) {
        DateTime d = new DateTime(date);
        return (sDOW.isBefore(d) || sDOW.isEqual(d)) && (eDOW.isAfter(d) || eDOW.isEqual(d));
    }

}
