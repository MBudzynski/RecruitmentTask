package com.example.springbootclient.physician;

import com.example.springbootclient.visit.VisitDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhysicianController {

    private final PhysicianService physicianService;

    public PhysicianController(PhysicianService physicianService) {
        this.physicianService = physicianService;
    }

    @GetMapping("/physicians")
    List<PhysicianDto> physicianList(){
        return physicianService.getAllPhysicians();
    }

    @PostMapping("/physicians")
    void addPhysician(@RequestBody PhysicianDto physicianDto){
        physicianService.addPhysician(physicianDto);
    }

    @GetMapping("/physicians/{id}")
    List<VisitDto> returnPhysicianVisitsList(@PathVariable Long id){
        return physicianService.returnPhysicianVisitsList(id);
    }

    @DeleteMapping("/physicians/{id}")
    ResponseEntity deletePhysicians(@PathVariable Long id){
        physicianService.deletePhysicians(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addPhysician")
    PhysicianDto saveNewPhysician(@RequestBody PhysicianDto physicianDto){
        return physicianService.saveNewPhysician(physicianDto);
    }



}
