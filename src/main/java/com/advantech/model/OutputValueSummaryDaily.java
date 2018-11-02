/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Wei.Cheng
 */
public interface OutputValueSummaryDaily {

    public Date getImportDate();

    public String getDataDate();

    public String getOrderType();

    public String getProductValuePlant();

    public String getYearMonth();

    public Integer getQuantity();

    public BigDecimal getStandardCost();

    public BigDecimal getActualCost();
    
    public String getCurrency();
    
    public BigDecimal getStandardCostUSD();
    
    public BigDecimal getActualCostUSD();
}
