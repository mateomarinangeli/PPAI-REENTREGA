package com.redsismica.demo.gestores;

// Importar anotaciones de Spring
import com.redsismica.demo.domain.*;
import com.redsismica.demo.domain.state.CambioEstado;
import com.redsismica.demo.presentation.*;
import jakarta.annotation.PostConstruct;
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


import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
    private EventoSismico eventoSeleccionado;
    private Sesion sesion;


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
        this.sesion = intermediarioSesion.findById(1);
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

    public void tomarSeleccionEvento(int idEvento, PantallaPrincipal pantalla) {
        eventoSeleccionado = intermediarioEventoSismico.findById(idEvento);
        LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        List<CambioEstado> cambiosEstado = eventoSeleccionado.bloquear(fechaHoraActual);

        // Se persiste el evento y los cambios de estado
        intermediarioEventoSismico.update(eventoSeleccionado);
        intermediarioCambioEstado.update(cambiosEstado.get(0));
        intermediarioCambioEstado.insert(cambiosEstado.get(1), idEvento);

        // Se buscan los datos sísmicos
        List<String> datosSismicos = eventoSeleccionado.buscarDatosSismicos();
        EventoCompletoDTO eventoCompletoDTO = mapEventoToDTO(eventoSeleccionado);
        pantalla.mostrarDatosEvento(datosSismicos, eventoCompletoDTO);
    }

    public EventoCompletoDTO mapEventoToDTO(EventoSismico evento) {
        EventoCompletoDTO dto = new EventoCompletoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setFechaHora(evento.getFechaHoraOcurrencia().toString());

        dto.series = new ArrayList<>();
        for (SerieTemporal serie : evento.getSerieTemporal()) { // lazy loading aquí
            dto.series.add(mapSerieToDTO(serie));
        }

        return dto;
    }

    private SerieDTO mapSerieToDTO(SerieTemporal serie) {
        SerieDTO dto = new SerieDTO();
        dto.setIdSerie(serie.getIdSerieTemporal());

        dto.muestras = new ArrayList<>();
        if (serie.getMuestrasSismicas() != null) { // lazy
            for (MuestraSismica muestra : serie.getMuestrasSismicas()) {
                dto.muestras.add(mapMuestraToDTO(muestra));
            }
        }

        return dto;
    }

    private MuestraDTO mapMuestraToDTO(MuestraSismica muestra) {
        MuestraDTO dto = new MuestraDTO();
        dto.setFechaHoraMuestra(muestra.getFechaHoraMuestra());
        dto.detalles = new ArrayList<>();
        if (muestra.getDetalleMuestraSismica() != null) { // lazy
            for (DetalleMuestraSismica detalle : muestra.getDetalleMuestraSismica()) {
                dto.detalles.add(mapDetalleToDTO(detalle));
            }
        }

        return dto;
    }

    private DetalleMuestraDTO mapDetalleToDTO(DetalleMuestraSismica detalle) {
        DetalleMuestraDTO dto = new DetalleMuestraDTO();
        dto.valor = detalle.getValor();
        dto.tipo = mapTipoDeDatoToDTO(detalle.getTipoDeDato());
        return dto;
    }

    private TipoDeDatoDTO mapTipoDeDatoToDTO(TipoDeDato tipo) {
        TipoDeDatoDTO dto = new TipoDeDatoDTO();
        dto.denominacion = tipo.getDenominacion();
        dto.nombreUnidadMedida = tipo.getNombreUnidadMedida();
        return dto;
    }

    public void tomarSeleccionRechazo(PantallaPrincipal pantallaPrincipal) {
        if (!eventoSeleccionado.validarEvento()) {
            System.out.println("El evento está incompleto: le falta alcance, magnitud o origen.");
            return;
        }
        else {
            LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
            Empleado empleado = sesion.obtenerEmpleadoDeSesion();
            List<CambioEstado> cambiosEstado = eventoSeleccionado.rechazarEvento(fechaHoraActual, empleado);
            intermediarioEventoSismico.update(eventoSeleccionado);
            intermediarioCambioEstado.update(cambiosEstado.get(0));
            intermediarioCambioEstado.insert(cambiosEstado.get(1), eventoSeleccionado.getIdEvento());
        }

    }

    public void tomarSeleccionConfirmacion(PantallaPrincipal pantallaPrincipal) {
        if (!eventoSeleccionado.validarEvento()) {
            System.out.println("El evento está incompleto: le falta alcance, magnitud o origen.");
            return;
        }
        else {
            LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
            Empleado empleado = sesion.obtenerEmpleadoDeSesion();
            List<CambioEstado> cambiosEstado = eventoSeleccionado.confirmarEvento(fechaHoraActual, empleado);
            intermediarioEventoSismico.update(eventoSeleccionado);
            intermediarioCambioEstado.update(cambiosEstado.get(0));
            intermediarioCambioEstado.insert(cambiosEstado.get(1), eventoSeleccionado.getIdEvento());
        }

    }

    public void tomarSeleccionSolicitudRevisionExperto(PantallaPrincipal pantallaPrincipal) {
        if (!eventoSeleccionado.validarEvento()) {
            System.out.println("El evento está incompleto: le falta alcance, magnitud o origen.");
            return;
        }
        else {
            LocalDateTime fechaHoraActual = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
            Empleado empleado = sesion.obtenerEmpleadoDeSesion();
            List<CambioEstado> cambiosEstado = eventoSeleccionado.solicitarRevisionEvento(fechaHoraActual, empleado);
            intermediarioEventoSismico.update(eventoSeleccionado);
            intermediarioCambioEstado.update(cambiosEstado.get(0));
            intermediarioCambioEstado.insert(cambiosEstado.get(1), eventoSeleccionado.getIdEvento());
        }

    }
}
