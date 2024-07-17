package pc.gear.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        System.out.println(url + "----------------------------------");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*");

                WebMvcConfigurer.super.addCorsMappings(registry);
            }
        };
    }
}
