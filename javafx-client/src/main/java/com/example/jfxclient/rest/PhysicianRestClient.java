package com.example.jfxclient.rest;

import com.example.jfxclient.dto.PhysicianDto;
import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.handler.SavedPhysicianHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

public class PhysicianRestClient{

    private static PhysicianRestClient instance = null;
    private static final String GET_PHYSICIAN_URL = "http://localhost:8080/physicians";
    private static final String GET_PHYSICIAN_VISITS_URL = "http://localhost:8080/physicians/";
    private static final String GET_PHYSICIAN_DELETE_URL = "http://localhost:8080/physicians/";
    private static final String POST_PHYSICIAN_URL = "http://localhost:8080/addPhysician";
    private final RestTemplate REST_TEMPLATE;

    private PhysicianRestClient() {
        this.REST_TEMPLATE = new RestTemplate();
    }

    public static PhysicianRestClient getInstance(){
        if(instance == null){
            synchronized (PhysicianRestClient.class){
                if(instance == null){
                    instance = new PhysicianRestClient();
                }
            }
        }
        return instance;
    }

    public List<PhysicianDto> getPhysicians(){
        ResponseEntity<PhysicianDto[]> physiciansResponseEntity = REST_TEMPLATE.getForEntity(GET_PHYSICIAN_URL, PhysicianDto[].class);
        return Arrays.asList(physiciansResponseEntity.getBody());
    }

    public List<VisitDto> getPhysicianVisits(long id) {
        ResponseEntity<VisitDto[]> physiciansResponseEntity = REST_TEMPLATE.getForEntity(GET_PHYSICIAN_VISITS_URL + id , VisitDto[].class);
        return Arrays.asList(physiciansResponseEntity.getBody());
    }

    public void deleteRecord(Long id) {
        REST_TEMPLATE.delete(GET_PHYSICIAN_DELETE_URL + id);
    }

    public void savePhysician(PhysicianDto physicianDto, SavedPhysicianHandler handler) {
        ResponseEntity<PhysicianDto> responseEntity = REST_TEMPLATE.postForEntity(POST_PHYSICIAN_URL, physicianDto, PhysicianDto.class);
        if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
            handler.handle();
        }
    }


}
