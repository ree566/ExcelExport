/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Justin.Yeh
 */
public class WebApiClient {

    private final String baseUrl = "http://172.22.250.120:7878/v1/Employee/";
    private final WebClient webClient = WebClient.create();
    private List<WebApiUser> l = null;
    private boolean ifUserInAtmc = false;

    public boolean IsUserInAtmc(String jobNo) {
        l = RequestUserInAtmc(jobNo);
        return ifUserInAtmc;
    }

    public List<WebApiUser> GetUserInAtmc(String jobNo) {
        return ifUserInAtmc ? l : RequestUserInAtmc(jobNo);
    }

    private List<WebApiUser> RequestUserInAtmc(String jobNo) {

        Mono<Object[]> body = webClient
                .get()
                .uri(baseUrl + jobNo)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object[].class);
        Object[] atmcEMP;
        try {
            atmcEMP = body.block();
            ObjectMapper mapper = new ObjectMapper();
            l = mapper.convertValue(atmcEMP, new TypeReference<List<WebApiUser>>() {
            });
            ifUserInAtmc = true;
        } catch (Exception e) {
            System.out.println("Object[]  e: " + e);
            ifUserInAtmc = false;
            return null;
        }
        return l;
    }
}
