package com.redsismica.demo.gestores;

// Importar anotaciones de Spring
import com.redsismica.demo.domain.EventoSismico;
import com.redsismica.demo.presentation.EventoResumenDTO;
import com.redsismica.demo.presentation.PantallaPrincipal;
import org.springframework.stereotype.Component;
import com.redsismica.demo.persistence.AlcanceSismo.IIntermediarioBDRAlcanceSismo;
import com.redsismica.demo.persistence.ClasificacionSismo.IIntermediarioBDRClasificacionSismo;
import com.redsismica.demo.persistence.Empleado.IIntermediarioBDREmpleado;
import com.redsismica.demo.persistence.EventoSismico.IIntermediarioBDREventoSismico;
import com.redsismica.demo.persistence.CambioEstado.IIntermediarioBDRCambioEstado;
import com.redsismica.demo.persistence.Estado.IIntermediarioBDREstado;
import com.redsismica.demo.persistence.MagnitudRichter.IIntermediarioBDRMagnitudRichter;
import com.redsismica.demo.persistence.EstacionSismologica.IIntermediarioBDREstacionSismologica;
import com.redsismica.demo.persistence.OrigenDeGeneracion.IIntermediarioBDROrigenGeneracion;
import com.redsismica.demo.persistence.SerieTemporal.IIntermediarioBDRSerieTemporal;
import com.redsismica.demo.persistence.Sesion.IIntermediarioBDRSesion;
import com.redsismica.demo.persistence.Sismografo.IIntermediarioBDRSismografo;
import com.redsismica.demo.persistence.Usuario.IIntermediarioBDRUsuario;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Clase Gestor: Capa de Servicio (Controlador de Negocio).
 * Sus responsabilidades son orquestar la lógica y transacciones,
 * utilizando los Intermediarios BDR inyectados.
 */
@Component
public class Gestor {

    private final IIntermediarioBDRAlcanceSismo intermediarioAlcanceSismo;
    private final IIntermediarioBDRClasificacionSismo intermediarioClasificacionSismo;
    private final IIntermediarioBDREmpleado intermediarioEmpleado;
    private final IIntermediarioBDREventoSismico intermediarioEventoSismico;
    private final IIntermediarioBDRCambioEstado intermediarioCambioEstado;
    private final IIntermediarioBDREstado intermediarioEstado;
    private final IIntermediarioBDRMagnitudRichter intermediarioMagnitudRichter;
    private final IIntermediarioBDREstacionSismologica intermediarioEstacionSismologica;
    private final IIntermediarioBDROrigenGeneracion intermediarioOrigenGeneracion;
    private final IIntermediarioBDRSerieTemporal intermediarioSerieTemporal;
    private final IIntermediarioBDRSesion intermediarioSesion;
    private final IIntermediarioBDRSismografo intermediarioSismografo;
    private final IIntermediarioBDRUsuario intermediarioUsuario;

    public Gestor(IIntermediarioBDRAlcanceSismo intermediarioAlcanceSismo,
                  IIntermediarioBDRClasificacionSismo intermediarioClasificacionSismo,
                  IIntermediarioBDREmpleado intermediarioEmpleado,
                  IIntermediarioBDREventoSismico intermediarioEventoSismico,
                  IIntermediarioBDRCambioEstado intermediarioCambioEstado,
                  IIntermediarioBDREstado intermediarioEstado,
                  IIntermediarioBDRMagnitudRichter intermediarioMagnitudRichter,
                  IIntermediarioBDREstacionSismologica intermediarioEstacionSismologica,
                  IIntermediarioBDROrigenGeneracion intermediarioOrigenGeneracion,
                  IIntermediarioBDRSerieTemporal intermediarioSerieTemporal,
                  IIntermediarioBDRSesion intermediarioSesion,
                  IIntermediarioBDRSismografo intermediarioSismografo,
                  IIntermediarioBDRUsuario intermediarioUsuario) {

        this.intermediarioAlcanceSismo = intermediarioAlcanceSismo;
        this.intermediarioClasificacionSismo = intermediarioClasificacionSismo;
        this.intermediarioEmpleado = intermediarioEmpleado;
        this.intermediarioEventoSismico = intermediarioEventoSismico;
        this.intermediarioCambioEstado = intermediarioCambioEstado;
        this.intermediarioEstado = intermediarioEstado;
        this.intermediarioMagnitudRichter = intermediarioMagnitudRichter;
        this.intermediarioEstacionSismologica = intermediarioEstacionSismologica;
        this.intermediarioOrigenGeneracion = intermediarioOrigenGeneracion;
        this.intermediarioSerieTemporal = intermediarioSerieTemporal;
        this.intermediarioSesion = intermediarioSesion;
        this.intermediarioSismografo = intermediarioSismografo;
        this.intermediarioUsuario = intermediarioUsuario;
    }

