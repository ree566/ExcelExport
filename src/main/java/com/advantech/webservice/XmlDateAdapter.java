/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Wei.Cheng
 */
public class XmlDateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:SSS+08:00");

    @Override
    public Date unmarshal(String v) throws Exception {
        if (v == null) {
            return null;
        }
        return sdf.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        if (v == null) {
            return null;
        }
        return sdf.format(v);
    }

}
