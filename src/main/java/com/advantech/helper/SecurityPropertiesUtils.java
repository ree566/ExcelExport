/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.db1.User;
import static com.google.common.base.Preconditions.checkState;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Wei.Cheng
 */
public class SecurityPropertiesUtils {

    public static User retrieveAndCheckUserInSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        checkState(!(auth instanceof AnonymousAuthenticationToken), "查無登入紀錄，請重新登入");
        return (User) auth.getPrincipal();
    }

}
