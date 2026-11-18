# Caterpillar Mining Equipment Units API

This Spring Boot application exposes the equipment registration operation required for the Caterpillar Mining PC2 exercise. It stores mining equipment units in MySQL and applies the domain rules for identifiers, operating status, geographic coordinates, serial number uniqueness, auditing, and serial number encryption.

## Development steps and simulated commit points

The timeline below simulates ten-minute development increments. Each HTML comment marks where a commit would be made during a timed exercise; no ten-minute wait is required.

### 00:00 - Create the Spring Boot project

Set the Maven coordinates, Java 25, Spring Boot 3.5.8, and the required dependencies for Spring Web, Bean Validation, Spring Data JPA, MySQL, and OpenAPI. Add the application entry point and configure the server to listen on port 8080.

<!-- Suggested commit at +00:10: chore: initialize Caterpillar mining platform -->

### 00:10 - Create the shared bounded context

Add the reusable `GeoCoordinate` value object with latitude and longitude bounds. Add the audited aggregate base class, JPA auditing configuration, and the physical naming strategy for snake case identifiers and plural table names.

<!-- Suggested commit at +00:20: feat(shared): add coordinates, auditing, and naming strategy -->

### 00:20 - Model the mining domain

Add the `CatMiningEquipmentUnitId` UUID value object, the `OperationStatus` value object and its allowed values, the `CreateMiningEquipmentUnitCommand`, and the `MiningEquipmentUnit` aggregate root. The aggregate checks required fields, non-negative operating hours, valid coordinates, and generates the business identifier.

<!-- Suggested commit at +00:30: feat(mining): model equipment unit aggregate and value objects -->

### 00:30 - Add persistence and serial number encryption

Create the JPA repository and domain repository adapter. The serial number is stored using AES-GCM through a JPA attribute converter and is decrypted when the entity is read. A keyed HMAC-SHA-256 fingerprint plus the assigned mine site has a database unique constraint, so concurrent registrations cannot bypass the duplicate rule.

<!-- Suggested commit at +00:40: feat(mining): persist equipment units and encrypt serial numbers -->

### 00:40 - Implement the command application layer

Define the domain command service contract and its application implementation. It checks for an existing serial number at the same mine site and persists a new aggregate only when the rule passes.

<!-- Suggested commit at +00:50: feat(mining): handle equipment registration command -->

### 00:50 - Define request and response resources

Create the validated request resource, response resource, and assemblers. Request validation restricts status values, coordinate ranges, required strings, and operating hours. The response includes the generated database and business IDs, decrypted serial number, coordinates, and audit timestamps.

<!-- Suggested commit at +01:00: feat(api): add equipment resources and assemblers -->

### 01:00 - Expose the POST endpoint

Add `POST /api/v1/equipment-units`. Successful registrations return HTTP 201 with the created resource. The controller delegates request mapping and business processing to the appropriate layers.

<!-- Suggested commit at +01:10: feat(api): expose equipment unit registration endpoint -->

### 01:10 - Handle errors and document the API

Add a shared exception handler with JSON error responses for validation failures (400), duplicate serial numbers (409), and unexpected server errors (500). Add OpenAPI metadata and endpoint descriptions in English.

<!-- Suggested commit at +01:20: feat(api): map errors and document OpenAPI endpoint -->

### 01:20 - Configure MySQL and database naming

The default connection creates and uses the `caterpillar` database on local MySQL. Override the connection settings with `DATABASE_URL`, `DATABASE_USERNAME`, and `DATABASE_PASSWORD`. Hibernate applies snake case naming and plural table names automatically.

<!-- Suggested commit at +01:30: chore(config): configure MySQL and persistence naming -->

### 01:30 - Review the deliverable

Review the endpoint contract, DDD layer boundaries, English API messages, response fields, database constraints, and README instructions. Testing is outside the stated scope of the exercise.

<!-- Suggested commit at +01:40: docs: complete setup and development walkthrough -->

## Requirements

- Java 25
- MySQL 8 or a compatible MySQL server
- Maven 3.9 or later

Create the `caterpillar` database if the MySQL account cannot create databases automatically. The configured JDBC URL creates it when permitted.

## Configuration

The default local connection is `jdbc:mysql://localhost:3306/caterpillar`, username `root`, and password `password`. Override these values through environment variables:

```powershell
$env:DATABASE_URL = "jdbc:mysql://localhost:3306/caterpillar?createDatabaseIfNotExist=true&serverTimezone=UTC"
$env:DATABASE_USERNAME = "root"
$env:DATABASE_PASSWORD = "your-password"
$env:SERIAL_ENCRYPTION_SECRET = "replace-with-a-long-private-secret"
```

The built-in encryption secret is only for local development. Set `SERIAL_ENCRYPTION_SECRET` to a stable private value before storing data that must remain readable across deployments. Changing the secret makes existing encrypted serial numbers unreadable.

## Run the application

From this directory, start the application with:

```powershell
mvn spring-boot:run
```

The API listens on `http://localhost:8080`.

## Register an equipment unit

Send a request to `POST /api/v1/equipment-units`:

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

The request does not accept `id` or `equipmentUnitId`; the application generates both. A successful response has HTTP 201 and includes all equipment fields plus `createdAt` and `updatedAt`. `serialNumber` is returned in plaintext by the API and encrypted in the database.

## Business rules and errors

- `operationStatus` must be `ACTIVE`, `IN_MAINTENANCE`, or `INACTIVE`.
- Latitude must be from -90 through 90, and longitude from -180 through 180.
- Model, serial number, and assigned mine site are required.
- Operating hours cannot be negative.
- A serial number cannot be registered twice at the same assigned mine site. The same serial number may be used at a different site.
- Invalid requests return HTTP 400, duplicates return HTTP 409, and unexpected errors return HTTP 500. Error responses contain `timestamp`, `status`, `error`, `message`, and `path`.

## API documentation

When the application is running, open [Swagger UI](http://localhost:8080/swagger-ui.html) or view the OpenAPI JSON at [http://localhost:8080/api-docs](http://localhost:8080/api-docs).
