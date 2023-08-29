package com.example.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TestController {
    @ResponseBody
    @RequestMapping(value = "/test")
    public String testDemo(String input, HttpServletResponse response) throws IOException {
        System.out.println(response);
        return "Hello World!";
    }
}
