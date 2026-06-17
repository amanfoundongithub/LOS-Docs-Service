package com.loan_org.document_service.infrastructure.web;

import com.loan_org.document_service.infrastructure.web.interceptor.JwtInterceptor;
import com.loan_org.document_service.infrastructure.web.interceptor.MdcInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor securityInterceptor;
    private final MdcInterceptor mdcInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(mdcInterceptor)
                .addPathPatterns("/api/v1/documents/**");
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/api/v1/documents/**");
    }
}