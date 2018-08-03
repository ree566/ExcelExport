/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
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
    public void testReadPivot() throws Exception{
        String fileLocal = "C:\\Users\\wei.cheng\\Desktop\\2018不良品&良品表單.xlsx";
        FileInputStream excelFile = new FileInputStream(new File(fileLocal));
        XSSFWorkbook workbook = new XSSFWorkbook(excelFile);
        assertNotNull(workbook);
        XSSFSheet datatypeSheet = workbook.getSheet("工作表6");
        assertNotNull(datatypeSheet);
        List<XSSFPivotTable> l = datatypeSheet.getPivotTables();
        assertEquals(1, l.size());
        
        FileOutputStream out = new FileOutputStream("");
//        out.write(l.get(0).get);
    }
    
}
