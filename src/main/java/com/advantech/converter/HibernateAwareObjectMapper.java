/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import javax.annotation.PostConstruct;

/**
 *
 * @author Wei.Cheng
 */
public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {
        Hibernate5Module hbm = new Hibernate5Module();
        hbm.enable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        registerModule(hbm);
    }

    @PostConstruct
    public void afterPropertiesSet() {
//        configure(SerializationFeature.EAGER_SERIALIZER_FETCH, true);
    }

}
