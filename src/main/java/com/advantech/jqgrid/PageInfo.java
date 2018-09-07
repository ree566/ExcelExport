/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.jqgrid;

/**
 *
 * @author Wei.Cheng
 */
public class PageInfo implements Cloneable {

    private boolean _search;
    private String nd;
    private int page;
    private int rows;
    private String sidx;
    private String sord;

    private String searchField;
    private String searchString;
    private String searchOper;
    private String filters;

    private Integer maxNumOfRows;

    public PageInfo() {
        this.sidx = "id";
        this.sord = "asc";
        this.page = 1;
        this.rows = 10;
        this.maxNumOfRows = 0;
    }

    public boolean get_Search() {
        return _search;
    }

    /**
     * No need to set this
     * @param _search
     * @return 
     */
    public PageInfo set_Search(boolean _search) {
        this._search = _search;
        return this;
    }

    public String getNd() {
        return nd;
    }

    /**
     * No need to set this
     * @param nd
     * @return 
     */
    public PageInfo setNd(String nd) {
        this.nd = nd;
        return this;
    }

    public int getPage() {
        return page;
    }

    /**
     * Begin of the query result, default 1
     * @param page
     * @return 
     */
    public PageInfo setPage(int page) {
        this.page = page;
        return this;
    }

    public int getRows() {
        return rows;
    }

    /**
     * Set rows < 0 to query all result, default 10
     * @param rows
     * @return 
     */
    public PageInfo setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public String getSidx() {
        return sidx;
    }

    /**
     * The sort order field name
     * @param sidx
     * @return 
     */
    public PageInfo setSidx(String sidx) {
        this.sidx = sidx;
        return this;
    }

    public String getSord() {
        return sord;
    }

    /**
     * The field sort order (asc, desc)
     * @param sord
     * @return 
     */
    public PageInfo setSord(String sord) {
        this.sord = sord;
        return this;
    }

    public Integer getMaxNumOfRows() {
        return maxNumOfRows;
    }

    /**
     * The total query result with paginate
     * @param maxNumOfRows
     * @return 
     */
    public PageInfo setMaxNumOfRows(Integer maxNumOfRows) {
        this.maxNumOfRows = maxNumOfRows;
        return this;
    }

    public String getSearchField() {
        return searchField;
    }

    /**
     * The search field name
     * @param searchField 
     */
    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    /**
     * The search param
     * @param searchString 
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchOper() {
        return searchOper;
    }

    /**
     * The search operation(eq, ne, lt, rt ...etc.)
     * @param searchOper 
     */
    public void setSearchOper(String searchOper) {
        this.searchOper = searchOper;
    }

    public String getFilters() {
        return filters;
    }

    /**
     * No need to set this param
     * @param filters 
     */
    public void setFilters(String filters) {
        this.filters = filters;
    }

    @Override
    public PageInfo clone() throws CloneNotSupportedException {
        return (PageInfo) super.clone();
    }
}
