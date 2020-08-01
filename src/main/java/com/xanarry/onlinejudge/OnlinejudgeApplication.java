package com.xanarry.onlinejudge;

import com.xanarry.onlinejudge.interceptor.LoginInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@MapperScan("com.xanarry.onlinejudge.dao")
@SpringBootApplication
public class OnlinejudgeApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(OnlinejudgeApplication.class, args);
        OjConfiguration configuration = context.getBean(OjConfiguration.class);
        System.out.println(configuration);
    }
}


@Configuration
class WebApplicationConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor userInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截，但是放行所有静态资源
        registry.addInterceptor(userInterceptor()).excludePathPatterns("/css/**", "/js/**", "/fonts/**", "/img/**", "/plugin/**");
    }
}
