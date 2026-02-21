# AI-Powered E-Commerce Order & Support Automation - Test Suite

An enterprise-grade test automation framework combining **UI Testing**, **API Testing**, and **AI-powered performance monitoring** for e-commerce applications.

## 🎯 Features

### Core Testing Framework
- **Selenium WebDriver** - Cross-browser automation (Chrome, Firefox, Edge)
- **REST Assured** - API testing with schema validation
- **TestNG** - Parallel test execution
- **Allure Reports** - Comprehensive test reporting

### 🤖 4 AI Layers

1. **Smart Assertion Generation** - Automatically generates comprehensive assertions by:
   - Learning response schemas from sample responses
   - Handling dynamic values (timestamps, IDs, tokens)
   - Detecting data type violations
   - Validating nested structures automatically

2. **Schema Learning & Drift Detection** - Intelligent API contract testing:
   - Auto-learns endpoint response schemas
   - Detects schema drift (new/removed/changed fields)
   - Alerts on breaking API changes
   - Prevents silent API failures

3. **Performance Anomaly Detection** - Statistical analysis using machine learning:
   - Establishes baseline performance metrics
   - Detects response time anomalies (Z-score analysis)
   - Identifies performance degradation trends
   - Alerts on sudden performance spikes
   - Tracks response time percentiles (p50, p95, p99)

4. **Baseline Monitoring & Thresholds** - Real-time performance tracking:
   - Establishes dynamic performance baselines
   - Monitors against thresholds
   - Generates performance alerts and reports
   - CSV export for further analysis

### Additional Features
- **Visual AI Testing** - Applitools Eyes for visual regression
- **Smart Wait Management** - AI-powered intelligent wait times
- **Self-Healing Locators** - Healenium for resilient UI tests
- **Docker Support** - PostgreSQL + Healenium server setup

## 📋 Prerequisites

- **Java 8+**
- **Maven 3.6+**
- **Docker & Docker Compose** (for Healenium server)
- **Chrome/Firefox/Edge browser**

## 🚀 Quick Start

### 1. Environment Setup

```bash
# Clone repository
cd ui-automation

# Start PostgreSQL & Healenium server
docker-compose up -d

# Verify services
docker-compose ps
```

### CI/CD - GitHub Actions
This project includes an automated **GitHub Actions** workflow that:
- ✅ **Builds** the Maven project on every push and pull request
- ✅ **Runs TestNG tests** automatically
- ✅ **Generates Allure reports**
- ✅ **Uploads test artifacts** for review

**Workflow Location**: `.github/workflows/build.yml`

**Triggered on:**
- Push to `master` or `develop` branches
- Pull requests to `master` or `develop` branches

**View Results:**
1. Go to **Actions** tab in GitHub repository
2. Select a workflow run
3. Download artifacts (Allure results, Surefire reports)

### 2. Configure Test Data

Edit configuration files:
- `src/test/resources/Config.properties` - Base URLs, credentials
- `src/test/resources/api-testdata.properties` - API test data
- `src/test/resources/ai-config.properties` - AI feature settings

### 3. Run Tests

```bash
# Run all tests
mvn clean test

# Run API tests only
mvn clean test -Dsuites=testng-api.xml

# Run UI tests only
mvn clean test -Dsuites=testng.xml

# Enable performance anomaly alerts
mvn clean test -Dapi.fail.on.anomaly=true

# Generate Allure report
mvn allure:serve
```

## 📁 Project Structure

```
.github/
└── workflows/
    └── build.yml                        # GitHub Actions CI/CD workflow
src/
├── main/
│   └── java/com/project/
│       ├── api/
│       │   ├── APIBase.java              # API test base class
│       │   ├── endpoints/                # API endpoint clients
│       │   └── performance/
│       │       └── PerformanceAnomalyDetector.java
│       ├── ui/
│       │   ├── pages/                    # Page object models
│       │   └── Base/BaseTest.java        # UI test base class
│       └── utils/
│           ├── TestDataGenerator.java
│           └── APITestDataConfig.java
└── test/
    ├── java/com/project/ai/
    │   ├── api/APITestWithAILayers.java  # API test suite
    │   └── ui/UITestWithAILayers.java    # UI test suite
    └── resources/
        ├── testng.xml                    # UI test config
        ├── testng-api.xml                # API test config
        ├── Config.properties
        ├── ai-config.properties
        ├── log4j2.xml
        └── fixtures/                     # Test data files

target/
├── allure-results/                       # Allure report data
├── performance-metrics/                  # CSV performance data
└── api-schemas/                          # JSON schema validators
```

## 🔍 Test Suites

