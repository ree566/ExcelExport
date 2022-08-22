/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

/**
 *
 * @author Wei.Cheng
 */
public enum State {
    Open(1), Close(0);
    
    private final Integer value;
    
    private State(Integer value) {
        this.value = value;
    }
}
