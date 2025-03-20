package com.edacamo.mspersons.infrastructure.exception;

public enum ResponseCode {

    // Códigos de error
    BAD_REQUEST("BAD_REQUEST_001", "Solicitud incorrecta"),
    VALIDATION_ERROR("VALIDATION_ERROR_002", "Validación fallida"),
    SQL_ERROR("SQL_ERROR_003", "Error en la consulta SQL"),
    DATA_ACCESS_ERROR("DATA_ACCESS_ERROR_004", "Error de acceso a datos"),
    TIMEOUT_ERROR("TIMEOUT_ERROR_005", "Tiempo de espera agotado"),
    NOT_FOUND("NOT_FOUND_006", "El recurso solicitado no fue encontrado"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR_007", "Error interno del servidor"),

    // Códigos de éxito
    SUCCESS("SUCCESS_001", "Operación realizada con éxito"),
    DATA_CREATED("DATA_CREATED_002", "Datos creados correctamente"),
    DATA_UPDATED("DATA_UPDATED_003", "Datos actualizados correctamente");

    private final String code;
    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // Método para obtener un ResponseCode por código
    public static ResponseCode fromCode(String code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.getCode().equals(code)) {
                return responseCode;
            }
        }
        throw new IllegalArgumentException("Código de respuesta no válido: " + code);
    }
}
