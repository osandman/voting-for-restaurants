package net.osandman.votingforrestaurants.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.sql.SQLException;

@Configuration
@EnableCaching
@Slf4j
public class AppConfig {

    @Bean(destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        Server h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        log.info("H2 {}", h2Server.getStatus());
        return h2Server;
    }

    @Bean
    public Jackson2ObjectMapperBuilder builder() {
        return new Jackson2ObjectMapperBuilder()
//                .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new Hibernate5JakartaModule(), new JavaTimeModule());
    }
}
