🧪 AI-Powered E-Commerce Order & Support Automation – Test Suite

A robust automation framework that integrates UI Testing, API Testing, AI-driven validation, and performance monitoring for modern e-commerce systems.

🎯 Core Stack

Selenium WebDriver – Cross-browser UI automation (Chrome, Firefox, Edge)

REST Assured – API testing with schema validation

TestNG – Parallel execution

Allure Reports – Detailed reporting

Docker + PostgreSQL + Healenium – Self-healing UI support

🤖 AI Capabilities (4 Intelligent Layers)

Smart Assertion Generation

Auto-validates API responses

Handles dynamic values (IDs, timestamps)

Validates nested JSON & data types

Schema Learning & Drift Detection

Learns API contracts automatically

Detects breaking changes in responses

Prevents silent API failures

Performance Anomaly Detection

Tracks p50, p95, p99 response times

Uses Z-score (3σ) anomaly detection

Identifies degradation trends & spikes

Baseline Monitoring

Establishes dynamic performance baselines

Alerts when thresholds are exceeded

Exports CSV metrics for analysis

✨ Additional Features

Applitools Eyes – Visual AI regression testing

Healenium – Self-healing locators

Smart Wait Management – Intelligent synchronization

GitHub Actions CI/CD – Automated build, test & Allure report generation

🚀 Quick Start
# Start Docker services
docker-compose up -d

# Run all tests
mvn clean test

# Run API only
mvn clean test -Dsuites=testng-api.xml

# Run UI only
mvn clean test -Dsuites=testng.xml

# Generate Allure report
mvn allure:serve
📊 Performance Monitoring

Automatically tracks:

Mean response time

Percentiles (p50, p95, p99)

Anomaly detection (3σ rule)

Degradation alerts

Output available in:

Console logs

Allure Reports

target/performance-metrics/*.csv

🛠 Key Technologies
Tool	Purpose
Selenium 4	UI Automation
REST Assured	API Testing
TestNG	Execution Framework
Allure	Reporting
Healenium	Self-Healing
Applitools	Visual Testing
Apache Commons Math	Statistical Analysis
🐳 Docker Services

PostgreSQL (Healenium DB) – Port 5432

Healenium Server – Port 9090

docker-compose up -d
docker-compose down
📈 Reporting

Allure reports include:

Test summary

Pass/Fail breakdown

Request/Response logs

Performance alerts
