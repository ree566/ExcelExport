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

    private int draw;
    private int start;//其实记录，第一条为0
    private int length;
    private List<ColumnInfo> columns; //列的Name列表
    private List<OrderInfo> order;//列对应是否能排序
    private List<SearchInfo> search;    //排序列的编号

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public List<OrderInfo> getOrder() {
        return order;
    }

    public void setOrder(List<OrderInfo> order) {
        this.order = order;
    }

    public List<SearchInfo> getSearch() {
        return search;
    }

    public void setSearch(List<SearchInfo> search) {
        this.search = search;
    }

    public static class OrderInfo {

        private int column;
        private String dir;

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
        }

    }

    public static class SearchInfo {

        private boolean regex;
        private String value;

        public boolean isRegex() {
            return regex;
        }

        public void setRegex(boolean regex) {
            this.regex = regex;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class ColumnInfo {

        private String data;
        private String name;
        private boolean orderable;
        private List<SearchInfo> search;
        private boolean searchable;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isOrderable() {
            return orderable;
        }

        public void setOrderable(boolean orderable) {
            this.orderable = orderable;
        }

        public List<SearchInfo> getSearch() {
            return search;
        }

        public void setSearch(List<SearchInfo> search) {
            this.search = search;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

    }

}
