package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditConfig {

    @Bean //아까 쓴 구현체(AuditorAwareImpl) 빈 등록
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
