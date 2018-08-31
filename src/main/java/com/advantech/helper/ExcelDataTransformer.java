/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.ScrappedDetail;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
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

    private List<ScrappedDetail> filterResult(List<ScrappedDetail> l, DateTime sDOW, DateTime eDOW) {
        return l.stream().filter(p -> isDateBetweens(p.getCreateDate(), sDOW, eDOW)).collect(toList());
    }

    private boolean isDateBetweens(Date date, DateTime sDOW, DateTime eDOW) {
        DateTime d = new DateTime(date);
        return (sDOW.isBefore(d) || sDOW.isEqual(d)) && (eDOW.isAfter(d) || eDOW.isEqual(d));
    }

    public List<ScrappedDetail> getFloorSixExcelData() throws IOException, SAXException, InvalidFormatException {
        return this.getFloorSixExcelData(null, null);
    }

    public List<ScrappedDetail> getFloorSixExcelData(DateTime sDOW, DateTime eDOW) throws IOException, SAXException, InvalidFormatException {
        String xmlConfig = "\\excel-template\\ScrappedDetail_6F.xml";
        List<ScrappedDetail> l = reader.read(xmlConfig, floorSixFileLocation);
        if(sDOW != null && eDOW != null){
            l = filterResult(l, sDOW, eDOW);
        }
        return l;
    }

    public List<ScrappedDetail> getFloorFiveExcelData() throws FileNotFoundException, IOException {
        return this.getFloorFiveExcelData(null, null);
    }

    public List<ScrappedDetail> getFloorFiveExcelData(DateTime sDOW, DateTime eDOW) throws FileNotFoundException, IOException {
        List<ScrappedDetail> l = new ArrayList();

        FileInputStream excelFile = new FileInputStream(new File(floorFiveFileLocation));
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            XSSFSheet datatypeSheet = workbook.getSheet("報   廢");

            for (Row row : datatypeSheet) {
                Cell checkCell = row.getCell(3);
                Cell dateCell = row.getCell(2);

                if ("工單".equals(checkCell.getStringCellValue()) || (sDOW != null && new DateTime(dateCell.getDateCellValue()).isBefore(sDOW))) {
                    continue;
                } else if (checkCell.getCellTypeEnum() == CellType.BLANK || (sDOW != null && new DateTime(dateCell.getDateCellValue()).isAfter(eDOW))) {
                    break;
                }
                convertCellToString(checkCell, row.getCell(4), row.getCell(5));
                ScrappedDetail d = this.rowToDetail(row);
                l.add(d);
            }
            // Closing the workbook
        }

        return l;
    }

    private void convertCellToString(Cell... cells) {
        for (Cell c : cells) {
            c.setCellType(CellType.STRING);
        }
    }

    private ScrappedDetail rowToDetail(Row row) {
        ScrappedDetail d = new ScrappedDetail();
        d.setCreateDate(row.getCell(2).getDateCellValue());
        d.setPo(row.getCell(3).getStringCellValue());
        d.setModelName(row.getCell(4).getStringCellValue());
        d.setMaterialNumber(row.getCell(5).getStringCellValue());
        d.setAmount((int) row.getCell(6).getNumericCellValue());
        d.setReason(row.getCell(7).getStringCellValue());
        d.setKind(row.getCell(8).getStringCellValue());
        d.setPrice((int) row.getCell(9).getNumericCellValue());
        d.setNegligenceUser(row.getCell(11).getStringCellValue());
        d.setRemark(row.getCell(14).getStringCellValue());
        return d;
    }

}
