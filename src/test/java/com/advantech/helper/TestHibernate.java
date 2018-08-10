/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import com.advantech.model.User;
import java.util.Set;
import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestHibernate {
    
    @Autowired
    SessionFactory sessionFactory;
    
    @Test
    @Transactional
    @Rollback(true)
    public void testUserPojo(){
        Session session = sessionFactory.getCurrentSession();
        User user = session.get(User.class, 89);
        assertNotNull(user);
        Set userNoti = user.getUserNotifications();
        assertEquals(1, userNoti.size());
        HibernateObjectPrinter.print(user);
    }
    
}
