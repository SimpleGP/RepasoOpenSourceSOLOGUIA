# API de unidades de equipo minero Caterpillar

Esta aplicación Spring Boot implementa el registro de unidades de equipo minero solicitado en la PC2. Expone una API REST, persiste los datos en MySQL y aplica las reglas de negocio para identificadores, estado operativo, coordenadas, números de serie duplicados, auditoría y cifrado.

## Guía para crear el proyecto desde IntelliJ IDEA

Usa esta guía para crear una copia de práctica desde la ventana **New Project** mostrada en la imagen. El proyecto generado será una base vacía; después tendrás que agregar las carpetas, clases y configuración que se describen en los pasos de desarrollo más abajo.

### 1. Completar la primera pantalla

En **Generators**, selecciona **Spring Boot** y configura los campos así:

| Campo | Valor |
|---|---|
| Name | `caterpillar-mining-platform` |
| Location | La carpeta donde quieras guardar tu copia |
| Language | Java |
| Type | Maven |
| Group | `com.caterpillar.platform` |
| Artifact | `caterpillar-mining-platform` |
| Package name | `com.caterpillar.platform` |
| JDK | JDK 25 |
| Java | 25 |
| Packaging | Jar |
| Configuration | Properties |

El enunciado exige Java 25 y Spring Boot 3.5.8. Si el generador no ofrece Java 25 o esa versión de Spring Boot, selecciona la versión más cercana para generar el proyecto y luego ajusta `pom.xml` a los valores requeridos. No elijas Java 17 como muestra la primera imagen. La segunda imagen muestra Spring Boot 4.1.2; para esta solución usa Spring Boot 3.5.8, tal como pide el examen.

### 2. Seleccionar dependencias

En la pantalla siguiente agrega:

- Spring Web
- Spring Data JPA
- Validation
- MySQL Driver
- Lombok
- Spring Boot DevTools

SpringDoc OpenAPI también debe estar agregado. Si el generador no lo ofrece en la lista de dependencias, agrega `springdoc-openapi-starter-webmvc-ui` en `pom.xml` después de crear el proyecto.

### 3. Generar y revisar la estructura inicial

Pulsa **Create** y espera a que IntelliJ importe Maven. Revisa que el proyecto tenga `pom.xml`, `src/main/java` y `src/main/resources`. Cambia el nombre de la clase principal a `CaterpillarMiningPlatformApplication` si el generador creó otro nombre.

### 4. Continuar con la solución

Configura `application.properties` para usar el puerto 8080 y la base `caterpillar`. Luego sigue, en orden, las categorías de desarrollo de este README: `shared`, el dominio `mining`, persistencia, aplicación e interfaces REST. Mantén el mismo package raíz `com.caterpillar.platform` para que la clase principal detecte los componentes Spring.

## Pasos de desarrollo por categoría y carpetas

### 1. Configuración y arranque del proyecto

**Carpetas y archivos:** `pom.xml`, `src/main/resources/application.properties`, `src/main/java/com/caterpillar/platform/CaterpillarMiningPlatformApplication.java`.

1. Configurar Java 25 y Spring Boot 3.5.8 en Maven.
2. Agregar las dependencias de Spring Web, Bean Validation, Spring Data JPA, MySQL y OpenAPI.
3. Configurar el puerto 8080 y los datos de conexión a MySQL.
4. Crear la clase principal y habilitar la auditoría de JPA.

**Commit sugerido:** `chore: configurar proyecto y dependencias base`.

<!-- Sugerencia de commit al terminar esta categoría: chore: configurar proyecto y dependencias base -->

### 2. Bounded context compartido

**Carpetas:** `src/main/java/com/caterpillar/platform/shared/domain`, `shared/infrastructure` y `shared/interfaces`.

1. Crear `GeoCoordinate` como value object reutilizable, con validación de latitud entre -90 y 90 y longitud entre -180 y 180.
2. Crear `AuditableAbstractAggregateRoot` para reutilizar el identificador autogenerado y las fechas de creación y actualización.
3. Crear la estrategia física de nombres de base de datos para usar snake case y tablas plurales.
4. Crear recursos y manejadores compartidos para los errores HTTP y la configuración de OpenAPI.

