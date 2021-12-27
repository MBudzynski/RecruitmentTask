package com.example.jfxclient.rest;

import com.example.jfxclient.dbupdateobserver.Observer;
import com.example.jfxclient.dbupdateobserver.Subject;
import com.example.jfxclient.dto.PhysicianIdHolder;
import com.example.jfxclient.dto.VisitDto;
import com.example.jfxclient.handler.SavedVisitHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


public class VisitsRestClient implements Subject {

    private static VisitsRestClient instance = null;
    private ArrayList<Observer> observerList;
    private final PhysicianIdHolder physicianIdHolder;
    private static final String DELETE_VISIT_URL = "http://localhost:8080/visit/";
    private static final String ADD_VISIT_URL = "http://localhost:8080/addVisit";
    private final RestTemplate REST_TEMPLATE;

    private VisitsRestClient() {
        this.physicianIdHolder = PhysicianIdHolder.getInstance();
        this.REST_TEMPLATE = new RestTemplate();
        this.observerList = new ArrayList<>();
    }

    public static VisitsRestClient getInstance(){
        if(instance == null){
            synchronized (VisitsRestClient.class){
                if(instance == null){
                    instance = new VisitsRestClient();
                }
            }
        }
        return instance;
    }

    public void deleteRecord(Long id) {
        REST_TEMPLATE.delete(DELETE_VISIT_URL + id);
    }

    public void saveVisit(VisitDto visitDto, SavedVisitHandler handler) {


        visitDto.setId(physicianIdHolder.getPhysicianId());
        ResponseEntity<VisitDto> responseEntity = REST_TEMPLATE.postForEntity(ADD_VISIT_URL,visitDto,VisitDto.class);
        if(HttpStatus.OK.equals(responseEntity.getStatusCode())){
            handler.handle();
            notifyObservers();
        }
    }

    @Override
    public void register(Observer o) {
        observerList.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : observerList){
            o.loadVisits();
        }
    }
}
