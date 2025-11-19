package com.redsismica.demo.presentation;

import com.formdev.flatlaf.FlatClientProperties;
import com.redsismica.demo.presentation.EventoResumenDTO;
import com.redsismica.demo.gestores.Gestor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        btnRegistrarResultado.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRegistrarResultado.putClientProperty(FlatClientProperties.STYLE, """
    background: #0078FF;
    foreground: #FFFFFF;
    arc: 25;
    borderWidth: 0;
    focusWidth: 2;
""");
        btnRegistrarResultado.setMargin(new Insets(15, 30, 15, 30));
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
        btnSeleccionar.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnSeleccionar.putClientProperty(FlatClientProperties.STYLE, """
        background: #005BCE;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnSeleccionar.setMargin(new Insets(10, 25, 10, 25));

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
        // Limpiar y configurar el layout para la vista de detalle
        panelRevision.removeAll();
        panelRevision.setLayout(new BorderLayout());
        panelRevision.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Contenedor principal para la información (usa BoxLayout para apilar secciones generales)
        JPanel contenidoVertical = new JPanel();
        contenidoVertical.setLayout(new BoxLayout(contenidoVertical, BoxLayout.Y_AXIS));
        contenidoVertical.setOpaque(false);


        // ---- Título de la vista ----
        JLabel tituloDetalle = new JLabel("Detalles del Evento Seleccionado", SwingConstants.LEFT);
        tituloDetalle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloDetalle.putClientProperty(FlatClientProperties.STYLE, "foreground: #003E9C;");
        tituloDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
        contenidoVertical.add(tituloDetalle);
        contenidoVertical.add(Box.createRigidArea(new Dimension(0, 20)));

        // ----- Datos generales del evento -----
        JPanel panelDatosGenerales = new JPanel(new GridLayout(1, 3, 20, 10));
        panelDatosGenerales.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDatosGenerales.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(0, 120, 255), 1),
                        "Datos Generales del Evento"
                ),
                new EmptyBorder(10, 10, 10, 10)
        ));

        panelDatosGenerales.setOpaque(false);

        String[] etiquetas = {"Alcance:", "Clasificación:", "Origen:"};
        for (int i = 0; i < datosSismicos.size(); i++) {
            JLabel lblDato = new JLabel(etiquetas[i] + " " + datosSismicos.get(i));
            lblDato.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblDato.setForeground(new Color(50, 50, 50));
            panelDatosGenerales.add(lblDato);
        }

        contenidoVertical.add(panelDatosGenerales);
        contenidoVertical.add(Box.createRigidArea(new Dimension(0, 25)));


        // --- Secciones de Series Temporales ---

        JLabel lblSeries = new JLabel("Series Temporales Asociadas");
        lblSeries.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeries.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSeries.putClientProperty(FlatClientProperties.STYLE, "foreground: #0078FF;");
        contenidoVertical.add(lblSeries);
        contenidoVertical.add(Box.createRigidArea(new Dimension(0, 10)));


        // NUEVO CONTENEDOR HORIZONTAL para las series
        // Usamos BoxLayout en eje X para apilarlas horizontalmente.
        JPanel panelSeriesHorizontal = new JPanel();
        panelSeriesHorizontal.setLayout(new BoxLayout(panelSeriesHorizontal, BoxLayout.X_AXIS));
        panelSeriesHorizontal.setOpaque(false);

        // *** CÁLCULO DE ANCHO PARA EL SCROLL HORIZONTAL ***
        int anchoSerie = 300;
        int separacion = 15;
        int anchoTotalSeries = 0;

        // BUCLE DE CONSTRUCCIÓN DE SERIES
        for (SerieDTO serie : eventoCompletoDTO.series) {

            // --- Panel Contenedor de la Serie (el que va a la columna) ---
            JPanel panelSerie = new JPanel();
            panelSerie.setLayout(new BoxLayout(panelSerie, BoxLayout.Y_AXIS));
            panelSerie.setAlignmentY(Component.TOP_ALIGNMENT); // Alinear contenido arriba
            panelSerie.setPreferredSize(new Dimension(anchoSerie, 600));
            panelSerie.setMinimumSize(new Dimension(anchoSerie, 400));

            panelSerie.setOpaque(true);
            panelSerie.setBackground(new Color(240, 245, 255));
            panelSerie.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 180, 255), 1),
                    new EmptyBorder(10, 15, 10, 15)
            ));

            JLabel lblSerieTitulo = new JLabel("Serie ID: " + serie.idSerie);
            lblSerieTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblSerieTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelSerie.add(lblSerieTitulo);
            panelSerie.add(Box.createRigidArea(new Dimension(0, 10)));

            // --- Contenedor de las Muestras de esta Serie ---
            JPanel panelMuestrasContenedor = new JPanel();
            panelMuestrasContenedor.setLayout(new BoxLayout(panelMuestrasContenedor, BoxLayout.Y_AXIS));
            panelMuestrasContenedor.setOpaque(false);
            panelMuestrasContenedor.setAlignmentX(Component.LEFT_ALIGNMENT);


            for (MuestraDTO muestra : serie.muestras) {
                JPanel panelMuestra = new JPanel();
                panelMuestra.setLayout(new BoxLayout(panelMuestra, BoxLayout.Y_AXIS));
                panelMuestra.setAlignmentX(Component.LEFT_ALIGNMENT);
                panelMuestra.setOpaque(false);
                panelMuestra.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Muestra: " + muestra.fechaHoraMuestra),
                        new EmptyBorder(5, 5, 5, 5)
                ));

                for (DetalleMuestraDTO detalle : muestra.detalles) {
                    JLabel lblDetalle = new JLabel(
                            detalle.tipo.denominacion + " (" + detalle.tipo.nombreUnidadMedida + "): " +
                                    detalle.valor
                    );
                    lblDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    lblDetalle.setBorder(new EmptyBorder(2, 15, 2, 0));
                    lblDetalle.setAlignmentX(Component.LEFT_ALIGNMENT);
                    panelMuestra.add(lblDetalle);
                }

                panelMuestrasContenedor.add(panelMuestra);
                panelMuestrasContenedor.add(Box.createRigidArea(new Dimension(0, 8)));
            }

            // Agregar las muestras a la columna de la Serie
            panelSerie.add(panelMuestrasContenedor);
            panelSerie.add(Box.createVerticalGlue()); // Empuja el contenido hacia arriba

            // Agregar la columna de la Serie al contenedor horizontal
            panelSeriesHorizontal.add(panelSerie);
            panelSeriesHorizontal.add(Box.createRigidArea(new Dimension(separacion, 0))); // Separador entre series

            anchoTotalSeries += anchoSerie + separacion; // Sumar al ancho total requerido
        }

        // FORZAR EL ANCHO DEL CONTENEDOR HORIZONTAL
        if (eventoCompletoDTO.series.size() > 0) {
            anchoTotalSeries -= separacion; // Quitar la última separación sobrante
        }

        // Establecer el ancho total para que el JScrollPane sepa que necesita scroll horizontal
        panelSeriesHorizontal.setPreferredSize(new Dimension(anchoTotalSeries, 600));

        // Envolver el contenedor horizontal en un ScrollPane
        JScrollPane scrollHorizontalSeries = new JScrollPane(panelSeriesHorizontal,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollHorizontalSeries.setBorder(null);

        // Agregar el scroll horizontal (que contiene todas las series) al contenido vertical principal
        contenidoVertical.add(scrollHorizontalSeries);
        contenidoVertical.add(Box.createRigidArea(new Dimension(0, 15)));


        // Scroll pane vertical para todo el contenido (Datos Generales + Series)
        JScrollPane scroll = new JScrollPane(contenidoVertical,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        panelRevision.add(scroll, BorderLayout.CENTER);

        // ====================================================================
        // ---- NUEVO PANEL DE ACCIONES CON 5 BOTONES ----
        // ====================================================================

        // Panel contenedor de los botones
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // 15px de espacio horizontal

        // --- Botón 1: Visualizar Mapa (Información) ---
        JButton btnVisualizarMapa = new JButton("Visualizar Mapa");
        btnVisualizarMapa.setMargin(new Insets(10, 20, 10, 20));
        btnVisualizarMapa.putClientProperty(FlatClientProperties.STYLE, """
        background: #2196F3;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnVisualizarMapa.addActionListener(e -> { /* Acción pendiente */ });

        // --- Botón 2: Modificar Datos (Secundario/Advertencia) ---
        JButton btnModificarDatos = new JButton("Modificar Datos");
        btnModificarDatos.setMargin(new Insets(10, 20, 10, 20));
        btnModificarDatos.putClientProperty(FlatClientProperties.STYLE, """
        background: #FF9800;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnModificarDatos.addActionListener(e -> { /* Acción pendiente */ });

        // --- Botón 3: Confirmar Evento (Principal/Éxito) ---
        JButton btnConfirmarEvento = new JButton("Confirmar Evento");
        btnConfirmarEvento.setFont(new Font("Segoe UI", Font.BOLD, 17));
        btnConfirmarEvento.setMargin(new Insets(10, 25, 10, 25));
        btnConfirmarEvento.putClientProperty(FlatClientProperties.STYLE, """
        background: #4CAF50;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnConfirmarEvento.addActionListener(e -> { /* Acción pendiente */ });

        // --- Botón 4: Rechazar Evento (Peligro/Acción negativa) ---
        JButton btnRechazarEvento = new JButton("Rechazar Evento");
        btnRechazarEvento.setMargin(new Insets(10, 20, 10, 20));
        btnRechazarEvento.putClientProperty(FlatClientProperties.STYLE, """
        background: #F44336;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnRechazarEvento.addActionListener(e -> { tomarSeleccionRechazo(); });

        // --- Botón 5: Solicitar Revisión a Experto (Secundario) ---
        JButton btnSolicitarRevision = new JButton("Solicitar Revisión a Experto");
        btnSolicitarRevision.setMargin(new Insets(10, 20, 10, 20));
        btnSolicitarRevision.putClientProperty(FlatClientProperties.STYLE, """
        background: #9E9E9E;
        foreground: #FFFFFF;
        arc: 15;
        borderWidth: 0;
    """);
        btnSolicitarRevision.addActionListener(e -> { /* Acción pendiente */ });


        // Añadir todos los botones al panel
        panelAcciones.add(btnVisualizarMapa);
        panelAcciones.add(btnModificarDatos);
        panelAcciones.add(btnConfirmarEvento);
        panelAcciones.add(btnRechazarEvento);
        panelAcciones.add(btnSolicitarRevision);


        // Reemplazar el antiguo panelBotonSur
        panelAcciones.setBorder(new EmptyBorder(10, 0, 0, 0));
        panelRevision.add(panelAcciones, BorderLayout.SOUTH);

        panelRevision.revalidate();
        panelRevision.repaint();
    }

    private void tomarSeleccionRechazo() {
        gestor.tomarSeleccionRechazo(this);
        JOptionPane.showMessageDialog(
                this,
                "El evento ha sido rechazado con éxito!",
                "Rechazo completado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
