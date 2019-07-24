package org.exampledriven.sleuth.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/{sleepInMillis}")
    public String callSleep(@RequestParam("port") int port, @PathVariable int sleepInMillis, @RequestParam(value = "message", defaultValue = "Good morning") String message) {

        SleepRequest sleepRequest = new SleepRequest(sleepInMillis, message);

        SleepResponse sleepResponse = restTemplate.postForObject("http://localhost:" + port + "/sleep", sleepRequest, SleepResponse.class);

        return "Service at port " + port + " was sleeping for : " + sleepInMillis + " until " + sleepResponse.getDate();

    }

}
