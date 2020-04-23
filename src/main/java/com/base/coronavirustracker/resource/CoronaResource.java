package com.base.coronavirustracker.resource; 

import java.util.List;

import com.base.coronavirustracker.model.LocationStats;
import com.base.coronavirustracker.services.CoronaVirusDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/cases")
public class CoronaResource {

    @Autowired
    private CoronaVirusDataService service;

    
    @GetMapping()
    public ResponseEntity<List<LocationStats>> findAll() {
        List<LocationStats> listDto = service.getAllStats();
        return ResponseEntity.ok().header("Access-Control-Allow-Origin", "*").body(listDto);
    }
}
