/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Wei.Cheng
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    private static final String SALT_MESSAGE = "Hello world!";

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.md5Hex(SALT_MESSAGE + rawPassword); // TODO implement
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.encode(rawPassword).equals(encodedPassword); // TODO implement
    }

}
