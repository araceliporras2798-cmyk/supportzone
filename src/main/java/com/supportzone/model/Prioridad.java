package com.supportzone.model;

/**
 * Nivel de prioridad de una solicitud de soporte técnico.
 */
public enum Prioridad {
    /** Afecta la operación del cliente; se atiende primero. */
    ALTA,
    /** Afecta parcialmente; se atiende en el día. */
    MEDIA,
    /** Consulta o mejora; se atiende según disponibilidad. */
    BAJA
}
