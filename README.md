# SupportZone – API de Solicitudes de Soporte Técnico

API RESTful desarrollada con **Java 21** y **Spring Boot 4** para que una empresa de servicios tecnológicos registre, consulte, actualice y elimine las solicitudes de soporte técnico de sus clientes. Reemplaza el registro en papel y en correos desordenados.

Los datos se guardan **en memoria** (colecciones `Map`), sin base de datos. Por eso, la información se reinicia cada vez que se detiene la aplicación.

---

## 1. Tecnologías

| Herramienta | Uso |
|---|---|
| Java 21 | Lenguaje |
| Spring Boot 4.1.2 (Spring Web MVC) | API REST |
| Spring Validation (Jakarta Bean Validation) | Validación con `@Valid`, `@NotNull`, `@NotBlank`, etc. |
| springdoc-openapi 3.1.1 | Documentación Swagger UI |
| JUnit 5 + MockMvc | Pruebas automatizadas |
| Postman | Pruebas funcionales |
| Maven Wrapper | Compilación y ejecución |

## 2. Instalación y ejecución

### Requisitos

- **JDK 21** instalado (`java -version` debe mostrar 21).
- Git.
- Conexión a internet la primera vez para descargar las dependencias.

No hace falta instalar Maven: el proyecto incluye el Maven Wrapper.

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/araceliporras2798-cmyk/supportzone.git
cd supportzone

# 2. Ejecutar la aplicación

# Windows (PowerShell o CMD)
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

| Recurso | URL |
|---|---|
| API de solicitudes | http://localhost:8080/api/solicitudes |
| Swagger UI (documentación interactiva) | http://localhost:8080/swagger-ui.html |
| Especificación OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

### Ejecutar las pruebas automatizadas

```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

## 3. Estructura del proyecto

Arquitectura en capas: **controller → service → model**, con DTOs para la entrada y salida de datos y un manejador global de excepciones.

```text
src/main/java/com/supportzone/
├── SupportzoneApplication.java
├── config/
│   └── OpenApiConfig.java
├── controller/
│   ├── SolicitudController.java
│   ├── ClienteController.java
│   └── TecnicoController.java
├── dto/
│   ├── SolicitudDTO.java
│   ├── EstadoDTO.java
│   ├── ClienteDTO.java
│   ├── TecnicoDTO.java
│   └── ErrorResponse.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── RecursoNoEncontradoException.java
│   └── ReglaNegocioException.java
├── model/
│   ├── Solicitud.java
│   ├── Cliente.java
│   ├── Tecnico.java
│   ├── Prioridad.java
│   └── EstadoSolicitud.java
└── service/
    ├── SolicitudService.java
    ├── SolicitudServiceImpl.java
    ├── ClienteService.java
    ├── ClienteServiceImpl.java
    ├── TecnicoService.java
    └── TecnicoServiceImpl.java

src/test/java/com/supportzone/
├── service/
│   └── SolicitudServiceImplTest.java
└── controller/
    └── SolicitudControllerTest.java

