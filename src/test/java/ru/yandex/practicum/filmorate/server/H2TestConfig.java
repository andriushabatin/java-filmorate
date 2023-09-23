package ru.yandex.practicum.filmorate.server;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2TestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server databaseTestServer() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
