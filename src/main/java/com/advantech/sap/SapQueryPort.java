/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.sap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoTable;
import org.springframework.stereotype.Component;

/**
 *
 * @author MFG.ESOP
 */
@Component
public class SapQueryPort {

    public JCoTable getMaterialInfo(String po) throws JCoException {
        JCoFunction function;
        JCoDestination destination = SAPConn.connect();

        //调用ZCHENH001函数
        function = destination.getRepository().getFunction("ZGET_SAP_SODNWO_DATA_CK");

        JCoParameterList input = function.getImportParameterList();

        input.setValue("WONO", po);
        input.setValue("SDATE", "");
        input.setValue("EDATE", "");
        input.setValue("SPFLG", "");
        input.setValue("PLANT", "TWM3");

        function.execute(destination);

        JCoTable table = function.getTableParameterList().getTable("ZWODETAIL");//调用接口返回结果

//        for (int i = 0; i < table.getNumRows(); i++) {
//
//            table.setRow(i);
//
//            System.out.println(table.getString("AUFNR") + '\t' + table.getString("MATNR"));
//
//        }
        return table;

    }
}
