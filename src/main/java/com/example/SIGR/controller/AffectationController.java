package com.example.SIGR.controller;


import com.example.SIGR.repository.AffectationRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.util.Elements;

@RestController
@RequestMapping("/api/affectations")
@CrossOrigin(origins ="*")
public class AffectationService {

    private AffectationRepository affectationRepository;
    


}
