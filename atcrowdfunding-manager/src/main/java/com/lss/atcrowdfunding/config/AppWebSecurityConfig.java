package com.lss.atcrowdfunding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 是 springsecurity框架的配置文件(配置类)
 *@Configuration        组合注解   扫描管理项目中的所有的组件(标注@Component注解的类)
 * @Repository  @Service
 *@EnableWebSecurity   组合注解    支持springsecurity框架中的注解
 *
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;


    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

       //将加载信息与数据库相连系(连接数据库，进行登录)
        //登录
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());//使用BCryptPasswordEncoder进行加密
    }

    //授权

    /**
     * authorize 授权
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests().antMatchers("/welcome.jsp","/static/**").permitAll()//放行
                .anyRequest().authenticated();//除index.jsp 和所有的在layui的所有静态资源  其他的请求必须要经过身份认证才能访问

        http.formLogin().loginPage("/welcome.jsp");//访问受限跳转的页面

        //实验三   改造登录页面，指定对应的控制器方法
        //使用springsecurity中默认的login控制器
        http.formLogin().loginProcessingUrl("/login")
                .usernameParameter("loginacct")
                .passwordParameter("userpswd")  //getParamemter
                .defaultSuccessUrl("/atcrowfunding/main");//登陆成功之后跳转到具体的控制器

        http.csrf().disable();//服务器端禁用csrf的验证

        //注销
        http.logout();

        //第二种处理异常的方式，实现异常处理器 自定义异常处理器
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

                if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
                    response.getWriter().write("403");
                }else{
                    request.setAttribute("err",e.getMessage());

                    request.getRequestDispatcher("/WEB-INF/views/unauth.jsp").forward(request,response);
                }

            }
        });

        //实验8 记住我 功能（默认保存两周）
        http.rememberMe();



    }
}
