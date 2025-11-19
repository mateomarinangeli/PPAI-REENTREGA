package com.redsismica.demo;

import com.redsismica.demo.gestores.Gestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.redsismica.demo.presentation.PantallaPrincipal;

import javax.swing.*;

@Component
public class UIRunner implements CommandLineRunner {

    private final Gestor gestor;

    @Autowired
    public UIRunner(Gestor gestor) {
        this.gestor = gestor;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() -> {
            new PantallaPrincipal(gestor).setVisible(true);
        });
    }
}
