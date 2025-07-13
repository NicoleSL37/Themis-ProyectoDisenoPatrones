package com.themis.themis_backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "denuncias")

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_denuncia", // Nombre de la columna que diferenciará los tipos
        discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BASE_DENUNCIA")
public abstract class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_denuncia", unique = true, nullable = false)
    String codigoDenuncia;

    @Column(name = "estado", nullable = false)
    private String estado; // Ej: "En Proceso", "Cerrada", "Archivada"

    // <<-- Campos comunes de Fecha y Hora del Incidente (normalizados a LocalDate/LocalTime) -->>
    @Column(name = "fecha_incidente")
    private LocalDate fechaIncidente;

    @Column(name = "hora_incidente")
    private LocalTime horaIncidente;
    // -->>

    @Column(name = "es_ahora", nullable = false)
    private boolean esAhora;

    @Column(name = "departamento", nullable = false) // Ahora es nullable = false para ambos
    private String departamento;

    @Column(name = "provincia", nullable = false) // Ahora es nullable = false para ambos
    private String provincia;

    @Column(name = "distrito", nullable = false) // Ahora es nullable = false para ambos
    private String distrito;

    @Lob // Para almacenar texto largo
    @Column(name = "descripcion_hechos", nullable = false)
    private String descripcionHechos;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "denuncia_archivos", // Tabla común para archivos
            joinColumns = @JoinColumn(name = "denuncia_id"))
    @Column(name = "ruta_archivo", length = 512)
    private List<String> rutasArchivos = new ArrayList<>();

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "es_anonima", nullable = false) // Columna para el campo 'anonimo'
    private boolean anonimo;

    // <<-- Constructor vacío -->>
    public Denuncia() {
        // Constructor por defecto
    }

    // <<-- Métodos @PrePersist movidos a la clase base -->>
    @PrePersist
    public void generateCodigoAndSetFechaRegistro() {
        if (this.codigoDenuncia == null || this.codigoDenuncia.isEmpty()) {
            // El prefijo se generará en los subtipos si se desea
            this.codigoDenuncia = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        }
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
        if (this.estado == null || this.estado.isEmpty()) {
            this.estado = "PENDIENTE"; // Estado por defecto
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoDenuncia() {
        return codigoDenuncia;
    }

    public void setCodigoDenuncia(String codigoDenuncia) {
        this.codigoDenuncia = codigoDenuncia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaIncidente() {
        return fechaIncidente;
    }

    public void setFechaIncidente(LocalDate fechaIncidente) {
        this.fechaIncidente = fechaIncidente;
    }

    public LocalTime getHoraIncidente() {
        return horaIncidente;
    }

    public void setHoraIncidente(LocalTime horaIncidente) {
        this.horaIncidente = horaIncidente;
    }

    public boolean isEsAhora() {
        return esAhora;
    }

    public void setEsAhora(boolean esAhora) {
        this.esAhora = esAhora;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDescripcionHechos() {
        return descripcionHechos;
    }

    public void setDescripcionHechos(String descripcionHechos) {
        this.descripcionHechos = descripcionHechos;
    }

    public List<String> getRutasArchivos() {
        return rutasArchivos;
    }

    public void setRutasArchivos(List<String> rutasArchivos) {
        this.rutasArchivos = rutasArchivos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public boolean isAnonimo() {
        return anonimo;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }

}
