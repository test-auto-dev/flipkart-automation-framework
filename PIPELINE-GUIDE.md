# Enhanced CI/CD Pipeline Guide - Parallel Execution & Test Exclusion

## Overview

This guide explains the enhanced Azure DevOps and Jenkins pipeline configurations that support:
- ✅ Configurable parallel test execution
- ✅ Dynamic test group inclusion/exclusion
- ✅ Specific test class selection
- ✅ Test class exclusion
- ✅ Retry mechanism for flaky tests
- ✅ Comprehensive reporting

---

## Files Provided

1. **azure-pipelines-enhanced.yml** - Enhanced Azure DevOps pipeline
2. **Jenkinsfile-enhanced** - Enhanced Jenkins pipeline

---

## Key Features

### 1. Parallel Execution with User Input

**Parameter:** `parallelThreads`

**Default:** 3  
**Options:** 1, 2, 3, 4, 5, 6, 8

**How it works:**
```yaml
-DthreadCount=${{ parameters.parallelThreads }}
```

**Recommendation:**
- Local testing: 2-3 threads
- CI/CD: 4-6 threads
- Max: Available CPU cores + 1

---

### 2. Test Group Filtering

#### Include Specific Groups

**Parameter:** `includeGroups`

**Usage Examples:**
```
smoke              - Run only smoke tests
regression         - Run only regression tests
api                - Run only API tests
ui                 - Run only UI tests
smoke,api          - Run smoke and API tests
smoke,regression,api,ui  - Run all grouped tests
```

**Implementation:**
```bash
-Dgroups.include="smoke,api"
```

**In TestNG XML:**
```xml
<groups>
    <run>
        <include name="smoke"/>
        <include name="api"/>
    </run>
</groups>
```

---

#### Exclude Specific Groups

**Parameter:** `excludeGroups`

**Usage Examples:**
```
broken     - Skip broken tests
skip       - Skip tests marked as skip
wip        - Skip work-in-progress tests
broken,skip    - Exclude multiple groups
```

**Implementation:**
```bash
-Dgroups.exclude="broken,skip"
```

**In TestNG XML:**
```xml
<groups>
    <run>
        <exclude name="broken"/>
        <exclude name="skip"/>
    </run>
</groups>
```

---

### 3. Specific Test Class Selection

**Parameter:** `specificTestClass`

**Usage Examples:**
```
SearchTest
LoginTest
ProductAPITest
com.flipkart.automation.tests.ui.SearchTest
```

**Implementation:**
```bash
-Dtest=SearchTest
```

**Command:**
```bash
mvn clean test -Dtest=SearchTest
mvn clean test -Dtest=SearchTest#testMethod
```

---

### 4. Exclude Specific Test Classes

**Parameter:** `excludeTestClass`

**Usage Examples:**
```
BrokenTest
SkipTest
LegacyTest
BrokenTest,SkipTest,LegacyTest
```

**Implementation:**
```bash
-DexcludeTestClasses="BrokenTest,SkipTest"
```

---

### 5. Retry Mechanism

**Parameter:** `retryCount`

**Options:** 0, 1, 2, 3

**Implementation:**
```bash
-Dsurefire.rerunFailingTestsCount=2
```

**How it works:**
- If test fails, it will retry up to 2 times
- Only failed tests are retried
- Useful for flaky tests

---

## Azure DevOps Pipeline Usage

### Setup Instructions

1. **Add `azure-pipelines-enhanced.yml` to your repository root**
   ```bash
   git add azure-pipelines.yml
   git commit -m "Add enhanced Azure DevOps pipeline"
   git push
   ```

2. **Create Pipeline in Azure DevOps**
   - Go to Pipelines → New Pipeline
   - Select your repository
   - Choose "Existing Azure Pipelines YAML file"
   - Select `azure-pipelines-enhanced.yml`
   - Click Save

3. **Run with Parameters**
   - Click "Run pipeline"
   - Set your desired parameters
   - Click "Run"

### Example Scenarios

#### Scenario 1: Run Smoke Tests with Chrome in Parallel
```yaml
Browser: chrome
Environment: qa
Test Suite: smoke-suite.xml
Parallel Threads: 4
Include Groups: smoke
Exclude Groups: broken
```

#### Scenario 2: Run Regression Tests Excluding Broken Tests
```yaml
Browser: firefox
Environment: staging
Test Suite: regression-suite.xml
Parallel Threads: 6
Include Groups: regression
Exclude Groups: broken,skip
Retry Count: 2
```

#### Scenario 3: Run Specific Test Class Only
```yaml
Browser: chrome
Environment: qa
Specific Test Class: SearchTest
Parallel Threads: 1
Retry Count: 1
```

#### Scenario 4: Run All Tests Except Specific Classes
```yaml
Browser: edge
Environment: prod
Test Suite: testng.xml
Parallel Threads: 3
Exclude Test Classes: BrokenTest,LegacyTest
Retry Count: 2
```

