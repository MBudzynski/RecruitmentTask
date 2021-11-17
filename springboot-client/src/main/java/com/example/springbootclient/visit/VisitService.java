package com.example.springbootclient.visit;

import com.example.springbootclient.exception.NoExistException;
import com.example.springbootclient.physician.Physician;
import com.example.springbootclient.physician.PhysicianRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VisitService {

    Logger logger = LoggerFactory.getLogger(VisitService.class);

    private final PhysicianRepository physicianRepository;
    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository, PhysicianRepository physicianRepository) {
        this.visitRepository = visitRepository;
        this.physicianRepository = physicianRepository;
    }

    public void deleteVisit(Long id) {
        if(visitRepository.existsById(id)){
            visitRepository.deleteById(id);
            logger.info(String.format("Visit with id %d was deleted", id));
        } else {
            throw new NoExistException(String.format("Visit with id %d not exist", id));
        }
    }

    public VisitDto saveVisitToPhysician(VisitDto visitDto) {
        Long physicianId = visitDto.getId();
        Optional<Physician> physician = physicianRepository.findById(physicianId);
        if(physician.isPresent()){
            Visit visit = prepareVisit(visitDto);
            visit.setPhysician(physician.get());
            logger.info("New visit is add to Physician with ID:" + physicianId);
            return VisitDto.toDto(visitRepository.save(visit));
        } else {
            logger.info("Attempt add Visit to physician have finished ERROR");
            throw new NoExistException(String.format("Physician with id %d not exist", physicianId));
        }
    }

    private Visit prepareVisit(VisitDto visitDto) {
        VisitDto dto = visitDto;
        visitDto.setId(null);
        return dto.fromDto();
    }
}