**Commit sugerido:** `feat(shared): agregar coordenadas, auditoria y componentes comunes`.

<!-- Sugerencia de commit al terminar shared: feat(shared): agregar coordenadas, auditoria y componentes comunes -->

### 3. Dominio de minería

**Carpetas:** `src/main/java/com/caterpillar/platform/mining/domain/model`, `domain/services`, `domain/repositories` y `domain/exceptions`.

1. Crear `CatMiningEquipmentUnitId` como identificador de negocio generado con UUID.
2. Crear `OperationStatus` como value object con los valores `ACTIVE`, `IN_MAINTENANCE` e `INACTIVE`.
3. Crear `CreateMiningEquipmentUnitCommand` con los datos necesarios para el registro.
4. Crear `MiningEquipmentUnit` como aggregate root auditable, con campos obligatorios y validación de horas de operación no negativas.
5. Definir el contrato del servicio de comandos, el contrato del repositorio y la excepción para números de serie duplicados.

**Commit sugerido:** `feat(mining): modelar agregado y reglas del dominio`.

<!-- Sugerencia de commit al terminar el dominio: feat(mining): modelar agregado y reglas del dominio -->

### 4. Persistencia y cifrado

**Carpetas:** `src/main/java/com/caterpillar/platform/mining/infrastructure/persistence/jpa`.

1. Crear el repositorio Spring Data JPA y el adaptador que implementa el contrato del dominio.
2. Guardar el número de serie cifrado con AES-GCM mediante un `AttributeConverter`; al leer la entidad, el valor se descifra automáticamente.
3. Crear una huella HMAC-SHA-256 para el número de serie.
4. Agregar una restricción única compuesta por `assigned_mine_site` y la huella. Así se rechazan duplicados del mismo sitio incluso ante solicitudes simultáneas.

**Commit sugerido:** `feat(mining): persistir equipos y proteger numeros de serie`.

<!-- Sugerencia de commit al terminar persistencia: feat(mining): persistir equipos y proteger numeros de serie -->

### 5. Capa de aplicación

**Carpeta:** `src/main/java/com/caterpillar/platform/mining/application`.

1. Implementar el servicio de comandos.
2. Crear el aggregate root desde el comando recibido.
3. Comprobar si el mismo número de serie ya está registrado en el sitio asignado.
4. Persistir el agregado cuando se cumplen las reglas.

**Commit sugerido:** `feat(mining): implementar servicio de registro`.

<!-- Sugerencia de commit al terminar application: feat(mining): implementar servicio de registro -->

### 6. Recursos, ensambladores y endpoint

**Carpeta:** `src/main/java/com/caterpillar/platform/mining/interfaces/rest`.

1. Crear el recurso de entrada con validaciones para campos obligatorios, estado, coordenadas y horas.
2. Crear el recurso de respuesta con los identificadores, datos del equipo y fechas de auditoría.
3. Crear los ensambladores entre recursos, comandos y entidades.
4. Exponer únicamente `POST /api/v1/equipment-units`.
5. Responder con HTTP 201 y el recurso creado. Los identificadores se generan en el servidor y no se aceptan en la solicitud.

**Commit sugerido:** `feat(api): exponer registro de unidades de equipo`.

<!-- Sugerencia de commit al terminar interfaces: feat(api): exponer registro de unidades de equipo -->

### 7. Manejo de errores y documentación

**Carpetas y archivos:** `src/main/java/com/caterpillar/platform/shared/interfaces/rest`, `src/main/java/com/caterpillar/platform/shared/interfaces/rest/config` y `README.md`.

1. Devolver HTTP 400 para solicitudes inválidas, 409 para números de serie duplicados y 500 para errores inesperados.
2. Mantener los mensajes de la API en inglés, según las restricciones técnicas del examen.
3. Configurar la documentación OpenAPI y Swagger UI.
4. Completar este README con la estructura, reglas, configuración y forma de ejecución del proyecto.

