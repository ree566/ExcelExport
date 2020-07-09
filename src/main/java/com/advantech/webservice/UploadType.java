/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

/**
 *
 * @author Wei.Cheng
 */
public enum UploadType {

    INSERT("A"),
    UPDATE("U"),
    DELETE("D");

    private final String state;

    private UploadType(final String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }

}
