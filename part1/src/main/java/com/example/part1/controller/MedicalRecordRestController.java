package com.example.part1.controller;

import com.example.part1.domain.ErrorInfo;
import com.example.part1.domain.Record;
import com.example.part1.repo.RecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class MedicalRecordRestController {

    @Autowired
    RecordRepo recordRepo;

    /*Create a new record*/
    @PostMapping("/medical-records")
    public ResponseEntity<?> createRecord(@RequestBody Record record, UriComponentsBuilder ucBuilder) {
        if (recordRepo.existsById(record.getId())){
            return new ResponseEntity(new ErrorInfo("This record with ID "+record.getId() + " already exists."), HttpStatus.CONFLICT);
        }
        recordRepo.save(record);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/medical-records/{id}").buildAndExpand(record.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

}
