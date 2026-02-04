# DevTo Copilot CLI Contest

This monorepo contains four Spring Boot multi-module applications developed for the DevTo Copilot CLI Contest. All applications follow clean architecture principles with strict separation of concerns.

## Technology Stack

- **Framework:** Spring Boot 2.7.18
- **Build Tool:** Maven 3.x
- **Java Version:** 8
- **Architecture:** Multi-module Maven projects
- **ORM:** Spring Data JPA
- **Messaging:** Apache Kafka
- **Testing:** JUnit 5, Mockito, Rest-Assured

## Projects

### 1. Dashboard UI
A comprehensive monitoring and analytics dashboard application.

**Location:** [`dashboard-ui/`](dashboard-ui/)

**Description:** Interactive dashboard for visualizing metrics, monitoring systems, and analyzing data in real-time.

**Port:** 8081

[View Dashboard README](dashboard-ui/README.md)

### 2. Logs Aggregator
A centralized log aggregation and analysis system.

**Location:** [`logs-aggregator/`](logs-aggregator/)

**Description:** Collects, processes, and analyzes logs from multiple sources, providing insights and searchable log storage.

**Port:** 8082

[View Logs Aggregator README](logs-aggregator/README.md)

### 3. Postcards
A creative digital postcard generation and sharing platform.

**Location:** [`postcards/`](postcards/)

**Description:** Create, customize, and send beautiful digital postcards with ease.

**Port:** 8080

[View Postcards README](postcards/README.md)

### 4. Documentation Builder App
An automated documentation generation and building system.

**Location:** [`documentation-builder-app/`](documentation-builder-app/)

**Description:** Automatically generates, builds, and publishes technical documentation from code, markdown, and API specifications.

**Port:** 8083

[View Documentation Builder README](documentation-builder-app/README.md)

## Repository Structure

```
.
├── dashboard-ui/               # Dashboard application (15 modules)
├── logs-aggregator/            # Log aggregation system (15 modules)
├── postcards/                  # Postcard generation platform (15 modules)
├── documentation-builder-app/  # Documentation automation tool (15 modules)
├── temp-workspace/             # Temporary workspace (gitignored)
├── COPILOT_HISTORY            # AI collaboration history log
├── PROJECT_STRUCTURE.md       # Detailed architecture guide
├── .gitignore                 # Git ignore rules
└── README.md                  # This file
```

## Module Architecture

Each project follows a consistent 15-module structure:

**Model Layer (6 modules)**
- `*-model-universal` - Shared constants and utilities
- `*-model-domain-objects` - Core business models
- `*-model-business-rules` - Validation and business rules
- `*-model-persistence-records` - JPA entities
- `*-model-application-interface-dtos` - API DTOs
- `*-model-converters` - MapStruct converters

**Domain Layer (2 modules)**
- `*-domain-repositories` - Data access layer
- `*-domain-services` - Business logic

**Application Layer (3 modules)**
- `*-application-services-apis` - Service contracts
- `*-services-producer` - Message producers
- `*-services-consumer` - Message consumers

**Presentation Layer (2 modules)**
- `*-application-ui` - Frontend application
- `*-application-web` - REST API (runnable)

**QA Layer (2 modules)**
- `*-qa-unit-tests` - Unit tests
- `*-qa-integration-tests` - Integration tests

📖 **[Read the complete architecture guide](PROJECT_STRUCTURE.md)**

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Maven 3.6+
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code with Java extensions)

### Building a Project

```bash
# Navigate to a project
cd postcards

# Build all modules
mvn clean install

# Run the application
cd postcards-application-web
mvn spring-boot:run
```

### Running Tests

```bash
# Unit tests
cd postcards/postcards-qa-unit-tests
mvn test

# Integration tests
cd postcards/postcards-qa-integration-tests
mvn verify
```

### Accessing Applications

Once running, applications are available at:
- Dashboard UI: http://localhost:8081/api
- Logs Aggregator: http://localhost:8082/api
- Postcards: http://localhost:8080/api
- Documentation Builder: http://localhost:8083/api

H2 Console (Development): `http://localhost:{port}/api/h2-console`

## Development

This is a monorepo structure where each project can be developed independently. Each project follows the same architectural patterns for consistency.

### Common Maven Commands

```bash
mvn clean install          # Build all modules
mvn test                   # Run tests
mvn spring-boot:run        # Run application (in *-application-web)
mvn dependency:tree        # View dependencies
```

### AI-Assisted Development

This project heavily utilizes GitHub Copilot for development. See [COPILOT_HISTORY](COPILOT_HISTORY) for detailed collaboration logs.

Each project maintains its own `COPILOT_HISTORY.md` file documenting AI-assisted development activities.

## License

MIT

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Documentation

- [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) - Complete architecture and development guide
- [COPILOT_HISTORY](COPILOT_HISTORY) - AI collaboration history