#### Scenario 5: Run Only API Tests with High Parallelization
```yaml
Browser: chrome
Environment: qa
Test Suite: testng.xml
Parallel Threads: 8
Include Groups: api
Exclude Groups: broken
```

---

## Jenkins Pipeline Usage

### Setup Instructions

1. **Add `Jenkinsfile-enhanced` to your repository root**
   ```bash
   git add Jenkinsfile-enhanced
   git commit -m "Add enhanced Jenkins pipeline"
   git push
   ```

2. **Create Pipeline Job in Jenkins**
   - New Item → Pipeline
   - Configure → Pipeline → Pipeline script from SCM
   - Choose Git/GitHub
   - Set repository URL
   - Branch specifier: `*/main`
   - Script Path: `Jenkinsfile-enhanced`
   - Save

3. **Run with Parameters**
   - Click "Build with Parameters"
   - Fill in your desired parameters
   - Click "Build"

### Example Jenkins Commands

#### CLI Build Trigger
```bash
# Run smoke tests with 4 threads
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=chrome \
  -p ENVIRONMENT=qa \
  -p SUITE=smoke-suite.xml \
  -p PARALLEL_THREADS=4 \
  -p INCLUDE_GROUPS=smoke \
  -p EXCLUDE_GROUPS=broken

# Run regression tests with Docker Grid
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=chrome \
  -p ENVIRONMENT=staging \
  -p PARALLEL_THREADS=6 \
  -p ENABLE_DOCKER=true

# Run specific test with retry
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p SPECIFIC_TEST_CLASS=SearchTest \
  -p RETRY_COUNT=2
```

---

## Test Grouping in TestNG

### How to Define Groups in Tests

```java
package com.flipkart.automation.tests.ui;

import org.testng.annotations.Test;

public class SearchTest extends BaseTest {
    
    @Test(groups = {"smoke", "regression", "ui"})
    public void testSearchWithValidProduct() {
        // Test code
    }
    
    @Test(groups = {"regression", "ui"})
    public void testSearchWithMultipleKeywords() {
        // Test code
    }
    
    @Test(groups = {"broken"})
    public void testBrokenSearch() {
        // Test code
    }
}
```

### Available Groups

```
smoke       - Critical path tests
regression  - Full test coverage
api         - API tests
ui          - UI tests
broken      - Tests that need fixing
skip        - Tests to skip
wip         - Work in progress tests
```

### Test Suite Configuration (testng.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Flipkart Test Suite" parallel="methods" thread-count="3">
    
    <listeners>
        <listener class-name="com.flipkart.automation.listeners.TestListener"/>
        <listener class-name="com.flipkart.automation.listeners.RetryListener"/>
    </listeners>
    
    <!-- Smoke Tests -->
    <test name="Smoke Tests">
        <groups>
            <run>
                <include name="smoke"/>
                <exclude name="broken"/>
            </run>
        </groups>
        <classes>
            <class name="com.flipkart.automation.tests.ui.SearchTest"/>
            <class name="com.flipkart.automation.tests.api.ProductAPITest"/>
        </classes>
    </test>
    
    <!-- Regression Tests -->
    <test name="Regression Tests">
        <groups>
            <run>
                <include name="regression"/>
                <exclude name="broken"/>
            </run>
        </groups>
        <classes>
            <class name="com.flipkart.automation.tests.ui.SearchTest"/>
            <class name="com.flipkart.automation.tests.api.ProductAPITest"/>
        </classes>
    </test>
</suite>
```

---

## Command Line Examples

### Run with Parallel Threads

```bash
# Run with 5 parallel threads
mvn clean test -DthreadCount=5

# Run with 8 parallel threads (high parallelization)
mvn clean test -DthreadCount=8
```

### Run with Group Filters

```bash
# Include only smoke tests
mvn clean test -Dgroups.include="smoke"

# Include smoke and API tests
mvn clean test -Dgroups.include="smoke,api"

# Exclude broken tests
mvn clean test -Dgroups.exclude="broken"

# Include smoke but exclude broken
mvn clean test -Dgroups.include="smoke" -Dgroups.exclude="broken"
```

### Run Specific Test Classes

```bash
# Run single test class
mvn clean test -Dtest=SearchTest

# Run single test method
mvn clean test -Dtest=SearchTest#testSearchWithValidProduct

# Run multiple classes
mvn clean test -Dtest=SearchTest,LoginTest

# Exclude specific classes
mvn clean test -DexcludeTestClasses="BrokenTest,SkipTest"
```

### Combined Commands

```bash
# Run smoke tests with 4 threads, retry 2 times
mvn clean test \
  -Dgroups.include="smoke" \
  -DthreadCount=4 \
  -Dsurefire.rerunFailingTestsCount=2

# Run regression tests excluding broken, with 6 threads
mvn clean test \
  -Dgroups.include="regression" \
  -Dgroups.exclude="broken" \
  -DthreadCount=6

# Run specific test with high verbosity
mvn clean test \
  -Dtest=SearchTest \
  -X -DthreadCount=1

