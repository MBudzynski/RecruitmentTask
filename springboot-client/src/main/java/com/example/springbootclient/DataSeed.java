package com.example.springbootclient;

import com.example.springbootclient.physician.Physician;
import com.example.springbootclient.physician.PhysicianRepository;
import com.example.springbootclient.visit.Visit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataSeed implements InitializingBean {

    @Autowired
    private PhysicianRepository physicianRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        populatePhysicians();
    }

    private void populatePhysicians() {
        if (physicianRepository.count() > 0) {
            return;
        }


        List<Visit> visitList1 = new ArrayList<>();
        List<Visit> visitList2 = new ArrayList<>();
        List<Visit> visitList3 = new ArrayList<>();
        List<Visit> visitList4 = new ArrayList<>();

        Physician physician1 = new Physician().toBuilder()
                .firstName("Janusz")
                .lastName("Nosacz")
                .specialization("Wszystkie")
                .visitList(visitList1)
                .build();

        Physician physician2 = new Physician().toBuilder()
                .firstName("Mariusz")
                .lastName("Kowalski")
                .specialization("Pediatra")
                .visitList(visitList2)
                .build();

        Physician physician3 = new Physician().toBuilder()
                .firstName("Joanna")
                .lastName("Wilk")
                .specialization("Kardiolog")
                .visitList(visitList3)
                .build();

        Physician physician4 = new Physician().toBuilder()
                .firstName("Wiktoria")
                .lastName("Zagarowa")
                .specialization("Rodzinny")
                .visitList(visitList4)
                .build();


        Visit visit1 = new Visit().toBuilder()
                .patientFirstName("Krystyna")
                .patientLastName("ZGazowni")
                .dateVisit(LocalDate.now())
                .physician(physician1)
                .hourVisit(LocalTime.now().plusHours(1L)).build();

        Visit visit2 = new Visit().toBuilder()
                .patientFirstName("Kasia")
                .patientLastName("Brzoza")
                .dateVisit(LocalDate.now())
                .physician(physician1)
                .hourVisit(LocalTime.now().plusHours(2L)).build();

        Visit visit3 = new Visit().toBuilder()
                .patientFirstName("Martyna")
                .patientLastName("Kowalska")
                .dateVisit(LocalDate.now())
                .physician(physician1)
                .hourVisit(LocalTime.now().plusHours(3L)).build();

        Visit visit4 = new Visit().toBuilder()
                .patientFirstName("Krystyna")
                .patientLastName("Malecka")
                .dateVisit(LocalDate.now())
                .physician(physician2)
                .hourVisit(LocalTime.now().plusHours(2L)).build();

        Visit visit5 = new Visit().toBuilder()
                .patientFirstName("Joanna")
                .patientLastName("Pytlak")
                .dateVisit(LocalDate.now())
                .physician(physician2)
                .hourVisit(LocalTime.now().plusHours(3L)).build();

        Visit visit6 = new Visit().toBuilder()
                .patientFirstName("Zofia")
                .patientLastName("Mazur")
                .dateVisit(LocalDate.now())
                .physician(physician3)
                .hourVisit(LocalTime.now().plusHours(1L)).build();

        Visit visit7 = new Visit().toBuilder()
                .patientFirstName("Barbara")
                .patientLastName("Wojda")
                .dateVisit(LocalDate.now())
                .physician(physician3)
                .hourVisit(LocalTime.now().plusHours(2L)).build();

        Visit visit8 = new Visit().toBuilder()
                .patientFirstName("Tomasz")
                .patientLastName("Wroszko")
                .dateVisit(LocalDate.now())
                .physician(physician4)
                .hourVisit(LocalTime.now().plusHours(1L)).build();


        visitList1.add(visit1);
        visitList1.add(visit2);
        visitList1.add(visit3);
        visitList2.add(visit4);
        visitList2.add(visit5);
        visitList3.add(visit6);
        visitList3.add(visit7);
        visitList4.add(visit8);



        physicianRepository.save(physician1);
        physicianRepository.save(physician2);
        physicianRepository.save(physician3);
        physicianRepository.save(physician4);
    }

}
