package com.example.springbootclient.visit;

import com.example.springbootclient.exception.NoExistException;
import com.example.springbootclient.physician.Physician;
import com.example.springbootclient.physician.PhysicianRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private VisitService service;

    @Test
    @Transactional
    void shouldDeleteOneVisit() throws Exception {
        //given
        List<Visit> visitList = populateVisitsList();
        visitRepository.saveAll(visitList);
        Long id = visitList.get(0).getId();
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/visit/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();
        //than
        Optional<Visit> visit0 = visitRepository.findById(id);
        Optional<Visit> visit1 = visitRepository.findById(visitList.get(1).getId());
        Optional<Visit> visit2 = visitRepository.findById(visitList.get(2).getId());
        assertThat(visit0.isPresent()).isFalse();
        assertThat(visit1.isPresent()).isTrue();
        assertThat(visit1.get().getPatientFirstName()).isEqualTo("Kasia");
        assertThat(visit1.get().getPatientLastName()).isEqualTo("Brzoza");
        assertThat(visit2.isPresent()).isTrue();
        assertThat(visit2.get().getPatientFirstName()).isEqualTo("Martyna");
        assertThat(visit2.get().getPatientLastName()).isEqualTo("Martyna");
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenVisitWithIdNotExisted() {
        //given
        List<Visit> visitList = populateVisitsList();
        visitRepository.saveAll(visitList);
        Long id = visitList.get(0).getId();
        //when
        NoExistException noExistException = Assertions.assertThrows(NoExistException.class,
                () -> service.deleteVisit(id + 20L));
        //than
        assertThat(noExistException.getMessage()).isEqualTo(String.format("Visit with id %d not exist", id+20L));

    }

    @Test
    @Transactional
    void shouldAddVisitToPhysician() throws Exception {
        //given
        Physician physician = populatePhysician();
        physicianRepository.save(physician);
        VisitDto visit = populateVisits();
        visit.setId(physician.getId());
        String json = objectMapper.writeValueAsString(visit);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/addVisit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();
        //than
        List<Visit> visitList = visitRepository.findAll();
        Optional<Visit> visitResult = visitList.stream()
                .filter(visit1 -> visit1.getPhysician().getId().equals(physician.getId())).findFirst();
        assertThat(visitResult.isPresent()).isTrue();
        assertThat(visitResult.get().getPatientFirstName()).isEqualTo("Krystyna");
        assertThat(visitResult.get().getPatientLastName()).isEqualTo("ZGazowni");
    }

    @Test
    @Transactional
    void shouldThrowExceptionPhysicianWithIdNotExisted() {
        //given
        Physician physician = populatePhysician();
        physicianRepository.save(physician);
        VisitDto dto = populateVisits();
        dto.setId(physician.getId() +20L);
        //when
        NoExistException noExistException = Assertions.assertThrows(NoExistException.class,
                () -> service.saveVisitToPhysician(dto));
        //than
        assertThat(noExistException.getMessage()).isEqualTo(String.format("Physician with id %d not exist", physician.getId()+20L));

    }

    private VisitDto populateVisits() {

        Visit visit = new Visit().toBuilder()
                .patientFirstName("Krystyna")
                .patientLastName("ZGazowni")
                .dateVisit(LocalDate.now())
                .hourVisit(LocalTime.now().plusHours(1L)).build();
        return VisitDto.toDto(visit);
    }


    private List<Visit> populateVisitsList() {

        List<Visit> visitList = new ArrayList<>();

        Visit visit1 = new Visit().toBuilder()
                .patientFirstName("Krystyna")
                .patientLastName("ZGazowni")
                .dateVisit(LocalDate.now())
                .hourVisit(LocalTime.now().plusHours(1L)).build();

        Visit visit2 = new Visit().toBuilder()
                .patientFirstName("Kasia")
                .patientLastName("Brzoza")
                .dateVisit(LocalDate.now())
                .hourVisit(LocalTime.now().plusHours(2L)).build();

        Visit visit3 = new Visit().toBuilder()
                .patientFirstName("Martyna")
                .patientLastName("Martyna")
                .dateVisit(LocalDate.now())
                .hourVisit(LocalTime.now().plusHours(3L)).build();

        visitList.add(visit1);
        visitList.add(visit2);
        visitList.add(visit3);

        return visitList;
    }

    private Physician populatePhysician(){
        List<Visit> visitList1 = new ArrayList<>();
        Physician physician = new Physician().toBuilder()
                .firstName("Janusz")
                .lastName("Nosacz")
                .specialization("Wszystkie")
                .visitList(visitList1)
                .build();
        return physician;
    }

}