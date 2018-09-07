/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.advantech.model.UserNotification;
import com.advantech.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class UserNotificationConverter implements Converter<String, UserNotification> {

    @Autowired
    private UserNotificationService userNotificationService;

    @Override
    public UserNotification convert(String s) {
        return userNotificationService.findById(Integer.parseInt(s)).orElse(null);
    }

}
