/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.datatable;

import java.util.List;

/**
 *
 * @author Wei.Cheng
 * @param <T>
 */
public class DataTable<T> {

    private List<T> data;//数据
    private int totalDisplayRecords;//得到的记录数
    private int totalRecords;//数据库中记录数
    private int sEcho; //请求服务器端次数
    //getter and setter

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalDisplayRecords() {
        return totalDisplayRecords;
    }

    public void setTotalDisplayRecords(int totalDisplayRecords) {
        this.totalDisplayRecords = totalDisplayRecords;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

}
