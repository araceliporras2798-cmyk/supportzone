package com.supportzone.model;

/**
 * Estados por los que pasa una solicitud de soporte técnico.
 */
public enum EstadoSolicitud {
    /** Registrada, aún sin atención. */
    PENDIENTE,
    /** Un técnico la está atendiendo. */
    EN_PROCESO,
    /** Atención finalizada. */
    RESUELTA
}
