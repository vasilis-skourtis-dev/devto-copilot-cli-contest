# INSTRUCTIONS.md

## Purpose
**Why this file exists:**
This file contains **step-by-step instructions for developers** to set up their development environment, build, run, test, and deploy the application. It's the practical "how-to" guide that gets someone from "I just cloned this repo" to "I'm successfully running and modifying the application."

**What it contains:**
- Environment setup (JDK, Maven, IDE)
- How to build the project
- How to run the application locally
- How to run tests
- How to package for deployment
- Development workflow and best practices
- Troubleshooting common issues
- IDE-specific setup tips

**Audience:**
- New developers joining the team
- DevOps engineers deploying the application
- AI assistants being instructed to modify code
- Future you trying to remember how to run this project

**When to use:**
- First time setting up the project
- When something isn't working as expected
- Before making changes to ensure environment is correct
- When onboarding new team members

---

## 🔧 Environment Setup

### Prerequisites

**Required:**
- **JDK 1.8** (Java Development Kit 8)
  - Download: https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html
  - Or use OpenJDK 8
- **Maven 3.x**
  - Download: https://maven.apache.org/download.cgi
  - Or use your OS package manager

**Verify Installation:**
```bash
java -version
# Should show: java version "1.8.0_xxx"

mvn -version
# Should show: Apache Maven 3.x.x
```

**Optional (but recommended):**
- **IDE:** IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Git:** For version control

---

## 📦 Building the Project

### First-Time Setup

1. **Navigate to project directory:**
   ```bash
   cd COPILOT-EXERCISES/valentine-love-heart-cards
   ```

2. **Clean and build:**
   ```bash
   mvn clean package
   ```
   - This downloads dependencies (first run may take a few minutes)
   - Compiles Java source code
   - Packages into a JAR file
   - Output: `target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar`

3. **Verify build success:**
   Look for `BUILD SUCCESS` in the Maven output

---

## 🚀 Running the Application

### Option 1: Using Maven (Development)

```bash
mvn spring-boot:run
```

- **Advantages:** 
  - Quick restarts
  - No need to rebuild JAR for code changes (in some cases)
- **Use when:** Actively developing

### Option 2: Using JAR (Production-like)

```bash
# Build first
mvn clean package

# Run the JAR
java -jar target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar
```

- **Advantages:**
  - Tests actual deployment artifact
  - Closer to production environment
- **Use when:** Testing final build or deploying

### Access the Application

Once running, open your browser to:
```
http://localhost:8080
```

**To stop the application:**
- Press `Ctrl+C` in the terminal

---

## 🧪 Testing

_[Testing instructions to be added when tests are created]_

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=ClassNameTest
```

---

## 🔍 Development Workflow

### Making Changes

1. **Modify code** in your IDE or text editor
2. **For Java changes:**
   - Stop the application (`Ctrl+C`)
   - Rebuild and restart:
     ```bash
     mvn clean spring-boot:run
     ```
3. **For template/static file changes:**
   - If using `mvn spring-boot:run`, changes may be picked up automatically
   - Otherwise, restart the application

### Best Practices

- **Commit often:** Make small, logical commits
- **Test locally:** Verify changes work before committing
- **Check logs:** Watch the console output for errors
- **Read error messages:** Spring Boot provides helpful error details

---

## 🎨 IDE Setup

### IntelliJ IDEA

1. **Import Project:**
   - File → Open → Select `valentine-love-heart-cards` folder
   - Choose "Maven" project type
   - Wait for dependencies to download

2. **Configure JDK:**
   - File → Project Structure → Project SDK → Select JDK 1.8

3. **Run Configuration:**
   - Right-click `CardsApplication.java` → Run
   - Or create a Spring Boot run configuration

### VS Code

1. **Install Extensions:**
   - Java Extension Pack
   - Spring Boot Extension Pack

2. **Open Folder:**
   - File → Open Folder → Select `valentine-love-heart-cards`

3. **Run:**
   - Use Debug panel → "Spring Boot App" configuration

---

## 📁 Project Directory Structure

```
valentine-love-heart-cards/
├── src/
│   ├── main/
│   │   ├── java/              ← Java source code
│   │   └── resources/
│   │       ├── application.properties  ← Configuration
│   │       ├── static/        ← CSS, JS, images
│   │       └── templates/     ← Thymeleaf HTML
│   └── test/
│       └── java/              ← Test code
├── target/                    ← Build output (generated)
├── pom.xml                    ← Maven configuration
└── README.md                  ← Project overview
```

---

## 🐛 Troubleshooting

### Issue: "BUILD FAILURE - cannot find symbol"

**Cause:** Compilation error in Java code

**Solution:**
1. Check the error message for file and line number
2. Fix the Java syntax or import issue
3. Rebuild: `mvn clean package`

---

### Issue: "Port 8080 already in use"

**Cause:** Another application is using port 8080

**Solution:**
1. Kill the other application using port 8080
2. Or change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

---

### Issue: "Cannot access http://localhost:8080"

**Cause:** Application not running or error during startup

**Solution:**
1. Check terminal for error messages
2. Verify "Started CardsApplication" message appeared
3. Check logs for stack traces
4. Verify correct port in URL

---

### Issue: Maven dependencies not downloading

**Cause:** Network issues or Maven repository problems

**Solution:**
```bash
# Clear local Maven cache
rm -rf ~/.m2/repository

# Force update
mvn clean install -U
```

---

## 📝 Configuration

### Application Properties

Located at: `src/main/resources/application.properties`

**Key settings:**
```properties
# Server Configuration
server.port=8080
server.servlet.session.timeout=75m

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging
logging.level.org.springframework.web=DEBUG
logging.level.org.thymeleaf=DEBUG
```

**To override during development:**
```bash
# Change port
java -jar target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar --server.port=9090
```

---

## 🚢 Deployment

_[Deployment instructions to be added]_

Preliminary steps:
1. Build production JAR: `mvn clean package`
2. Copy JAR to server: `target/valentine-love-heart-cards-0.0.1-SNAPSHOT.jar`
3. Run on server: `java -jar valentine-love-heart-cards-0.0.1-SNAPSHOT.jar`

---

## 📞 Getting Help

- Check the console output for error messages
- Review Spring Boot documentation: https://docs.spring.io/spring-boot/docs/2.7.18/reference/html/
- Check other project docs: README.md, DESIGN_SPEC.md, PLAN.md

---

_Instructions updated: 2026-02-16_
