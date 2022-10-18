package com.Novare.AuthExample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyApiController {

    Logger logger = LoggerFactory.getLogger(MyApiController.class);

    @GetMapping("/api/check")
    public String check(){
        return "You need to be logged in to view this endpoint";
    }

    @GetMapping("/api/check/admin")
    public String checkAdmin(){
        return "You need to be logged in as a admin to view this endpoint";
    }

}
