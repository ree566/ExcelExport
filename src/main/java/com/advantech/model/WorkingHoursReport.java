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
public interface WorkingHoursReport {

    public String getDateField();

    public int getQuantity();

    public BigDecimal getSapWorktime();

    public BigDecimal getSapOutputValue();

    public String getPlant();
    
    public BigDecimal getEstimated();
    
    public BigDecimal getPercentage();
}