postman/
└── SupportZone.postman_collection.json
```

### Responsabilidad de las capas

| Capa | Responsabilidad |
|---|---|
| **model** | Clases del dominio con atributos privados, constructores, getters y setters. Una `Solicitud` tiene un `Cliente`, puede tener un `Tecnico` asignado, y su prioridad y estado son `enum`. |
| **dto** | Objetos que representan los datos que recibe y devuelve la API, con anotaciones de validación. |
| **service** | Interfaces e implementaciones que administran los datos en `ConcurrentHashMap` con IDs autoincrementales (`AtomicLong`). Aquí se aplican las reglas de negocio. |
| **controller** | Recibe las peticiones HTTP, valida los datos con `@Valid`, llama al servicio y devuelve el código HTTP correspondiente. |
| **exception** | `@RestControllerAdvice` convierte las excepciones en respuestas JSON con un formato uniforme. |

## 4. Modelo de datos

```text
Cliente 1 ──── * Solicitud * ──── 0..1 Tecnico
```

**Solicitud:** `id`, `cliente`, `tecnico`, `descripcion`, `prioridad`, `estado`, `fechaRegistro` y `fechaActualizacion`.

Al iniciar, la aplicación carga datos de ejemplo:

- 2 clientes (ID 1 y 2).
- 2 técnicos (ID 1 y 2).
- 2 solicitudes (ID 1 PENDIENTE y ID 2 EN_PROCESO).

## 5. Endpoints

### Solicitudes – `/api/solicitudes`

| Método | Ruta | Descripción | Respuesta OK | Errores |
|---|---|---|---|---|
| GET | `/api/solicitudes` | Listar todas | 200 | – |
| GET | `/api/solicitudes?estado=PENDIENTE&prioridad=ALTA` | Listar con filtros opcionales | 200 | 400 |
| GET | `/api/solicitudes/{id}` | Buscar por ID | 200 | 400, 404 |
| POST | `/api/solicitudes` | Registrar | 201 + header `Location` | 400, 404, 409 |
| PUT | `/api/solicitudes/{id}` | Actualizar todos los datos | 200 | 400, 404, 409 |
| PATCH | `/api/solicitudes/{id}/estado` | Cambiar solo el estado | 200 | 400, 404, 409 |
| DELETE | `/api/solicitudes/{id}` | Eliminar | 204 | 404 |

### Clientes y técnicos

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Buscar cliente |
| POST | `/api/clientes` | Registrar cliente |
| GET | `/api/clientes/{id}/solicitudes` | Consultar solicitudes de un cliente |
| GET | `/api/tecnicos` | Listar técnicos |
| GET | `/api/tecnicos/{id}` | Buscar técnico |
| POST | `/api/tecnicos` | Registrar técnico |

### Ejemplo: registrar una solicitud

`POST /api/solicitudes`

```json
{
  "clienteId": 1,
  "tecnicoId": 2,
  "descripcion": "La laptop de gerencia no enciende",
  "prioridad": "ALTA",
  "estado": "EN_PROCESO"
}
```

`tecnicoId` y `estado` son opcionales. Si no se envía `estado`, la solicitud queda en `PENDIENTE`.

Respuesta **201 Created**:

```json
{
  "id": 3,
  "cliente": {
    "id": 1,
    "nombre": "Ferretería El Tornillo",
    "correo": "contacto@eltornillo.pe",
    "telefono": "987654321"
  },
  "tecnico": {
    "id": 2,
    "nombre": "María Torres",
    "especialidad": "Hardware",
    "correo": "mtorres@supportzone.pe"
  },
  "descripcion": "La laptop de gerencia no enciende",
  "prioridad": "ALTA",
  "estado": "EN_PROCESO",
  "fechaRegistro": "2026-11-03T16:20:15.123",
  "fechaActualizacion": null
}
```

### Ejemplo: cambiar el estado

`PATCH /api/solicitudes/3/estado`

```json
{
  "estado": "RESUELTA"
}
```

## 6. Validaciones y reglas de negocio

| Campo | Regla |
|---|---|
| `clienteId` | Obligatorio, mayor que 0 y debe existir. |
| `tecnicoId` | Opcional, mayor que 0 y debe existir. |
| `descripcion` | Obligatoria, entre 10 y 500 caracteres. |
| `prioridad` | Obligatoria: `ALTA`, `MEDIA` o `BAJA`. |
| `estado` | Opcional: `PENDIENTE`, `EN_PROCESO` o `RESUELTA`. |
| Cliente `correo` | Formato de correo válido y no repetido. |
| Cliente `telefono` | 9 dígitos. |

**Regla de negocio:** una solicitud `EN_PROCESO` o `RESUELTA` debe tener un técnico asignado. Si no lo tiene, la API responde **409 Conflict**.

## 7. Manejo de errores

Todas las respuestas de error tienen la misma estructura.

Ejemplo de **400 Bad Request** por validación:

```json
{
  "timestamp": "2026-11-03T16:25:40.512",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Los datos enviados no son válidos",
  "path": "/api/solicitudes",
  "detalles": {
    "clienteId": "El ID del cliente es obligatorio",
    "descripcion": "La descripción es obligatoria"
  }
}
```

| Código | Cuándo ocurre |
|---|---|
| 400 Bad Request | Campos inválidos, JSON mal escrito, prioridad o estado no permitidos, ID no numérico (`/api/solicitudes/abc`). |
| 404 Not Found | La solicitud, cliente o técnico no existe, o la ruta no existe. |
| 405 Method Not Allowed | Método HTTP no soportado en la ruta. |
| 409 Conflict | Se incumple una regla de negocio, como una solicitud sin técnico asignado o un correo de cliente repetido. |
| 415 Unsupported Media Type | El cuerpo no se envió como JSON. |
| 500 Internal Server Error | Error no previsto. |

## 8. Pruebas

### Pruebas funcionales con Postman

1. Iniciar la aplicación.
2. En Postman, seleccionar **Import** y elegir `postman/SupportZone.postman_collection.json`.
3. Ejecutar la colección completa con **Run collection**, en orden y con la aplicación recién iniciada.

La colección incluye 19 peticiones, cada una con su script de verificación.

| Carpeta | Qué prueba |
|---|---|
| 1. Solicitudes (CRUD) | Listar, filtrar, registrar, buscar, actualizar (PUT), cambiar estado (PATCH) y eliminar. |
| 2. Casos de error | 404 de solicitud inexistente, 400 por validación, 400 por prioridad inválida, 400 por ID no numérico, 404 de cliente inexistente, 409 por regla de negocio y 404 de ruta inexistente. |
| 3. Clientes y técnicos | Listar y registrar clientes y técnicos, y consultar las solicitudes de un cliente. |

### Pruebas automatizadas

El comando `.\mvnw.cmd test` ejecuta:

- **SolicitudServiceImplTest:** lógica de negocio y almacenamiento en memoria (registro, IDs, filtros, actualización, eliminación y reglas de negocio).
- **SolicitudControllerTest:** endpoints con MockMvc, códigos HTTP y formato de los errores.
- **SupportzoneApplicationTests:** comprueba que la aplicación inicia correctamente.

## 9. Integrantes y roles

| Integrante | Rol | Entregable |
|---|---|---|
| Rodrigo Enrique Pichilingue Valverde | Backend – Modelos y servicios | Paquetes `model` y `service`: clases `Solicitud`, `Cliente`, `Tecnico`, enums, interfaces de servicio e implementación en memoria. |
| Aracely Yobana Porras León | Backend – API REST | Paquete `controller`: endpoints CRUD, rutas y códigos HTTP. Gestión del repositorio en GitHub. |
| Grisel Dayana Merino Garcia | Validaciones y calidad | DTOs con validaciones, `GlobalExceptionHandler`, pruebas en Postman, README, documentación e informe técnico. |

---

Proyecto académico del curso **Desarrollo de los Componentes del Negocio** – Instituto IDAT.