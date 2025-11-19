package com.redsismica.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.redsismica.demo.presentation.PantallaPrincipal;

import javax.swing.*;

@SpringBootApplication(scanBasePackages = {
		"com.redsismica.demo",
		"gestores",
		"persistence",
		"domain",
		"presentation"
})
public class DemoApplication {

	public static void main(String[] args) {
		try {
			com.formdev.flatlaf.FlatIntelliJLaf.setup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SpringApplication.run(DemoApplication.class, args);
	}
}
