package com.zentriq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.zentriq.ui.DesktopChatApp;

import javafx.application.Application;

@SpringBootApplication
public class ZentriqApplication {

	private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        // Start Spring Boot
        context = new SpringApplicationBuilder(ZentriqApplication.class)
                .headless(false) // needed for JavaFX
                .run(args);

        DesktopChatApp.springContext = context;
        Application.launch(DesktopChatApp.class, args);
        // Launch JavaFX
        //javafx.application.Application.launch(com.zentriq.ui.DesktopChatApp.class, args);
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }

}
