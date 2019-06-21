/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.math.BigDecimal;

/**
 *
 * @author Wei.Cheng
 */
public interface OvertimeRecord {

    public String getEmplrId();
    
    public String getLocalName();
    
    public Integer getWeekOfMonth();
    
    public String getSitefloor();
    
    public String getSum();
}
