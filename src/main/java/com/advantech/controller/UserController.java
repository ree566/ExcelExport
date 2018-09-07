/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.User;
import com.advantech.repo.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/UserController")
public class UserController {
    
    @Autowired
    private UserRepository repo;
    
    @ResponseBody
    @RequestMapping(value = "/find", method = {RequestMethod.GET})
    public List<User> find() {
        return repo.findAll();
    }
    
}
