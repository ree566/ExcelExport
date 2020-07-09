/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.webservice.Factory;
import com.advantech.webservice.MultiWsClient;
import com.advantech.webservice.UploadType;
import java.io.StringWriter;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicUploadPort {

    private Marshaller jaxbMarshaller;

    @Autowired
    private MultiWsClient client;

    @PostConstruct
    protected abstract void initJaxbMarshaller();

    /**
     * PersistClass is for marshaller, marshall list of object generate in
     * method transformData Class type is same as class inside method
     * transformData
     *
     * @param persistClass
     * @throws JAXBException
     */
    protected void initJaxbMarshaller(Class persistClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(persistClass);
        jaxbMarshaller = jaxbContext.createMarshaller();
    }

    /**
     * 單筆object更新
     *
     * @param jaxbElement
     * @param type
     * @throws Exception
     */
    protected void upload(Object jaxbElement, UploadType type, Factory f) throws Exception {
        String xmlString = this.generateXmlString(jaxbElement);
        TxResponse response = client.simpleTxSendAndReceive(xmlString, type, f);
        if (!"OK".equals(response.getTxResult())) {
            throw new Exception(response.getTxResult());
        }
    }

    protected String generateXmlString(Object jaxbElement) throws JAXBException {
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(jaxbElement, sw);
        String xmlString = sw.toString();
        return xmlString;
    }

}
