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


    private static final String GET_PHYSICIAN_URL = "http://localhost:8080/physicians";
    private static final String GET_PHYSICIAN_VISITS_URL = "http://localhost:8080/physicians/";
    private static final String GET_PHYSICIAN_DELETE_URL = "http://localhost:8080/physicians/";
    private static final String POST_PHYSICIAN_URL = "http://localhost:8080/addPhysician";
    private final RestTemplate restTemplate;

    public PhysicianRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public List<PhysicianDto> getPhysicians(){
        ResponseEntity<PhysicianDto[]> physiciansResponseEntity = restTemplate.getForEntity(GET_PHYSICIAN_URL, PhysicianDto[].class);
        return Arrays.asList(physiciansResponseEntity.getBody());
    }

    public List<VisitDto> getPhysicianVisits(long id) {
        ResponseEntity<VisitDto[]> physiciansResponseEntity = restTemplate.getForEntity(GET_PHYSICIAN_VISITS_URL + id , VisitDto[].class);
        return Arrays.asList(physiciansResponseEntity.getBody());
    }

    public void deleteRecord(Long id) {
        restTemplate.delete(GET_PHYSICIAN_DELETE_URL + id);
    }

    public void savePhysician(PhysicianDto physicianDto, SavedPhysicianHandler handler) {
        ResponseEntity<PhysicianDto> responseEntity = restTemplate.postForEntity(POST_PHYSICIAN_URL, physicianDto, PhysicianDto.class);
        if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
            handler.handle();
        }
    }


}