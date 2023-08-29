package com;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/servlet")
public class servletDemo implements Servlet  {
    @Override
    public void init(ServletConfig servletConfig){
        System.out.println("init完成了初始化");
    }
    @Override
    public ServletConfig getServletConfig() {
        return null;
    }
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse){
        System.out.println("srevlet Hello World");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }
}
