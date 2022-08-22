/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "T_WorkingHoursSummary")
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class WorkingHoursSummary implements Serializable {

    private WorkingHoursSummaryId id;
    private Date importDate;
    private BigDecimal activityToConf;

    @EmbeddedId
    @AttributeOverrides({
        @AttributeOverride(name = "PostingDate", column = @Column(name = "PostingDate", nullable = false, length = 50))
        , 
        @AttributeOverride(name = "Plant", column = @Column(name = "Plant", nullable = false, length = 50))
        ,
        @AttributeOverride(name = "CostCenter", column = @Column(name = "CostCenter", nullable = false, length = 50))
    })
    public WorkingHoursSummaryId getId() {
        return id;
    }

    public void setId(WorkingHoursSummaryId id) {
        this.id = id;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ImportDate", length = 23, insertable = false, updatable = false)
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    @Column(name = "Activity2Conf2")
    public BigDecimal getActivityToConf() {
        return activityToConf;
    }

    public void setActivityToConf(BigDecimal activityToConf) {
        this.activityToConf = activityToConf;
    }

}
