# 🎓 Conexia – Backend API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

> Plataforma integral para conectar instituciones educativas, empleadores y egresados, facilitando la gestión de ofertas laborales, cursos y postulaciones.

---

## 📋 Tabla de Contenidos

- [Visión General](#-visión-general)
- [Características Principales](#-características-principales)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [API Reference](#-api-reference)
- [Seguridad](#-seguridad)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Troubleshooting](#-troubleshooting)
- [Contribución](#-contribución)

---

## 🎯 Visión General

Conexia es una API REST robusta que conecta el ecosistema educativo-laboral, permitiendo:

- 🏫 **Instituciones**: Gestionar cursos y egresados
- 💼 **Empleadores**: Publicar ofertas laborales y gestionar postulaciones
- 👨‍🎓 **Egresados**: Explorar oportunidades y postularse a empleos
- 🔐 **Seguridad**: Autenticación JWT con roles y permisos granulares
---
## ✨ Características Principales

### 🔐 Autenticación y Autorización
- ✅ JWT (JSON Web Tokens) para autenticación stateless
- ✅ Sistema de roles: ADMIN, INSTITUCION, EMPLEADOR, EGRESADO
- ✅ Control de permisos granular a nivel de recursos
- ✅ Validación de propiedad de recursos

### 💼 Gestión de Ofertas Laborales
- ✅ CRUD completo con validaciones de negocio
- ✅ Control de estados (ACTIVA, CERRADA, VENCIDA)
- ✅ Validación automática de fechas
- ✅ Filtrado por estado y empleador

### 📝 Sistema de Postulaciones
- ✅ Prevención de postulaciones duplicadas
- ✅ Seguimiento de estados (EN_PROCESO, ACEPTADO, RECHAZADO)
- ✅ Validación de ofertas activas
- ✅ Control de transiciones de estado

### 🎓 Gestión de Cursos
- ✅ Asociación con instituciones
- ✅ Modalidades (PRESENCIAL, ONLINE, HÍBRIDO)
- ✅ Validación de fechas de inicio/fin
- ✅ CRUD con permisos por institución

---

## 🏗️ Arquitectura

### Estructura del Proyecto

```
com.conexia/
├── 📁 config/                  # Configuración de seguridad y beans
│   ├── SecurityConfig.java
│   ├── JwtFilterValidation.java
│   └── CorsConfig.java
│
├── 📁 exceptions/              # Manejo centralizado de errores
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   └── BusinessRuleException.java
│
├── 📁 persistence/
│   ├── 📁 entity/              # Entidades JPA
│   │   ├── User.java
│   │   ├── Institution.java
│   │   ├── Employer.java
│   │   ├── Graduate.java
│   │   ├── JobOffer.java
│   │   ├── Course.java
│   │   ├── Application.java
│   │   └── Rol.java
│   └── 📁 repository/          # Repositorios Spring Data JPA
│
├── 📁 presentation/
│   └── 📁 controller/          # Controladores REST
│       ├── AuthController.java
│       ├── InstitutionController.java
│       ├── EmployerController.java
│       ├── GraduateController.java
│       ├── JobOfferController.java
│       ├── CourseController.java
│       └── ApplicationController.java
│
├── 📁 service/
│   ├── 📁 dto/                 # Data Transfer Objects
│   ├── 📁 impl/                # Implementaciones de servicios
│   └── SecurityService.java    # Lógica de autorización
│
└── 📁 utils/
    ├── 📁 jwt/                 # Utilidades JWT
    └── 📁 mapper/              # Mappers MapStruct
```

### Patrón de Capas

```
┌─────────────────────────────────────┐
│      Controller Layer (REST)        │  ← Endpoints HTTP
├─────────────────────────────────────┤
│         Service Layer               │  ← Lógica de negocio
├─────────────────────────────────────┤
│       Repository Layer (JPA)        │  ← Acceso a datos
├─────────────────────────────────────┤
│          Database (MySQL)           │  ← Persistencia
└─────────────────────────────────────┘
```

---

## 🛠️ Tecnologías

### Backend Core
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM

### Base de Datos
- **MySQL 8.0** - Base de datos relacional principal
- **H2** - Base de datos en memoria para testing

### Utilidades
- **MapStruct** - Mapeo de objetos
- **Lombok** - Reducción de boilerplate
- **JWT (jjwt)** - Generación y validación de tokens
- **Validation API** - Validaciones de entrada

### Desarrollo y Testing
- **Maven** - Gestión de dependencias
- **JUnit 5** - Testing unitario
- **Mockito** - Mocking para tests
- **Swagger/OpenAPI** - Documentación de API

---

## 🚀 Instalación

### Prerrequisitos

```bash
# Verificar versiones
java -version    # Java 21 o superior
mvn -version     # Maven 3.9 o superior
mysql --version  # MySQL 8.0 o superior
```

### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/conexia-backend.git
cd conexia-backend
```

### Paso 2: Configurar la base de datos

```sql
-- Crear base de datos
CREATE DATABASE conexia_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario (opcional)
CREATE USER 'conexia_user'@'localhost' IDENTIFIED BY 'tu_password_seguro';
GRANT ALL PRIVILEGES ON conexia_db.* TO 'conexia_user'@'localhost';
FLUSH PRIVILEGES;
```

### Paso 3: Configurar variables de entorno

Crea un archivo `.env` en la raíz del proyecto:

```env
# Database
DB_URL=jdbc:mysql://localhost:3306/conexia_db
DB_USERNAME=root
DB_PASSWORD=tu_password

# JWT
JWT_SECRET=tu_clave_super_secreta_minimo_256_bits
JWT_ISSUER=conexia-api
JWT_EXPIRATION=86400000

# Server
SERVER_PORT=8080
```

### Paso 4: Instalar dependencias

```bash
mvn clean install
```

### Paso 5: Ejecutar la aplicación

```bash
# Modo desarrollo
mvn spring-boot:run

# O compilar y ejecutar JAR
mvn package
java -jar target/conexia-backend-1.0.0.jar
```

La API estará disponible en: `http://localhost:8080`

---

## ⚙️ Configuración

### application.properties

```properties
# ===================================
# DATABASE CONFIGURATION
# ===================================
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===================================
# JPA / HIBERNATE
# ===================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# ===================================
# JWT SECURITY
# ===================================
security.jwt.secret=${JWT_SECRET}
security.jwt.issuer=${JWT_ISSUER}
security.jwt.expiration=${JWT_EXPIRATION}

# ===================================
# SERVER
# ===================================
server.port=${SERVER_PORT:8080}
server.error.include-message=always
server.error.include-binding-errors=always

# ===================================
# LOGGING
# ===================================
logging.level.com.conexia=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Perfiles de Spring

```bash
# Desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Producción
java -jar target/conexia-backend.jar --spring.profiles.active=prod
```

---

## 📚 API Reference

### Base URL

```
http://localhost:8080
```

### Autenticación

Todos los endpoints excepto `/auth/**` requieren JWT:

```http
Authorization: Bearer <tu_jwt_token>
```

---

### 🔐 Auth Endpoints

#### Registrar Institución

```http
POST /auth/register/institution
Content-Type: application/json

{
  "username": "instituto_nacional",
  "password": "Secreta123!",
  "email": "contacto@institutonacional.edu"
}
```

**Response 201 Created:**
```json
{
  "username": "instituto_nacional",
  "message": "Institución registrada exitosamente",
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": "ACTIVE"
}
```

#### Registrar Empleador

```http
POST /auth/register/employer
Content-Type: application/json

{
  "username": "empresa_tech",
  "password": "Secreta123!",
  "email": "rrhh@empresatech.com"
}
```

#### Registrar Egresado

```http
POST /auth/register/graduate
Content-Type: application/json

{
  "username": "juan_perez",
  "password": "Secreta123!",
  "email": "juan.perez@email.com"
}
```

#### Login

```http
POST /auth/login
Content-Type: application/json

{
  "username": "juan_perez",
  "password": "Secreta123!"
}
```

**Response 200 OK:**
```json
{
  "username": "juan_perez",
  "message": "Login exitoso",
  "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "status": "SUCCESS"
}
```

---

### 💼 Job Offers Endpoints

#### Listar Ofertas Activas (Público)

```http
GET /api/offers/active
Authorization: Bearer <token>
```

**Response 200 OK:**
```json
[
  {
    "idOffer": 1,
    "employerId": 3,
    "title": "Desarrollador Java Sr",
    "description": "Buscamos desarrollador con experiencia en Spring Boot",
    "requirements": "3+ años de experiencia, conocimientos en microservicios",
    "location": "Buenos Aires, Argentina",
    "status": "ACTIVA",
    "publicationDate": "2025-11-01",
    "closingDate": "2025-12-15",
    "createdAt": "2025-11-01T10:00:00",
    "updatedAt": "2025-11-01T10:00:00"
  }
]
```

#### Crear Oferta Laboral

```http
POST /api/offers
Authorization: Bearer <token>
Content-Type: application/json

{
  "employerId": 3,
  "title": "Desarrollador Full Stack",
  "description": "Desarrollo de aplicaciones web con React y Spring Boot",
  "requirements": "2+ años experiencia, conocimiento en JavaScript y Java",
  "location": "Remoto",
  "closingDate": "2025-12-31"
}
```

**Validaciones:**
- ✅ `closingDate` debe ser fecha futura
- ✅ `employerId` debe existir
- ✅ Estado inicial: `ACTIVA`
- ✅ `publicationDate` se setea automáticamente

**Response 201 Created:**
```json
{
  "idOffer": 5,
  "employerId": 3,
  "title": "Desarrollador Full Stack",
  "description": "Desarrollo de aplicaciones web con React y Spring Boot",
  "requirements": "2+ años experiencia, conocimiento en JavaScript y Java",
  "location": "Remoto",
  "status": "ACTIVA",
  "publicationDate": "2025-11-18",
  "closingDate": "2025-12-31",
  "createdAt": "2025-11-18T14:30:00",
  "updatedAt": "2025-11-18T14:30:00"
}
```

#### Actualizar Oferta

```http
PUT /api/offers/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Senior Full Stack Developer",
  "description": "Actualización de descripción",
  "closingDate": "2026-01-15",
  "status": "ACTIVA"
}
```

**Permisos:** `ADMIN` o dueño de la oferta

#### Cerrar Oferta

```http
PATCH /api/offers/{id}/close
Authorization: Bearer <token>
```

#### Eliminar Oferta

```http
DELETE /api/offers/{id}
Authorization: Bearer <token>
```

#### Ofertas por Empleador

```http
GET /api/offers/employer/{employerId}
Authorization: Bearer <token>
```

**Permisos:** `ADMIN` o dueño del employer

---

### 📝 Applications Endpoints

#### Crear Postulación

```http
POST /api/applications
Authorization: Bearer <token>
Content-Type: application/json

{
  "graduateId": 5,
  "offerId": 3
}
```

**Validaciones:**
- ✅ No permite postulaciones duplicadas
- ✅ Solo a ofertas con estado `ACTIVA`
- ✅ Estado inicial: `EN_PROCESO`

**Response 201 Created:**
```json
{
  "idApplication": 10,
  "graduateId": 5,
  "offerId": 3,
  "status": "EN_PROCESO",
  "applicationDate": "2025-11-18",
  "createdAt": "2025-11-18T15:00:00",
  "updatedAt": "2025-11-18T15:00:00"
}
```

#### Ver Mis Postulaciones (Egresado)

```http
GET /api/applications/graduate/{graduateId}
Authorization: Bearer <token>
```

**Permisos:** `ADMIN` o dueño del graduate

#### Ver Postulaciones a una Oferta (Empleador)

```http
GET /api/applications/offer/{offerId}
Authorization: Bearer <token>
```

**Permisos:** `ADMIN` o dueño de la oferta

#### Filtrar por Estado

```http
GET /api/applications/offer/{offerId}/status/{status}
Authorization: Bearer <token>
```

Estados válidos: `EN_PROCESO`, `ACEPTADO`, `RECHAZADO`

#### Actualizar Estado de Postulación

```http
PUT /api/applications/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "status": "ACEPTADO"
}
```

**Validaciones:**
- ❌ No se puede pasar de `RECHAZADO` a `ACEPTADO`

**Permisos:** `ADMIN` o empleador dueño de la oferta

#### Eliminar Postulación

```http
DELETE /api/applications/{id}
Authorization: Bearer <token>
```

**Permisos:** `ADMIN` o egresado dueño de la postulación

---

### 🎓 Courses Endpoints

#### Listar Cursos

```http
GET /api/courses
Authorization: Bearer <token>
```

#### Listar Cursos (Paginado)

```http
GET /api/courses/paginated?page=0&size=10
Authorization: Bearer <token>
```

#### Detalle de Curso

```http
GET /api/courses/{id}
Authorization: Bearer <token>
```

#### Crear Curso

```http
POST /api/courses
Authorization: Bearer <token>
Content-Type: application/json

{
  "idInstitution": 2,
  "title": "Desarrollo Web Full Stack",
  "description": "Curso intensivo de 6 meses",
  "modality": "ONLINE",
  "startDate": "2025-12-01",
  "endDate": "2026-05-31"
}
```

**Validaciones:**
- ✅ `idInstitution` debe existir
- ✅ `endDate` >= `startDate`

**Permisos:** `ADMIN` o institución dueña

**Response 201 Created:**
```json
{
  "idCourse": 8,
  "idInstitution": 2,
  "title": "Desarrollo Web Full Stack",
  "description": "Curso intensivo de 6 meses",
  "modality": "ONLINE",
  "startDate": "2025-12-01",
  "endDate": "2026-05-31",
  "createdAt": "2025-11-18T16:00:00",
  "updatedAt": "2025-11-18T16:00:00"
}
```

#### Actualizar Curso

```http
PUT /api/courses/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "title": "Desarrollo Web Full Stack Avanzado",
  "modality": "HIBRIDO",
  "endDate": "2026-06-30"
}
```

**Permisos:** `ADMIN` o institución dueña

#### Eliminar Curso

```http
DELETE /api/courses/{id}
Authorization: Bearer <token>
```

**Permisos:** `ADMIN` o institución dueña

---

### 🏢 Institutions, Employers, Graduates

#### Instituciones

```http
GET    /api/institutions           # Lista (ADMIN)
GET    /api/institutions/{id}      # Detalle
POST   /api/institutions           # Crear (ADMIN)
PUT    /api/institutions/{id}      # Actualizar (ADMIN o dueño)
DELETE /api/institutions/{id}      # Eliminar (ADMIN)
```

#### Empleadores

```http
GET    /api/employers              # Lista (ADMIN)
GET    /api/employers/{id}         # Detalle
POST   /api/employers              # Crear (ADMIN)
PUT    /api/employers/{id}         # Actualizar (ADMIN o dueño)
DELETE /api/employers/{id}         # Eliminar (ADMIN)
```

#### Egresados

```http
GET    /api/graduates              # Lista (ADMIN)
GET    /api/graduates/{id}         # Detalle
POST   /api/graduates              # Crear (ADMIN)
PUT    /api/graduates/{id}         # Actualizar (ADMIN o dueño)
DELETE /api/graduates/{id}         # Eliminar (ADMIN)
```

---
### Estructura del JWT

```json
{
  "sub": "juan_perez",
  "userId": 123,
  "role": "ROLE_EGRESADO",
  "iat": 1700000000,
  "exp": 1700086400,
  "iss": "conexia-api"
}
```

### Matriz de Permisos

| Recurso | ADMIN | INSTITUCION | EMPLEADOR | EGRESADO |
|---------|-------|-------------|-----------|----------|
| **Ofertas** |
| Ver activas | ✅ | ✅ | ✅ | ✅ |
| Crear | ✅ | ❌ | ✅ (propias) | ❌ |
| Editar | ✅ | ❌ | ✅ (propias) | ❌ |
| Eliminar | ✅ | ❌ | ✅ (propias) | ❌ |
| **Postulaciones** |
| Ver propias | ✅ | ❌ | ❌ | ✅ |
| Crear | ✅ | ❌ | ❌ | ✅ |
| Ver recibidas | ✅ | ❌ | ✅ (sus ofertas) | ❌ |
| Actualizar estado | ✅ | ❌ | ✅ (sus ofertas) | ❌ |
| **Cursos** |
| Ver | ✅ | ✅ | ✅ | ✅ |
| Crear | ✅ | ✅ (propios) | ❌ | ❌ |
| Editar | ✅ | ✅ (propios) | ❌ | ❌ |
| Eliminar | ✅ | ✅ (propios) | ❌ | ❌ |

---
### Buenas Prácticas de Seguridad

```java
// ✅ Bueno: Usar @PreAuthorize con SecurityService
@PreAuthorize("hasRole('ADMIN') or @securityService.isJobOfferOwner(#id)")
@PutMapping("/api/offers/{id}")
public ResponseEntity<JobOfferDTO> update(@PathVariable Long id, ...) {
    // ...
}

// ❌ Malo: Confiar solo en validación del frontend
@PutMapping("/api/offers/{id}")
public ResponseEntity<JobOfferDTO> update(@PathVariable Long id, ...) {
    // Sin validación de permisos
}
```

---

## 🧪 Testing

### Ejecutar Tests

```bash
# Todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest=*Test

# Solo tests de integración
mvn test -Dtest=*IT

# Con cobertura
mvn test jacoco:report
```
---

## 🔧 Troubleshooting

### Problema: "JWT Token has expired"

**Causa:** El token JWT expiró (por defecto 24 horas).

**Solución:**
```bash
# Hacer login nuevamente para obtener nuevo token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"tu_usuario","password":"tu_password"}'
```

### Problema: "Access Denied"

**Causa:** Usuario sin permisos para la operación.

**Solución:**
- Verificar que el rol del usuario sea correcto
- Comprobar que el recurso pertenece al usuario (en caso de recursos propios)

### Problema: "Cannot create duplicate application"

**Causa:** El egresado ya se postuló a esa oferta.

**Solución:**
```bash
# Verificar postulaciones existentes
GET /api/applications/graduate/{graduateId}
```

### Problema: Database connection failed

**Solución:**
```bash
# Verificar que MySQL esté corriendo
sudo systemctl status mysql

# Verificar credenciales en application.properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Logs Útiles

```properties
# Habilitar logs detallados en application.properties
logging.level.com.conexia=DEBUG
logging.level.org.springframework.security=TRACE
logging.level.org.hibernate.SQL=DEBUG
```

---

## 👥 Contribución

### Proceso de Contribución

1. **Fork** el repositorio
2. Crea una **rama** para tu feature:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3. **Commit** tus cambios:
   ```bash
   git commit -m "feat: agregar funcionalidad X"
   ```
4. **Push** a tu fork:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5. Abre un **Pull Request**

### Convención de Commits

Seguimos [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: nueva funcionalidad
fix: corrección de bug
docs: cambios en documentación
style: formateo, punto y coma faltante, etc.
refactor: refactorización de código
test: agregar tests
chore: actualizar dependencias, configuración
```

### Estándares de Código

```bash
# Formatear código
mvn spotless:apply

# Verificar estilo
mvn checkstyle:check

# Análisis estático
mvn spotbugs:check
```

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

---

## 📞 Contacto

- **Issues:** [GitHub Issues](https://github.com/tu-usuario/conexia-backend/issues)
- **Email:** contacto@conexia.com

---

## 🙏 Agradecimientos

- Spring Boot Team por el excelente framework
- Comunidad de desarrolladores Java
- Todos los contribuidores del proyecto

---

<p align="center">
  Hecho con ❤️ por el equipo de Conexia
</p>
