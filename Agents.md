# Agentic AI Programming in Action

## Overview
This document captures the architectural and technical decisions for our Spring Boot application development approach. The focus is on simplicity, maintainability, and security through clean design.

---

## Technology Stack

### Core Technologies
- **Java**: 1.8
- **Spring Boot**: 2.7.18
- **Templating**: Thymeleaf
- **Frontend**: HTML5 + CSS + Vanilla JavaScript (minimal)
- **Build Tool**: Maven
- **Deployment**: Embedded Tomcat (java -jar style)

### Styling
- **Bootstrap CSS** - Optional, only if needed
- **Preference**: Avoid external CSS frameworks when possible

### Explicitly Avoided Technologies
We consciously avoid the following to maintain simplicity:
- ❌ TypeScript
- ❌ Angular (all versions)
- ❌ React
- ❌ Vue.js
- ❌ Any JavaScript frameworks
- ❌ npm/Node.js build tooling
- ❌ Complex build pipelines

### Rationale
- **Legacy & Compatibility**: Working with a proven, stable stack
- **Reusability**: Components designed for long-term reuse
- **Simplicity**: No unnecessary technology complications
- **Troubleshooting**: Easy to debug and maintain
- **Fast Iteration**: Quick access from idea to result
- **Small User Base Initially**: Start simple, validate adoption, then scale

---

## Build & Deployment Model

### Simple Workflow
```bash
# Build
mvn clean package

# Run
java -jar myapp.jar

# Access
http://localhost:8080
```

### Characteristics
- **No npm required** - Pure Java/Maven ecosystem
- **Single JAR deployment** - Embedded Tomcat, self-contained
- **Fast startup** - Quick feedback loop
- **Simple operations** - Traditional java -jar execution

---

## Modular Architecture

### Multi-Module Project Structure
All applications follow a strict **5-layer architecture** based on Model-View-Controller and multi-layer design principles, organized into 3 worlds: **Persistence**, **Domain**, and **Application**.

### Layer Hierarchy

#### **Layer 01: Models**
Data structures and transformations
- `[application-name]-model-persistence-records` - Database entities, JPA records
- `[application-name]-model-application-dtos` - Data Transfer Objects for API/UI
- `[application-name]-model-converters` - Mappers between persistence records and DTOs

#### **Layer 02: Persistence**
Data access layer
- `[application-name]-persistence-accessors` - DAOs, Repositories, database accessors

#### **Layer 03: Domain**
Business logic layer
- `[application-name]-domain-services` - Business services, core application logic

#### **Layer 04: Application**
Application interface layer
- `[application-name]-application-services-api` - RESTful web services, APIs
- `[application-name]-application-ui` - Controllers, UI logic, Thymeleaf templates

#### **Layer 05: Web**
Application assembly and configuration
- `[application-name]-application-web` - Web application configuration, Spring Boot main class, resources

### Dependency Rules

**Strict Downward Dependencies Only:**
- ✅ Higher layers can depend on lower layers (Layer 05 → Layer 04 → Layer 03 → Layer 02 → Layer 01)
- ❌ Lower layers **never** depend on higher layers (Layer 01 does NOT know about Layer 04)
- ✅ Same-level dependencies are allowed when logical (e.g., within Layer 01)

**Example:**
- Layer 05 (`application-web`) depends on Layer 04 (`application-ui`, `application-services-api`)
- Layer 04 depends on Layer 03 (`domain-services`)
- Layer 03 depends on Layer 02 (`persistence-accessors`)
- Layer 02 depends on Layer 01 (`model-persistence-records`, `model-converters`)
- Layer 01 has minimal external dependencies

---

## Testing Strategy

### Separate Test Modules
**Tests are NOT in the same modules as production code.**

#### Rationale:
- **Security by Design** - No test code in production deployments
- **Performance** - Smaller deployment artifacts
- **Defensive Programming** - Prevent test dependencies (e.g., JUnit) from being packaged in WAR/JAR
- **Clean Separation** - Clear boundary between production and test code

#### Test Module Structure:
```
qa-application-tests/
  ├── [module-name]-unit-tests/
  └── [module-name]-integration-tests/
```

#### Example (from current workspace):
```
qa-application-tests/
  ├── common-elements-unit-tests/
  ├── common-elements-integration-tests/
  ├── common-logging-elements-unit-tests/
  └── common-logging-elements-integration-tests/
```

---

## Design Principles

### Core Values
1. **Simplicity** - Choose the simplest solution that works
2. **Clarity** - Code structure should be self-documenting
3. **Extensibility** - Design for future growth without over-engineering
4. **Minimal Dependencies** - Only include what's necessary
5. **Security by Design** - Secure by default, not as an afterthought
6. **Performance Awareness** - Efficient, but not prematurely optimized

### Architectural Guidelines
- **Separation of Concerns** - Each layer has a clear responsibility
- **Encapsulation** - Internal details hidden within modules
- **Reusability** - Common components shared across applications
- **Testability** - Design for easy unit and integration testing
- **Maintainability** - Code should be easy to understand and modify

---

## Project Organization

### Multi-Module Maven Structure
```
parent-pom.xml
├── common-basic-elements/           (Reusable foundational modules)
│   ├── common-model-persistence-records/
│   ├── common-model-application-dtos/
│   ├── common-model-converters/
│   ├── common-persistence-accessors/
│   ├── common-domain-services/
│   ├── common-application-services/
│   ├── common-application-ui/
│   └── common-application-web/
├── common-logging-elements/         (Reusable logging modules)
│   └── [similar structure]
├── [application-name]/              (Specific application)
│   ├── [app]-model-persistence-records/
│   ├── [app]-model-application-dtos/
│   ├── [app]-model-converters/
│   ├── [app]-persistence-accessors/
│   ├── [app]-domain-services/
│   ├── [app]-application-services-api/
│   ├── [app]-application-ui/
│   └── [app]-application-web/
└── qa-application-tests/            (All tests separated)
    ├── [module]-unit-tests/
    └── [module]-integration-tests/
```

---

## Benefits of This Approach

### Development Benefits
- **Fast Feedback Loop** - Build and run in seconds
- **Easy Debugging** - Simple stack, clear error messages
- **Low Learning Curve** - Standard Java/Spring patterns
- **IDE Friendly** - Excellent tooling support in all major IDEs

### Operational Benefits
- **Simple Deployment** - Single JAR file
- **Low Resource Footprint** - Minimal dependencies
- **Easy Monitoring** - Standard Java tooling works
- **Clear Upgrade Path** - Modular structure supports incremental changes

### Business Benefits
- **Rapid Prototyping** - Quick from idea to working prototype
- **Cost Effective** - No complex infrastructure needed
- **Scalable Foundation** - Can grow as user base increases
- **Risk Mitigation** - Proven technologies, stable stack

---

## Next Steps

### To Be Defined
1. Specific application to build
2. Domain model and business requirements
3. Database choice and schema design
4. API contracts and endpoints
5. UI/UX requirements and mockups

### Project Structuring Decisions
- Naming conventions for specific applications
- Shared vs. application-specific modules
- Dependency management strategy
- Configuration management approach
- Deployment target environments

---

## Document History
- **2026-02-13**: Initial creation - Stack and architecture definition
