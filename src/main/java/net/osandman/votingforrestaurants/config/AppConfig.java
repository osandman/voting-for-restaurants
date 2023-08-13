package net.osandman.votingforrestaurants.config;

import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
@Slf4j
public class AppConfig {
    @Bean(destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        Server h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
        log.info("H2 {}", h2Server.getStatus());
        return h2Server;
    }
}
