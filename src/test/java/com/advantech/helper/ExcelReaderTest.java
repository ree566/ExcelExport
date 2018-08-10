/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.ScrappedDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jxls.reader.ReaderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.xml.sax.SAXException;

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
    private ExcelReader reader;

    private final String fileLocation = System.getProperty("user.home") + "/Desktop/2018不良品良品表單.xlsx";

    @Test
    public void testRead() throws IOException, SAXException, InvalidFormatException {
        String xmlConfig = "\\excel-template\\ScrappedDetail_5F.xml";

        List<ScrappedDetail> l = reader.read(xmlConfig, fileLocation);
        ReaderConfig.getInstance().setSkipErrors(true);

        assertNotEquals(0, l.size());

        
    }

//    @Test
    public void testRead2() throws Exception {
        FileInputStream excelFile = new FileInputStream(new File(fileLocation));
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            assertNotNull(workbook);
            XSSFSheet datatypeSheet = workbook.getSheet("報   廢");
            assertNotNull(datatypeSheet);

            for (Row row : datatypeSheet) {
                Cell checkCell = row.getCell(3);

                if ("工單".equals(checkCell.getStringCellValue())) {
                    continue;
                } else if (checkCell.getCellTypeEnum() == CellType.BLANK) {
                    break;
                }

//                convertCellToString(checkCell, row.getCell(4), row.getCell(5));
                row.forEach(cell -> {
                    printCellValue(cell);
                });
                System.out.println();

            }
            // Closing the workbook
        }
    }

    private void convertCellToString(Cell... cells) {
        for (Cell c : cells) {
            c.setCellType(CellType.STRING);
        }
    }

    private void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                break;
            case BLANK:
                System.out.print("_");
                break;
            default:
                System.out.print("_");
        }

        System.out.print("\t");
    }

}
