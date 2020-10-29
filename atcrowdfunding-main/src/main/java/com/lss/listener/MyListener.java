package com.lss.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/*监听ServletContext的创建和销毁*/
public class MyListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /*监听ServletContext的创建，获取当前项目的路径，存储到ServletContext域中*/
        ServletContext servletContext = servletContextEvent.getServletContext();
        String appPath = servletContext.getContextPath();//当前项目的路径（名字）
        servletContext.setAttribute("appPath",appPath);//存储路径到ServletContext域中

        /*打印查看获取的项目路径*/
        System.out.println(appPath);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
