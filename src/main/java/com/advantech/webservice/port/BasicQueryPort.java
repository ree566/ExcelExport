/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.port;

import com.advantech.webservice.Factory;
import com.advantech.webservice.MultiWsClient;
import com.advantech.webservice.unmarshallclass.QueryResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.tempuri.RvResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Wei.Cheng
 */
public abstract class BasicQueryPort {

    private Marshaller jaxbMarshaller;

    private Unmarshaller jaxbUnmarshaller;

    @Autowired
    private MultiWsClient client;

    @PostConstruct
    protected abstract void initJaxb();

    protected void initJaxb(Class marshallerClass, Class unmarshallerClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(marshallerClass);
        jaxbMarshaller = jaxbContext.createMarshaller();

        JAXBContext jaxbContext2 = JAXBContext.newInstance(unmarshallerClass);
        jaxbUnmarshaller = jaxbContext2.createUnmarshaller();
    }

    public List query(Object jaxbElement, Factory f) throws Exception {
        String xmlString = this.generateXmlString(jaxbElement);
        RvResponse response = client.simpleRvSendAndReceive(xmlString, f);
        Object o = unmarshalResult(response);
        QueryResult queryResult = (QueryResult) o;
        return queryResult.getQryData();
    }

    protected String generateXmlString(Object jaxbElement) throws JAXBException {
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(jaxbElement, sw);
        String xmlString = sw.toString();
        return xmlString;
    }

    protected Object unmarshalResult(RvResponse response) throws JAXBException {
        List l = response.getRvResult().getAny();
        Document doc = ((Node) l.get(1)).getOwnerDocument();
        //Skip the <diffgr:diffgram> tag, read QryData tag directly.
        Node node = doc.getFirstChild();
        if (node == null || !node.hasChildNodes()) {
            return new QueryResult() {
                @Override
                public List getQryData() {
                    return new ArrayList();
                }

                @Override
                public void setQryData(List QryData) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            };
        }
        Node mainMessageNode = node.getFirstChild();
        Object o = jaxbUnmarshaller.unmarshal(mainMessageNode);
        return o;
    }
}
