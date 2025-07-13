package com.themis.themis_backend.dto;

import lombok.Data;

@Data
public class ConsultaDenunciaDTO {

    private String tipoDenuncia; // "anonima" o "personaReal"
    private String tipoDocumento; // DNI, Carnet de Extranjería (solo para Persona Real)
    private String numeroDocumento; // Nro. Documento (solo para Persona Real)
    private String codigoDenuncia; // Código de denuncia (para ambos)
}
