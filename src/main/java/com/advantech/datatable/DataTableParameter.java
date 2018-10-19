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
 */
public class DataTableParameter {

    private int sEcho; //请求服务器端次数
    private int iDisplayStart;//其实记录，第一条为0
    private int iDisplayLength;
    private int iColumns;
    private List<String> mDataProps; //列的Name列表
    private List<Boolean> bSortables;//列对应是否能排序
    private int iSortingCols;
    private List<Integer> iSortCols;    //排序列的编号
    private List<String> iSortColsName; //排序列的名称
    private List<String> sSortDirs;     //排布列排序形式 Asc/Desc

    public int getsEcho() {
        return sEcho;
    }

    public void setsEcho(int sEcho) {
        this.sEcho = sEcho;
    }

    public int getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(int iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public int getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(int iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public int getiColumns() {
        return iColumns;
    }

    public void setiColumns(int iColumns) {
        this.iColumns = iColumns;
    }

    public List<String> getmDataProps() {
        return mDataProps;
    }

    public void setmDataProps(List<String> mDataProps) {
        this.mDataProps = mDataProps;
    }

    public List<Boolean> getbSortables() {
        return bSortables;
    }

    public void setbSortables(List<Boolean> bSortables) {
        this.bSortables = bSortables;
    }

    public int getiSortingCols() {
        return iSortingCols;
    }

    public void setiSortingCols(int iSortingCols) {
        this.iSortingCols = iSortingCols;
    }

    public List<Integer> getiSortCols() {
        return iSortCols;
    }

    public void setiSortCols(List<Integer> iSortCols) {
        this.iSortCols = iSortCols;
    }

    public List<String> getiSortColsName() {
        return iSortColsName;
    }

    public void setiSortColsName(List<String> iSortColsName) {
        this.iSortColsName = iSortColsName;
    }

    public List<String> getsSortDirs() {
        return sSortDirs;
    }

    public void setsSortDirs(List<String> sSortDirs) {
        this.sSortDirs = sSortDirs;
    }

}
