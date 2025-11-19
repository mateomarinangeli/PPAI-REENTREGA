package com.redsismica.demo.presentation;

import com.formdev.flatlaf.FlatClientProperties;
import com.redsismica.demo.presentation.EventoResumenDTO;
import com.redsismica.demo.gestores.Gestor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PantallaPrincipal extends JFrame {

    private final Gestor gestor; // <-- ✔ ahora se recibe desde afuera

    private CardLayout cardLayout;
    private JPanel panelCards;

    // Componentes pantalla 1
    private JButton btnRegistrarResultado;

    // Panel pantalla 2
    private JPanel panelRevision;

    // ✔ Constructor modificado: recibe el gestor desde Spring
    public PantallaPrincipal(Gestor gestor) {
        this.gestor = gestor; // <-- se guarda el inyectado

        configurarVentana();
        inicializarComponentes();
        configurarEventos();
    }

    private void configurarVentana() {
        setTitle("Revisión Manual de Evento Sísmico");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        cardLayout = new CardLayout();
        panelCards = new JPanel(cardLayout);

        setContentPane(panelCards);
    }

    private void inicializarComponentes() {

        JPanel panelInicio = new JPanel(new BorderLayout(20, 20));
        panelInicio.putClientProperty("FlatLaf.style", "arc: 15; background: #E0F0FF;");

        JLabel titulo = new JLabel("OLAI", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titulo.putClientProperty(FlatClientProperties.STYLE, """
            foreground: #003E9C;
        """);

        panelInicio.add(titulo, BorderLayout.NORTH);

        btnRegistrarResultado = new JButton("Registrar resultado de revisión manual");
        btnRegistrarResultado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnRegistrarResultado.putClientProperty(FlatClientProperties.STYLE, """
            background: #005BCE;
            foreground: #FFFFFF;
            arc: 20;
            borderWidth: 0;
            focusWidth: 2;
        """);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.add(btnRegistrarResultado);

        panelInicio.add(centro, BorderLayout.CENTER);

        panelRevision = new JPanel(new BorderLayout());
        panelRevision.putClientProperty("FlatLaf.style", "arc: 15; background: #FFFFFF;");

        JLabel lblCargando = new JLabel("Cargando datos de revisión...", SwingConstants.CENTER);
        lblCargando.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblCargando.putClientProperty(FlatClientProperties.STYLE, """
            foreground: #003E9C;
        """);

        panelRevision.add(lblCargando, BorderLayout.CENTER);

        panelCards.add(panelInicio, "inicio");
        panelCards.add(panelRevision, "revision");
    }

    private void configurarEventos() {
        btnRegistrarResultado.addActionListener(e -> opcRegistrarResultadoRevisionManual());
    }

    private void opcRegistrarResultadoRevisionManual() {
        gestor.opcRegistrarResultadoRevisionManual(this);
    }

    public void mostrarEventosAutoDetectados(List<EventoResumenDTO> eventos) {

        // Limpiar pantalla previa
        panelRevision.removeAll();
        panelRevision.setLayout(new BorderLayout(10,10));

        // ---- Construcción de tabla ----
        String[] columnas = {
                "ID",
                "Fecha/Hora",
                "Magnitud",
                "Lat Epic.",
                "Lon Epic.",
                "Lat Hipo.",
                "Lon Hipo."
        };

        Object[][] datos = new Object[eventos.size()][columnas.length];

        for (int i = 0; i < eventos.size(); i++) {
            EventoResumenDTO e = eventos.get(i);
            datos[i] = new Object[]{
                    e.idEvento,
                    e.fechaHora,
                    e.valorMagnitud,
                    e.latEpicentro,
                    e.lonEpicentro,
                    e.latHipocentro,
                    e.lonHipocentro
            };
        }

        JTable tabla = new JTable(datos, columnas);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.setRowHeight(28);

        // FlatLaf: estilo moderno
        tabla.putClientProperty("FlatLaf.style", "rowHeight:28;");
        tabla.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- Título superior ----
        JLabel titulo = new JLabel("Eventos Autodetectados", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.putClientProperty(FlatClientProperties.STYLE, """
        foreground: #003E9C;
    """);

        panelRevision.add(titulo, BorderLayout.NORTH);
        panelRevision.add(scroll, BorderLayout.CENTER);

        // Refrescar UI
        panelRevision.revalidate();
        panelRevision.repaint();

        // Cambiar pantalla si aún no está visible
        cardLayout.show(panelCards, "revision");
    }

}
