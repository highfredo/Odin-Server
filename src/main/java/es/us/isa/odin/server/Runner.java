package es.us.isa.odin.server;

import org.springframework.boot.SpringApplication;

public class Runner {
    public static void main(String[] args) {
    	String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        System.setProperty("server.port", webPort);
        SpringApplication.run(AppConfiguration.class);
    }
}
