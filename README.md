# SupportZone – Sistema de Gestión de Solicitudes de Soporte Técnico

## 1. Descripción del proyecto

SupportZone es una aplicación REST desarrollada para gestionar solicitudes de soporte técnico de una empresa de servicios tecnológicos.

El sistema permite registrar, consultar, actualizar y eliminar solicitudes, facilitando el control de incidencias y la organización de la atención técnica.

## 2. Objetivo

Desarrollar una API REST que permita administrar solicitudes de soporte técnico de manera organizada, aplicando los principios de programación orientada a objetos y una arquitectura por capas.

## 3. Tecnologías utilizadas

- Java 21
- Spring Boot 4.1.2
- Maven
- Spring Web
- Spring Validation
- Visual Studio Code
- Postman
- Git y GitHub

## 4. Estructura del proyecto

```text
src/main/java/com/supportzone/
├── controller/
│   └── SolicitudController.java
├── dto/
│   └── SolicitudDTO.java
├── exception/
│   └── GlobalExceptionHandler.java
├── model/
│   ├── Solicitud.java
│   ├── Cliente.java
│   └── Tecnico.java
├── service/
│   ├── SolicitudService.java
│   └── SolicitudServiceImpl.java
└── SupportzoneApplication.java
```

## 5. Funcionalidades principales

- Registrar nuevas solicitudes de soporte.
- Listar todas las solicitudes.
- Consultar una solicitud por su identificador.
- Actualizar la información de una solicitud.
- Eliminar solicitudes.
- Validar los datos ingresados.
- Gestionar errores mediante un controlador global.

## 6. Endpoints de la API REST

| Método | Endpoint | Descripción |
|---|---|---|
| GET | /api/solicitudes | Listar solicitudes |
| GET | /api/solicitudes/{id} | Buscar por ID |
| POST | /api/solicitudes | Registrar solicitud |
| PUT | /api/solicitudes/{id} | Actualizar solicitud |
| DELETE | /api/solicitudes/{id} | Eliminar solicitud |

## 7. Validaciones

El sistema verifica que los campos obligatorios estén completos y que la prioridad y el estado tengan valores permitidos.

**Prioridades:** ALTA, MEDIA y BAJA.

**Estados:** PENDIENTE, EN_PROCESO y RESUELTA.

Los errores de validación se gestionan de manera centralizada.

## 8. Instalación y ejecución

### Requisitos previos

- Tener instalado Java JDK 21.
- Tener Git instalado.
- Tener conexión a Internet para descargar las dependencias Maven.

### Pasos

1. Clonar el repositorio:

```bash
git clone https://github.com/araceliporras2798-cmyk/supportzone.git
```

2. Ingresar a la carpeta:

```bash
cd supportzone
```

3. Ejecutar la aplicación en Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

4. La API estará disponible en:

```text
http://localhost:8080/api/solicitudes
```

## 9. Pruebas

Se realizaron pruebas funcionales utilizando Postman para verificar:

- Registro de solicitudes.
- Consulta de solicitudes.
- Actualización de información.
- Eliminación de registros.
- Validación de datos incorrectos.
- Respuesta cuando una solicitud no existe.

## 10. Almacenamiento de información

En esta versión, las solicitudes se almacenan temporalmente en memoria mediante una colección Java. Por ello, los registros se reinician cuando se detiene la aplicación.

## 11. Integrantes del equipo

Proyecto académico desarrollado en equipo para el curso Desarrollo de los Componentes del Negocio – IDAT.