### API Testing
- **Authentication Tests**: Login, registration, account verification
- **Product Management**: List, search, filter products
- **Brand Management**: CRUD operations on brands
- **Order Management**: Order creation and processing
- **Schema Validation**: JSON schema validation for all responses
- **Performance Tracking**: Automatic response time monitoring

**Test File**: `src/test/java/com/project/ai/api/APITestWithAILayers.java`

### UI Testing
- **E-Commerce Workflows**: Product browsing, cart, checkout
- **User Management**: Registration, login, profile updates
- **Visual Testing**: AI-powered visual regression with Applitools
- **Self-Healing**: Locator recovery with Healenium

**Test File**: `src/test/java/com/project/ai/ui/UITestWithAILayers.java`

## 📊 Performance Monitoring

### PerformanceAnomalyDetector

Automatically tracks API performance using statistical analysis:

```java
// Automatic tracking in tests
Response response = PerformanceAnomalyDetector.track("endpoint-name", 
    () -> restAssured.get("/api/endpoint"));

// Print performance report
PerformanceAnomalyDetector.printPerformanceReport();
```

### Metrics Tracked
- **Mean Response Time**: Average response time
- **Percentiles**: 50th, 95th, 99th percentiles
- **Anomaly Detection**: Z-score analysis (3σ threshold)
- **Degradation Alerts**: 50% slower than baseline
- **Trend Analysis**: Consistent performance changes

### Output
- **Console Logs**: Real-time performance alerts
- **CSV Files**: `target/performance-metrics/{endpoint}_metrics.csv`
- **Allure Reports**: Integration with test reports

## 📈 Reporting

### Allure Reports

```bash
# Generate and serve Allure report
mvn allure:serve

# Or manual generation
.allure/allure-2.20.1/bin/allure generate target/allure-results --clean -o target/allure-report
.allure/allure-2.20.1/bin/allure open target/allure-report
```

Report includes:
- Test execution summary
- Performance metrics
- Passed/failed test breakdown
- Request/response details
- Anomaly alerts

### Performance Metrics

Access CSV performance data:
```
target/performance-metrics/{endpoint-name}_metrics.csv
```

Contains: Timestamp, ResponseTime, StatusCode

## 🛠️ Key Technologies

| Component | Version | Purpose |
|-----------|---------|---------|
| Selenium | 4.11.0 | Browser automation |
| TestNG | 7.4.0 | Test framework |
| REST Assured | 5.3.2 | API testing |
| Healenium | 3.4.5 | Self-healing locators |
| Applitools Eyes | 5.71.0 | Visual testing |
| Allure | 2.24.0 | Test reporting |
| Apache Commons Math | 3.6.1 | Statistical analysis |

## 📝 Configuration Files

### Config.properties
```properties
base.url=http://localhost:8080
api.base.url=http://localhost:8080/api
browser=chrome
timeout=10
implicit.wait=5
```

### ai-config.properties
```properties
# AI/ML feature toggles
enable.visual.testing=true
enable.self.healing=true
enable.performance.monitoring=true
```

## 🐳 Docker Services

### PostgreSQL
- Container: `healenium-postgres`
- Port: `5432`
- Database: `healenium`
- Init script: `postgres-init.sql`

### Healenium Server
- Container: `healenium-server`
- Port: `9090`
- Health check: `http://localhost:9090/actuator/health`

### Commands

```bash
# Start services
docker-compose up -d

# Stop services
docker-compose down

# View logs
docker-compose logs -f healenium-server

# Restart services
docker-compose restart
```

## 🔧 Maven Build

```bash
# Clean and compile
mvn clean compile

# Run tests and generate reports
mvn clean test allure:report

# Skip tests during build
mvn clean compile -DskipTests

# Run with specific properties
mvn test -Dbrowser=firefox -Denvironment=staging
```

## 🚨 Troubleshooting

### Docker Connection Issues
```bash
# Check container status
docker ps -a

# View container logs
docker logs healenium-server

# Rebuild containers
docker-compose down -v
docker-compose up -d --build
```

### Test Failures
1. Check `logs/` directory for detailed logs
2. Review `target/allure-results/` for test details
3. Verify configuration files in `src/test/resources/`
4. Check Healenium server status: `http://localhost:9090`

### Performance Issues
- Check `target/performance-metrics/` CSV files
- Review console logs for anomaly warnings
- Enable verbose logging in `log4j2.xml`

## 📞 Support & Contribution

For issues or contributions:
1. Check logs in `logs/` directory
2. Review test reports in `target/allure-results/`
3. Enable debug mode in configuration
4. Contact project team

---

**Last Updated**: February 22, 2026
**Framework Version**: 1.0-SNAPSHOT
**CI/CD**: GitHub Actions Enabled
