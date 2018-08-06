/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
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

//    @Test
    public void testRead() throws IOException, SAXException, InvalidFormatException {
        String xmlConfig = "\\excel-template\\ScrappedDetail.xml";
        String dataXLS = "C:\\Users\\wei.cheng\\Desktop\\2018不良品&良品表單.xlsx";

        List l = reader.read(xmlConfig, dataXLS);

        assertNotEquals(0, l.size());

        ObjectMapper oMapper = new ObjectMapper();

        Map<String, Object> map = oMapper.convertValue(l.get(5), Map.class);
        System.out.println(map);
    }

    @Test
    public void testReadPivot() throws Exception {
        String fileLocal = "C:\\Users\\wei.cheng\\Desktop\\2018不良品&良品表單.xlsx";
        FileInputStream excelFile = new FileInputStream(new File(fileLocal));
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelFile)) {
            assertNotNull(workbook);
            XSSFSheet datatypeSheet = workbook.getSheet("工作表6");
            assertNotNull(datatypeSheet);

            List skipRows = newArrayList(0, 1, datatypeSheet.getPhysicalNumberOfRows() - 1);
            int i = 0;
            for (Row row : datatypeSheet) {
                if (!skipRows.contains(i++)) {
                    System.out.print(i + " -> ");
                    row.forEach(cell -> {
                        printCellValue(cell);
                    });
                    System.out.println();
                }
            }
            // Closing the workbook
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
