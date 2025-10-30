# Flipkart Automation Framework

Enterprise-grade automation framework for testing Flipkart UI and API with Selenium, TestNG, and Rest-Assured.

## Features
- Page Object Model (POM) with PageFactory
- Parallel test execution
- Screenshot and reporting with Extent Reports
- Retry mechanism for flaky tests
- Multi-browser support (Chrome, Firefox, Edge)
- Selenium Grid compatibility
- Docker support
- Jenkins and Azure DevOps CI/CD integration
- API testing with Rest-Assured
- Comprehensive logging with Log4j2

## Prerequisites
- Java 11+
- Maven 3.6+
- Docker and Docker Compose (optional)

## Setup

### 1. Clone the repository
```bash
git clone <repository-url>
cd flipkart-automation-framework
```

### 2. Build the project
```bash
mvn clean install
```

### 3. Run tests
```bash
# Default (Chrome, QA environment)
mvn clean test

# Specific browser
mvn clean test -Dbrowser=firefox

# Specific environment
mvn clean test -Denvironment=staging

# Headless mode
mvn clean test -Dheadless=true

# Custom suite
mvn clean test -DsuiteXmlFile=src/test/resources/testng/smoke-suite.xml

# Parallel execution
mvn clean test -DthreadCount=5
```

## Docker Execution

```bash
# Start Selenium Grid
docker-compose up -d selenium-hub chrome-node firefox-node

# Run tests
docker-compose up test-execution

# Stop all services
docker-compose down
```

## Project Structure

```
flipkart-automation-framework/
├── src/
│   ├── main/java/com/flipkart/automation/
│   │   ├── base/           - Base classes for tests and pages
│   │   ├── config/         - Configuration management
│   │   ├── constants/      - Framework constants
│   │   ├── drivers/        - Browser factory
│   │   ├── listeners/      - TestNG listeners
│   │   ├── pages/          - Page object models
│   │   ├── reports/        - Reporting utilities
│   │   ├── utils/          - Utility classes
│   │   └── api/            - API testing components
│   └── test/
│       ├── java/           - Test classes
│       └── resources/      - Configuration and test data
├── pom.xml                 - Maven configuration
├── Dockerfile              - Docker image configuration
├── docker-compose.yml      - Docker Compose configuration
└── Jenkinsfile             - Jenkins pipeline configuration
```

## Reporting

After test execution, view the Extent Report:
```bash
open test-output/extent-reports/ExtentReport_*.html
```

## CI/CD Integration

### Jenkins
Configure parameters and trigger the Jenkins job with:
- Browser: chrome/firefox/edge
- Environment: qa/staging/prod
- Suite: testng.xml/smoke-suite.xml/regression-suite.xml

### Azure DevOps
Pipeline configured in `azure-pipelines.yml`. Customize parameters as needed.

## Contributing
Follow the page object model pattern for UI tests and use appropriate assertions.

## Support
For issues or questions, contact the QA team.