# Run all tests excluding certain classes with retry
mvn clean test \
  -DexcludeTestClasses="BrokenTest,LegacyTest" \
  -DthreadCount=5 \
  -Dsurefire.rerunFailingTestsCount=2 \
  -Denvironment=qa
```

---

## Pipeline Execution Flow

### Azure DevOps Flow

```
1. Pre-Validation
   ├── Validate input parameters
   ├── Check thread count
   └── Display configuration summary

2. Build
   ├── Maven clean compile
   └── Download dependencies

3. Test
   ├── Build Maven command with all parameters
   ├── Execute tests with specified parallelization
   ├── Capture screenshots on failure
   └── Generate logs

4. Reporting
   ├── Publish TestNG results
   ├── Archive Extent Reports
   ├── Archive Screenshots
   └── Archive Logs

5. Notification
   ├── Send email notification
   └── Update dashboard
```

### Jenkins Flow

```
1. Validate Parameters
   ├── Display configuration
   ├── Validate thread count
   └── Check for issues

2. Checkout
   └── Git clone repository

3. Build
   └── Maven clean compile

4. Start Selenium Grid (Optional)
   └── Docker compose up

5. Execute Tests
   ├── Build dynamic Maven command
   ├── Run tests with parameters
   └── Continue on failure

6. Analyze Results
   ├── Parse test results
   ├── Count screenshots
   └── Check logs

7. Generate Reports
   ├── Publish JUnit results
   ├── Archive artifacts
   └── Publish HTML report

8. Post Actions
   ├── Stop Docker (if used)
   ├── Cleanup
   └── Generate summary
```

---

## Performance Optimization

### Thread Count Recommendations

```
Single Machine:
- CPU Cores: 4
- Recommended Threads: 4-5
- Formula: CPU_Cores + 1

Docker/Grid:
- Recommended Threads: 6-8
- Can run more since load is distributed

Memory Considerations:
- Per thread: ~200-300 MB
- 5 threads: 1-1.5 GB
- 8 threads: 1.5-2.5 GB
```

### Optimization Tips

1. **Use headless mode for CI/CD**
   ```bash
   -Dheadless=true
   ```

2. **Increase threads for API tests** (lighter than UI)
   ```bash
   -DthreadCount=8 (for API tests)
   -DthreadCount=4 (for UI tests)
   ```

3. **Use Docker Grid for high parallelization**
   ```bash
   docker-compose up -d
   -DgridUrl=http://localhost:4444/wd/hub
   ```

4. **Filter tests by group to reduce execution time**
   ```bash
   -Dgroups.include="smoke"
   ```

---

## Troubleshooting

### Issue: Tests fail with thread-safety errors

**Solution:** Ensure WebDriver is ThreadLocal
```java
private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
```

### Issue: Too many threads causing timeouts

**Solution:** Reduce thread count
```bash
mvn clean test -DthreadCount=2
```

### Issue: Some tests always fail when run in parallel

**Solution:** Add RetryAnalyzer
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
```

### Issue: Memory OutOfMemory errors

**Solution:** Increase Maven heap size
```bash
export MAVEN_OPTS="-Xmx2048m -Xms512m"
mvn clean test
```

### Issue: Reports not generated

**Solution:** Check report directory permissions
```bash
mkdir -p test-output/extent-reports
chmod 755 test-output/extent-reports
```

---

## Monitoring & Metrics

### Jenkins Integration

Track metrics in Jenkins:
- Build duration
- Test count
- Failure rate
- Trend analysis

### Azure DevOps Integration

Track metrics in Azure DevOps:
- Test pass/fail rate
- Execution time trends
- Pipeline failures
- Resource usage

### Custom Dashboard

Create dashboard showing:
- Execution time vs thread count
- Failure rate by test group
- Coverage by environment

---

## Best Practices

1. **Always validate configuration before running**
   - Check thread count
   - Verify environment
   - Confirm test groups

2. **Use meaningful test group names**
   - smoke, regression, api, ui
   - Not: test1, test2, test3

3. **Exclude broken tests by default**
   ```bash
   -Dgroups.exclude="broken"
   ```

4. **Use retry for known flaky tests**
   ```bash
   -Dsurefire.rerunFailingTestsCount=2
   ```

5. **Monitor pipeline execution**
   - Check logs regularly
   - Review reports
   - Track metrics

6. **Use Docker Grid for consistency**
   - Same environment across runs
   - Better resource utilization
   - Easier scaling

---

## Additional Resources

- TestNG Parallel Execution: https://testng.org/doc/documentation-main.html#parallel-running
- Maven Surefire Plugin: https://maven.apache.org/surefire/maven-surefire-plugin/
- Azure Pipelines Documentation: https://docs.microsoft.com/en-us/azure/devops/pipelines/
- Jenkins Pipeline Documentation: https://www.jenkins.io/doc/book/pipeline/

---

## Support

For issues or questions:
1. Check troubleshooting section
2. Review logs: `test-output/automation.log`
3. Check reports: `test-output/extent-reports/`
4. Review pipeline configuration
