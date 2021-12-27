package com.example.jfxclient.rest;

import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.handler.SavedVisitHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;




public class VisitsRestClient {

    private static final String DELETE_VISIT_URL = "http://localhost:8080/visit/";
    private static final String ADD_VISIT_URL = "http://localhost:8080/addVisit";
    private final RestTemplate REST_TEMPLATE;

    public VisitsRestClient() {
        this.REST_TEMPLATE = new RestTemplate();
    }


    public void deleteRecord(Long id) {
        REST_TEMPLATE.delete(DELETE_VISIT_URL + id);
    }

    public void saveVisit(VisitDto visitDto, SavedVisitHandler handler) {
        ResponseEntity<VisitDto> responseEntity = REST_TEMPLATE.postForEntity(ADD_VISIT_URL,visitDto,VisitDto.class);
        if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
            handler.handle();
        }
    }
}
