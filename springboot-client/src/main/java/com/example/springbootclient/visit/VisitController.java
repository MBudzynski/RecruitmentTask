package com.example.springbootclient.visit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @DeleteMapping("/visit/{id}")
    ResponseEntity deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addVisit")
    VisitDto saveNewVisit(@RequestBody VisitDto visitDto){
        return visitService.saveVisitToPhysician(visitDto);
    }





}
