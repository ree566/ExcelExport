/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.db1.ScrappedDetail;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.reader.ReaderBuilder;
import org.jxls.reader.XLSReader;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class ExcelReader {

    public List read(final String localTempLocation, final String targetDataLoaction)
            throws IOException, SAXException, InvalidFormatException {

        InputStream inputXML = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream(localTempLocation));
        XLSReader mainReader = ReaderBuilder.buildFromXML(inputXML);
        InputStream inputXLS = new BufferedInputStream(new FileInputStream(targetDataLoaction));
        List<ScrappedDetail> l = new ArrayList();
        Map beans = new HashMap();
        beans.put("rows", l);
        mainReader.read(inputXLS, beans);
        return l;

    }

}