**Commit sugerido:** `docs(api): documentar pasos, errores y contrato REST`.

<!-- Sugerencia de commit al terminar la documentación: docs(api): documentar pasos, errores y contrato REST -->

## Requisitos

- Java 25
- MySQL 8 o compatible
- Maven 3.9 o posterior

La base de datos se llama `caterpillar`. La URL configurada intenta crearla automáticamente si el usuario de MySQL tiene permisos.

## Configuración

La conexión local predeterminada usa `jdbc:mysql://localhost:3306/caterpillar`, el usuario `root` y la contraseña `password`. Se pueden cambiar con estas variables de entorno en PowerShell:

```powershell
$env:DATABASE_URL = "jdbc:mysql://localhost:3306/caterpillar?createDatabaseIfNotExist=true&serverTimezone=UTC"
$env:DATABASE_USERNAME = "root"
$env:DATABASE_PASSWORD = "tu-contrasena"
$env:SERIAL_ENCRYPTION_SECRET = "reemplazar-por-un-secreto-privado-largo"
```

El secreto de cifrado incluido en la configuración es solo para desarrollo local. Define `SERIAL_ENCRYPTION_SECRET` con un valor privado y estable para conservar la lectura de los números ya cifrados. Si el secreto cambia, los valores existentes no podrán descifrarse.

## Conectar MySQL Workbench

MySQL Workbench es el cliente gráfico; para que la aplicación funcione también debe estar instalado y activo **MySQL Server**.

1. Abre Workbench y pulsa el botón **+** junto a **MySQL Connections**.
2. Completa la conexión:

   | Campo | Valor |
   |---|---|
   | Connection Name | `Caterpillar Local` |
   | Connection Method | `Standard (TCP/IP)` |
   | Hostname | `127.0.0.1` |
   | Port | `3306` |
   | Username | `root` |

3. Pulsa **Store in Vault…** e ingresa `password` si no cambiaste la contraseña predeterminada del proyecto. Si configuraste otra contraseña en MySQL Server, usa esa.
4. Deja **Default Schema** vacío si todavía no existe `caterpillar`; también puedes escribir `caterpillar` cuando la base ya esté creada.
5. Pulsa **Test Connection** y luego **OK** si la conexión funciona.
6. Si la base de datos aún no existe, abre una pestaña SQL en Workbench y ejecuta:

   ```sql
   CREATE DATABASE caterpillar;
   ```

La aplicación también intenta crear esa base al iniciar, siempre que el usuario de MySQL tenga permisos. Si Workbench no conecta, confirma que el servicio **MySQL Server** esté iniciado y que host, puerto, usuario y contraseña correspondan a tu instalación.

## Ejecución

Desde la carpeta del proyecto, iniciar la aplicación con:

```powershell
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

## Solicitud de ejemplo

Enviar un `POST` a `/api/v1/equipment-units` con este cuerpo:

```json
{
  "model": "793F",
  "serialNumber": "CAT0793F00001",
  "operationStatus": "ACTIVE",
  "assignedMineSite": "North Pit",
  "gpsLatitude": -12.0464,
  "gpsLongitude": -77.0428,
  "hoursOfOperation": 1250
}
```

La respuesta exitosa tiene HTTP 201 e incluye `id`, `equipmentUnitId`, todos los datos del equipo, `createdAt` y `updatedAt`. La respuesta contiene el número de serie descifrado; en la base de datos se guarda cifrado.

## Reglas de negocio

- `operationStatus` debe ser `ACTIVE`, `IN_MAINTENANCE` o `INACTIVE`.
- La latitud debe estar entre -90 y 90; la longitud, entre -180 y 180.
- `model`, `serialNumber` y `assignedMineSite` son obligatorios.
- `hoursOfOperation` no puede ser negativo.
- No se puede registrar el mismo número de serie dos veces en el mismo sitio. Se permite registrar ese número en otro sitio.
- Los errores HTTP incluyen `timestamp`, `status`, `error`, `message` y `path`.