    /**
     * Método que se ejecuta al presionar el botón: simula la búsqueda de datos
     * de la base de datos para la pantalla de revisión.
     */
    public String buscarDatosParaVentana() {
        // --- INICIO DE LÓGICA DE NEGOCIO ---

        // Ejemplo de uso de un Mapper inyectado:
        // var alcances = this.intermediarioAlcanceSismo.findAll();
        // System.out.println("LOG: Recuperados " + alcances.size() + " alcances.");

        // Aquí iría la lógica para buscar los eventos sísmicos pendientes
        // y ensamblar los datos necesarios para la pantalla de revisión.

        // --- FIN DE LÓGICA DE NEGOCIO ---

        return "Datos de revisión manual obtenidos por el Gestor.";
    }

    public void opcRegistrarResultadoRevisionManual(PantallaPrincipal pantalla) {
        List<EventoSismico> eventosAutoDetectados = buscarEventosSismicosAutoDetectados();
        List<EventoSismico> eventosOrdenados = ordenarEventos(eventosAutoDetectados);
        List<EventoResumenDTO> eventosParaMostrar = prepararEventosParaMostrar(eventosOrdenados);
        pantalla.mostrarEventosAutoDetectados(eventosParaMostrar);
    }

    private List<EventoResumenDTO> prepararEventosParaMostrar(List<EventoSismico> eventosOrdenados) {

        List<EventoResumenDTO> lista = new ArrayList<>();

        for (EventoSismico evento : eventosOrdenados) {

            EventoResumenDTO dto = new EventoResumenDTO();

            dto.setIdEvento(evento.getIdEvento());
            dto.setFechaHora(evento.getFechaHoraOcurrencia().toString());
            dto.setValorMagnitud(evento.getValorMagnitud());
            dto.setLatEpicentro(evento.getLatitudEpicentro());
            dto.setLonEpicentro(evento.getLongitudEpicentro());
            dto.setLatHipocentro(evento.getLatitudHipocentro());
            dto.setLonHipocentro(evento.getLongitudHipocentro());

            // Agregá cualquier otro campo que tenga tu DTO...

            lista.add(dto);
        }

        return lista;
    }


    private List<EventoSismico> ordenarEventos(List<EventoSismico> eventosAutoDetectados) {

        // Copia opcional para no modificar la lista original
        List<EventoSismico> ordenados = new ArrayList<>(eventosAutoDetectados);

        ordenados.sort(Comparator.comparing(EventoSismico::getFechaHoraOcurrencia));

        return ordenados;
    }

    private List<EventoSismico> buscarEventosSismicosAutoDetectados() {
        List<EventoSismico> eventos = intermediarioEventoSismico.findAll();

        List<EventoSismico> autodetectados = new ArrayList<>();

        for (EventoSismico evento : eventos) {
            if (evento.esAutoDetectado()) {
                autodetectados.add(evento);
            }
        }

        return autodetectados;
    }
}