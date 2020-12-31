/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.sap.SAPConn;
import com.advantech.sap.SapQueryPort;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author MFG.ESOP
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context_test.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSap {

    @Autowired
    private SapQueryPort port;

    @Test
    public void testWarehouse() throws JCoException {

        JCoTable table = port.getMaterialInfo("PNH1102ZA");

        for (int i = 0; i < table.getNumRows(); i++) {
            table.setRow(i);
            System.out.println(table.getString("AUFNR") + '\t' + table.getString("MATNR"));
        }

    }

}
