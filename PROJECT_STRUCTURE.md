# Project Structure Guide

## Architecture Overview

This monorepo contains four Spring Boot multi-module applications, each following a clean architecture pattern with strict separation of concerns.

## Module Organization

Each application follows this layered structure:

### Layer 1: Model Layer
Models and data structures representing different aspects of the domain.

- **`*-model-universal`** - Universal constants, enums, and shared utilities
- **`*-model-domain-objects`** - Core business domain models (Plain Java Objects)
- **`*-model-business-rules`** - Business validation rules and constraints
- **`*-model-persistence-records`** - JPA entities for database persistence
- **`*-model-application-interface-dtos`** - Data Transfer Objects for API/UI
- **`*-model-converters`** - MapStruct converters between model types

### Layer 2: Domain Layer
Core business logic and data access.

- **`*-domain-repositories`** - Spring Data repositories for data access
- **`*-domain-services`** - Business logic and domain services

### Layer 3: Application Layer
Application services and async processing.

- **`*-application-services-apis`** - Service interfaces and contracts
- **`*-services-producer`** - Message producers (Kafka/RabbitMQ)
- **`*-services-consumer`** - Message consumers (Kafka/RabbitMQ)

### Layer 4: Presentation Layer
User interfaces and web services.

- **`*-application-ui`** - Frontend UI (React/Angular/Vue)
- **`*-application-web`** - Spring Boot REST API and main application

### Layer 5: Quality Assurance
Testing modules.

- **`*-qa-unit-tests`** - Unit tests for all modules
- **`*-qa-integration-tests`** - Integration tests for the complete application

## Dependency Flow

```
┌─────────────────────────────────────┐
│     application-web (runnable)      │
│   application-ui (frontend build)   │
└───────────────┬─────────────────────┘
                │
        ┌───────┴────────┐
        ▼                ▼
  ┌──────────┐    ┌──────────────┐
  │ services │◄───│  services    │
  │ producer │    │  consumer    │
  └────┬─────┘    └──────┬───────┘
       │                 │
       └────────┬────────┘
                ▼
        ┌───────────────┐
        │ domain        │
        │ services      │
        └───────┬───────┘
                │
        ┌───────┴───────┐
        ▼               ▼
  ┌──────────┐   ┌──────────┐
  │  domain  │   │  model   │
  │  repos   │   │converter │
  └────┬─────┘   └────┬─────┘
       │              │
       └──────┬───────┘
              ▼
      ┌───────────────┐
      │ Model Layers  │
      │  (DTOs, JPA,  │
      │ Domain, etc.) │
      └───────────────┘
```

## Technology Stack

- **Framework:** Spring Boot 2.7.18
- **Build Tool:** Maven 3.x
- **Java Version:** 8
- **ORM:** Spring Data JPA / Hibernate
- **Database:** H2 (dev), PostgreSQL (prod)
- **Messaging:** Apache Kafka / Spring Kafka
- **Mapping:** MapStruct 1.5.5
- **Utilities:** Lombok 1.18.30
- **Testing:** JUnit 5, Mockito, Rest-Assured

## Building the Projects

### Build All Modules
```bash
cd postcards
mvn clean install
```

### Build Specific Module
```bash
cd postcards/postcards-domain-services
mvn clean install
```

### Run Application
```bash
cd postcards/postcards-application-web
mvn spring-boot:run
```

### Run Tests
```bash
# Unit tests
cd postcards/postcards-qa-unit-tests
mvn test

# Integration tests
cd postcards/postcards-qa-integration-tests
mvn verify
```

## Module Descriptions

### Model Modules

#### model-universal
- Shared enums, constants, exceptions
- Utility classes used across all layers
- No external dependencies except Lombok

#### model-domain-objects
- Rich domain models with business logic
- Domain events
- Value objects
- No framework dependencies (pure Java)

#### model-business-rules
- Validation annotations and validators
- Business rule engines
- Domain constraints
- Uses Spring Validation

#### model-persistence-records
- JPA entities with Hibernate annotations
- Database table mappings
- Entity relationships (@OneToMany, @ManyToOne, etc.)
- Uses Spring Data JPA

#### model-application-interface-dtos
- Request/Response DTOs for REST APIs
- View models for UI
- API contracts
- Jackson annotations for JSON serialization

#### model-converters
- MapStruct mapper interfaces
- Bidirectional conversions between:
  - Domain Objects ↔ JPA Entities
  - Domain Objects ↔ DTOs
  - JPA Entities ↔ DTOs

### Domain Modules

#### domain-repositories
- Spring Data JPA repositories
- Custom query methods
- Native queries and Specifications
- Transaction boundaries

#### domain-services
- Core business logic
- Domain operations
- Service orchestration
- Transaction management

### Application Modules

#### application-services-apis
- Service interfaces (API contracts)
- Service request/response models
- Async operation definitions

#### services-producer
- Kafka message producers
- Event publishers
- Async job submitters

#### services-consumer
- Kafka message listeners
- Event handlers
- Async job processors

### Presentation Modules

#### application-ui
- React/Angular/Vue frontend
- Built using frontend-maven-plugin
- Bundled with application-web

#### application-web
- **Main runnable application**
- REST Controllers
- Security configuration
- Exception handlers
- Spring Boot auto-configuration
- Embedded Tomcat server

### QA Modules

#### qa-unit-tests
- Unit tests for services, converters, validators
- Mocked dependencies
- Fast execution

#### qa-integration-tests
- End-to-end API tests
- Database integration tests
- Full Spring context tests
- Rest-Assured for API testing

## Development Guidelines

### Adding a New Feature

1. **Define Domain Model** in `*-model-domain-objects`
2. **Add Business Rules** in `*-model-business-rules`
3. **Create JPA Entity** in `*-model-persistence-records`
4. **Define DTOs** in `*-model-application-interface-dtos`
5. **Add Converters** in `*-model-converters`
6. **Create Repository** in `*-domain-repositories`
7. **Implement Service** in `*-domain-services`
8. **Add REST Controller** in `*-application-web`
9. **Write Tests** in `*-qa-unit-tests` and `*-qa-integration-tests`

### Module Dependency Rules

- Models depend only on other models
- Domain modules depend on models
- Application modules depend on domain + models
- Web module depends on all layers
- Test modules can depend on anything
- No circular dependencies allowed

### Best Practices

1. **Keep models thin** - Use services for business logic
2. **Use DTOs for APIs** - Never expose entities directly
3. **Validate at boundaries** - API layer and domain layer
4. **Test at all layers** - Unit + Integration tests
5. **Use transactions wisely** - At service layer, not repository
6. **Async when possible** - Use producers/consumers for heavy operations

## Common Maven Commands

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package (creates JAR)
mvn package

# Install to local repo
mvn install

# Skip tests
mvn install -DskipTests

# Run specific test
mvn test -Dtest=MyTest

# Dependency tree
mvn dependency:tree

# Update dependencies
mvn versions:display-dependency-updates
```

## Project Ports

Default application ports:
- **Dashboard UI**: 8081
- **Logs Aggregator**: 8082
- **Postcards**: 8080
- **Documentation Builder**: 8083

## Database Access

H2 Console (Development):
- URL: `http://localhost:{port}/api/h2-console`
- JDBC URL: `jdbc:h2:mem:{appname}db`
- Username: `sa`
- Password: (empty)

## Next Steps

1. Implement domain models for each application
2. Set up database schemas
3. Create REST API endpoints
4. Build frontend UIs
5. Add message-driven features
6. Configure production databases
7. Add security (Spring Security)
8. Implement logging and monitoring
9. Create Docker containers
10. Set up CI/CD pipelines
