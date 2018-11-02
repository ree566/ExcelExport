/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.User;
import com.advantech.model.UserNotification;
import com.advantech.repo.db1.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository repo;

    public List<User> findAll() {
        return repo.findAll();
    }

    public Page<User> findAll(Pageable pgbl) {
        return repo.findAll(pgbl);
    }

    public Optional<User> findById(Integer id) {
        return repo.findById(id);
    }

    public List<User> findByUserNotifications(UserNotification notifi) {
        return repo.findByUserNotifications(notifi);
    }

    public User findByJobnumber(String jobnumber) {
        return repo.findByJobnumber(jobnumber);
    }

    public <S extends User> S save(S s) {
        return repo.save(s);
    }

    public <S extends User> List<S> saveAll(Iterable<S> itrbl) {
        return repo.saveAll(itrbl);
    }

    public void delete(User t) {
        repo.delete(t);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

}
