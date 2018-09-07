/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.jqgrid;

import com.advantech.jqgrid.PageInfo;
import com.advantech.jqgrid.JqGridResponse;
import java.util.List;

/**
 *
 * @author Wei.Cheng
 */
public class JqGridResponseUtils {

    public static JqGridResponse toJqGridResponse(List l, PageInfo info) {
        JqGridResponse viewResp = new JqGridResponse();
        int count = info.getMaxNumOfRows();
        int total = count % info.getRows() == 0 ? (int) Math.ceil(count / info.getRows()) : (int) Math.ceil(count / info.getRows()) + 1;
        viewResp.setRows(l);
        viewResp.setTotal(total);
        viewResp.setRecords(count);
        viewResp.setPage(info.getPage());
        return viewResp;
    }
}
