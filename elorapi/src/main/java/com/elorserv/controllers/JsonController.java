package com.elorserv.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/getCenterList")
public class JsonController {

    private List<String> centerList = new ArrayList<>();

    private final String JSON_URL = "http://10.5.104.100/ikastetxeak.json";

    @GetMapping
    public List<String> getCenterList(){

		return centerList;
    }
}