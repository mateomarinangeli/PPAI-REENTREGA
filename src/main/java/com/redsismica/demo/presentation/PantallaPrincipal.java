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

        // ---- Botón Seleccionar Evento ----
        JButton btnSeleccionar = new JButton("Seleccionar Evento");
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSeleccionar.putClientProperty(FlatClientProperties.STYLE, """
        background: #0078FF;
        foreground: #FFFFFF;
        arc: 20;
        borderWidth: 0;
        focusWidth: 2;
    """);

        btnSeleccionar.addActionListener(e -> tomarSeleccionEvento(tabla));

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSeleccionar);

        panelRevision.add(panelBoton, BorderLayout.SOUTH);

        // Refrescar UI
        panelRevision.revalidate();
        panelRevision.repaint();

        // Cambiar pantalla si aún no está visible
        cardLayout.show(panelCards, "revision");
    }

    private void tomarSeleccionEvento(JTable tabla) {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un evento de la tabla.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // El ID está en la columna 0
        int idEvento = (int) tabla.getValueAt(fila, 0);

        gestor.tomarSeleccionEvento(idEvento, this);
    }

    public void mostrarDatosEvento(List<String> datosSismicos, EventoCompletoDTO eventoCompletoDTO) {
        panelRevision.removeAll();
        panelRevision.setLayout(new BorderLayout(10, 10));
        panelRevision.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel principal con scroll
        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        // ----- Datos generales del evento -----
        JPanel panelDatosGenerales = new JPanel(new GridLayout(1, 3, 15, 10));
        panelDatosGenerales.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE, 1), "Datos Generales"
        ));

        JLabel lblAlcance = new JLabel("Alcance: " + datosSismicos.get(0));
        lblAlcance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblClasificacion = new JLabel("Clasificación: " + datosSismicos.get(1));
        lblClasificacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lblOrigen = new JLabel("Origen: " + datosSismicos.get(2));
        lblOrigen.setFont(new Font("Segoe UI", Font.BOLD, 14));

        panelDatosGenerales.add(lblAlcance);
        panelDatosGenerales.add(lblClasificacion);
        panelDatosGenerales.add(lblOrigen);

        contenido.add(panelDatosGenerales);
        contenido.add(Box.createRigidArea(new Dimension(0, 15)));

        // ----- Series temporales -----
        for (SerieDTO serie : eventoCompletoDTO.series) {
            JPanel panelSerie = new JPanel();
            panelSerie.setLayout(new BoxLayout(panelSerie, BoxLayout.Y_AXIS));
            panelSerie.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
                    "Serie ID: " + serie.idSerie
            ));
            panelSerie.setBackground(new Color(245, 245, 245));
            panelSerie.setOpaque(true);
            panelSerie.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelSerie.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(5, 5, 5, 5),
                    panelSerie.getBorder()
            ));

            // ----- Muestras de la serie -----
            for (MuestraDTO muestra : serie.muestras) {
                JPanel panelMuestra = new JPanel();
                panelMuestra.setLayout(new BoxLayout(panelMuestra, BoxLayout.Y_AXIS));
                panelMuestra.setBorder(BorderFactory.createTitledBorder(
                        "Muestra: " + muestra.fechaHoraMuestra
                ));
                panelMuestra.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Detalles de la muestra
                for (DetalleMuestraDTO detalle : muestra.detalles) {
                    JLabel lblDetalle = new JLabel(
                            detalle.tipo.denominacion + " (" + detalle.tipo.nombreUnidadMedida + "): " +
                                    detalle.valor
                    );
                    lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    lblDetalle.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 0));
                    panelMuestra.add(lblDetalle);
                }

                panelSerie.add(panelMuestra);
                panelSerie.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            contenido.add(panelSerie);
            contenido.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Scroll pane para todo el contenido
        JScrollPane scroll = new JScrollPane(contenido,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // suaviza scroll

        panelRevision.add(scroll, BorderLayout.CENTER);
        panelRevision.revalidate();
        panelRevision.repaint();
    }


}
