package com.redsismica.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.redsismica.demo.config.DatabaseInitializer;

@Component
public class DatabaseInitializerRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        DatabaseInitializer.initialize();
    }
}
