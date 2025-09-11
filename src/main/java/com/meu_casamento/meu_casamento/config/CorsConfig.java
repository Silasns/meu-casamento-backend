package com.meu_casamento.meu_casamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Use allowedOriginPatterns p/ aceitar curingas (*.vercel.app)
                .allowedOriginPatterns(
                        "https://*.vercel.app",
                        "https://meu-casamento-git-develop-silasns-projects.vercel.app/", // se já tiver
                        "http://localhost:4200",
                        "https://meu-casamento-chi.vercel.app/"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Location")
                .allowCredentials(true) // deixe true só se realmente usa cookies/sessão
                .maxAge(3600);
    }
}
