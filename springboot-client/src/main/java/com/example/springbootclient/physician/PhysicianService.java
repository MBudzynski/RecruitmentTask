package com.example.springbootclient.physician;

import com.example.springbootclient.exception.NoExistException;
import com.example.springbootclient.visit.VisitDto;
import com.example.springbootclient.visit.VisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhysicianService {

    private final PhysicianRepository physicianRepository;

    Logger logger = LoggerFactory.getLogger(PhysicianService.class);

    public PhysicianService(PhysicianRepository physicianRepository) {
        this.physicianRepository = physicianRepository;
    }

    public List<PhysicianDto> getAllPhysicians() {
        return  physicianRepository.findAll().stream().map(PhysicianDto::toDto).collect(Collectors.toList());
    }

    public void addPhysician(PhysicianDto physicianDto) {
        physicianRepository.save(physicianDto.fromDto());
    }


    public List<VisitDto> returnPhysicianVisitsList(Long id) {
        Optional<Physician> physician = physicianRepository.findById(id);
        if(physician.isPresent()){
            logger.info("All visits physician with id " + id + "are get");
            return physician.get().getVisitList().stream().map(VisitDto::toDto).collect(Collectors.toList());
        } else {
            logger.info("Attempt get physician visits have finished Error:");
            throw new NoExistException(String.format("Physician with id %d not exist", id));
        }
    }

    public void deletePhysicians(Long id) {
        if(physicianRepository.existsById(id)){
            physicianRepository.deleteById(id);
            logger.info("Physician with id " + id + "is deleted");
        } else {
            logger.info("Attempt to delete physician have finished Error:");
            throw new NoExistException(String.format("Physician with id %d not exist", id));
        }
    }

    public PhysicianDto saveNewPhysician(PhysicianDto physicianDto) {
        logger.info("New physician " + physicianDto.getFirstName() + " " + physicianDto.getFirstName() + "was added to database");
        return PhysicianDto.toDto(physicianRepository.save(physicianDto.fromDto()));
    }
}
