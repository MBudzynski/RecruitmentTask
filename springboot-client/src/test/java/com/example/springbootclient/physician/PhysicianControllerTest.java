package com.example.springbootclient.physician;

import com.example.springbootclient.exception.NoExistException;
import com.example.springbootclient.visit.Visit;
import com.example.springbootclient.visit.VisitDto;
import com.example.springbootclient.visit.VisitRepository;
import com.example.springbootclient.visit.VisitService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PhysicianControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private PhysicianService service;


    @Test
    @Transactional
    void shouldGetAllPhysicians() throws Exception {
        //given
        Physician physician = populatePhysician();
        physicianRepository.save(physician);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/physicians"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();
        //than
        List<PhysicianDto> physicianList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<PhysicianDto>>(){});
        Optional<PhysicianDto> physicianResponse = physicianList.stream().filter(physicianDto -> physicianDto.getId().equals(physician.getId())).findFirst();
        assertThat(physicianResponse.isPresent()).isTrue();
        assertThat(physicianResponse.get().getId()).isEqualTo(physician.getId());
        assertThat(physicianResponse.get().getFirstName()).isEqualTo(physician.getFirstName());
        assertThat(physicianResponse.get().getLastName()).isEqualTo(physician.getLastName());
        assertThat(physicianResponse.get().getSpecialization()).isEqualTo(physician.getSpecialization());
    }

    @Test
    @Transactional
    void shouldGetPhysicianVisits() throws Exception {
        //given
        Physician physician = populatePhysicianWithVisit();
        physicianRepository.save(physician);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/physicians/" + physician.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();
        //than
        List<VisitDto> visitDtList = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<VisitDto>>(){});
        assertThat(visitDtList.size()).isEqualTo(3);
        assertThat(visitDtList.get(1).getPatientFirstName()).isEqualTo("Kasia");
        assertThat(visitDtList.get(1).getPatientLastName()).isEqualTo("Brzoza");
        assertThat(visitDtList.get(2).getPatientFirstName()).isEqualTo("Martyna");
        assertThat(visitDtList.get(2).getPatientLastName()).isEqualTo("Martyna");
    }

    @Test
    @Transactional
    void shouldThrowExceptionWhenPhysicianToGetVisitListNotExisted() {
        //given
        Physician physician = populatePhysicianWithVisit();
        physicianRepository.save(physician);
        //when
        NoExistException noExistException = Assertions.assertThrows(NoExistException.class,
                () -> service.returnPhysicianVisitsList(physician.getId() + 10L));
        //than
        assertThat(noExistException.getMessage()).isEqualTo(String.format("Physician with id %d not exist", physician.getId()+10L));
    }

    @Test
    @Transactional
    void shouldDeletePhysicianByID() throws Exception {
        //given
        Physician physician = populatePhysicianWithVisit();
        physicianRepository.save(physician);
        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/physicians/" + physician.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();
        //than
        List<Physician> physicianList = physicianRepository.findAll();
        Optional<Physician> physicianResponse = physicianList.stream().filter(physicianDto -> physicianDto.getId().equals(physician.getId())).findFirst();
        assertThat(physicianResponse.isPresent()).isFalse();

    }


    @Test
    @Transactional
    void shouldThrowExceptionWhenPhysicianToDeleteNotExisted() {
        //given
        Physician physician = populatePhysicianWithVisit();
        physicianRepository.save(physician);
        //when
        NoExistException noExistException = Assertions.assertThrows(NoExistException.class,
                () -> service.deletePhysicians(physician.getId() + 10L));
        //than
        assertThat(noExistException.getMessage()).isEqualTo(String.format("Physician with id %d not exist", physician.getId()+10L));
    }

    @Test
    @Transactional
    void shouldAddPhysicianToDatabase() throws Exception {
        //given
        Physician physician = populatePhysician();
        String json = objectMapper.writeValueAsString(physician);
        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addPhysician")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andReturn();

        //than
        Physician physicianResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Physician.class);
        Optional<Physician> physicianFromDatabase = physicianRepository.findById(physicianResponse.getId());

        assertThat(physicianFromDatabase.isPresent()).isTrue();
        assertThat(physicianResponse.getFirstName()).isEqualTo(physicianFromDatabase.get().getFirstName());
        assertThat(physicianResponse.getLastName()).isEqualTo(physicianFromDatabase.get().getLastName());
        assertThat(physicianResponse.getSpecialization()).isEqualTo(physicianFromDatabase.get().getSpecialization());
    }


    private Physician populatePhysicianWithVisit(){
        List<Visit> visitList1 = new ArrayList<>();
        Physician physician = new Physician().toBuilder()
                .firstName("Janusz")
                .lastName("Nosacz")
                .specialization("Wszystkie")
                .visitList(visitList1)
                .visitList(populateVisitsList())
                .build();
        return physician;
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