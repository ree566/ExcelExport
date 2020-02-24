/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import java.util.List;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public interface QueryResult<T> {

    List<T> getQryData();

    void setQryData(List<T> QryData);
    
}
