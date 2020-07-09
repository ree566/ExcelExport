/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 *
 * @author Wei.Cheng
 */
public interface Encodeable {

    Object token();

    public static <E extends Enum<E> & Encodeable> E forToken(Class<E> cls, Object tok) {
        return Stream.of(cls.getEnumConstants())
                .filter(e -> Objects.equals(e.token(), tok))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown token '"
                + tok + "' for enum " + cls.getName()));
    }
}
