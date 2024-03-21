package com.ethereal.kernel.config;
 
import com.ethereal.kernel.service.impl.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
 
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginUserDetailsService loginUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginUserDetailsService)// 设置自定义的userDetailsService
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 使用BCrypt加密密码
        return new BCryptPasswordEncoder();
    }
 
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();//开启运行iframe嵌套页面
        http//1、配置权限认证
                .authorizeRequests()
                //配置不拦截路由
                .antMatchers("/500").permitAll()
                .antMatchers("/403").permitAll()
                .antMatchers("/404").permitAll()
                .antMatchers("/websocket/**").permitAll()
                .antMatchers("/toLogin").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/profile/**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                .antMatchers("/static/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest() //任何其它请求
                .authenticated() //都需要身份认证
                .and()
                //2、登录配置表单认证方式
                .formLogin()
                .loginPage("/toLogin")//自定义登录页面的url
                .usernameParameter("username")//设置登录账号参数，与表单参数一致
                .passwordParameter("password")//设置登录密码参数，与表单参数一致
                // 告诉Spring Security在发送指定路径时处理提交的凭证，默认情况下，将用户重定向回用户来自的页面。登录表单form中action的地址，也就是处理认证请求的路径，
                // 只要保持表单中action和HttpSecurity里配置的loginProcessingUrl一致就可以了，也不用自己去处理，它不会将请求传递给Spring MVC和您的控制器，所以我们就不需要自己再去写一个/user/login的控制器接口了
                .loginProcessingUrl("/doLogin")//配置默认登录入口
                .defaultSuccessUrl("/index",true)//登录成功后默认的跳转页面路径
                .failureUrl("/toLogin?error=true")
                .and()
                //3、注销
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and()
                //4、session管理
                .sessionManagement()
                .invalidSessionUrl("/toLogin") //失效后跳转到登陆页面
                //单用户登录，如果有一个登录了，同一个用户在其他地方登录将前一个剔除下线
                //.maximumSessions(1).expiredSessionStrategy(expiredSessionStrategy())
                //单用户登录，如果有一个登录了，同一个用户在其他地方不能登录
                .maximumSessions(1).maxSessionsPreventsLogin(true);
                //5、禁用跨站csrf攻击防御
        http.csrf()
                .disable();
    }
 
}
