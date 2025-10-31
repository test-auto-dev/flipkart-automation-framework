# CI/CD Pipeline - Quick Reference & Cheat Sheet

## Azure DevOps - Quick Commands

### Run Smoke Tests with Parallelization
```
Browser: chrome
Environment: qa
Test Suite: smoke-suite.xml
Parallel Threads: 4
Include Groups: smoke
Exclude Groups: broken
```

### Run Regression Tests
```
Browser: firefox
Environment: staging
Test Suite: regression-suite.xml
Parallel Threads: 6
Include Groups: regression
Exclude Groups: broken,skip
Retry Count: 2
```

### Run Specific Test Class
```
Specific Test Class: SearchTest
Parallel Threads: 2
Retry Count: 1
```

### Run All Except Broken Tests
```
Test Suite: testng.xml
Parallel Threads: 5
Exclude Groups: broken,skip,wip
Retry Count: 2
```

### Run API Tests Only
```
Test Suite: testng.xml
Parallel Threads: 8
Include Groups: api
Exclude Groups: broken
```

### Run UI Tests Only
```
Test Suite: testng.xml
Parallel Threads: 4
Include Groups: ui
Exclude Groups: broken
```

---

## Jenkins - Quick Commands

### CLI - Run Smoke Tests
```bash
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=chrome \
  -p ENVIRONMENT=qa \
  -p PARALLEL_THREADS=4 \
  -p INCLUDE_GROUPS=smoke \
  -p EXCLUDE_GROUPS=broken
```

### CLI - Run Regression Tests
```bash
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=firefox \
  -p ENVIRONMENT=staging \
  -p PARALLEL_THREADS=6 \
  -p INCLUDE_GROUPS=regression \
  -p EXCLUDE_GROUPS=broken,skip \
  -p RETRY_COUNT=2
```

### CLI - Run Specific Test with Retry
```bash
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p SPECIFIC_TEST_CLASS=SearchTest \
  -p RETRY_COUNT=2
```

### CLI - Run with Docker Grid
```bash
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=chrome \
  -p PARALLEL_THREADS=6 \
  -p ENABLE_DOCKER=true \
  -p GRID_URL=http://localhost:4444/wd/hub
```

### CLI - High Parallelization
```bash
java -jar jenkins-cli.jar -s http://jenkins:8080 \
  build "FlipkartAutomation" \
  -p BROWSER=chrome \
  -p PARALLEL_THREADS=8 \
  -p INCLUDE_GROUPS=api \
  -p HEADLESS=true
```

---

## Maven Command Line - Direct Execution

### Basic Execution
```bash
mvn clean test
```

### With Browser
```bash
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge
```

### With Parallel Threads
```bash
mvn clean test -DthreadCount=4
mvn clean test -DthreadCount=8
```

### With Test Suite
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng/smoke-suite.xml
mvn clean test -DsuiteXmlFile=src/test/resources/testng/regression-suite.xml
```

### With Group Filtering
```bash
# Include groups
mvn clean test -Dgroups.include="smoke"
mvn clean test -Dgroups.include="smoke,api"

# Exclude groups
mvn clean test -Dgroups.exclude="broken"
mvn clean test -Dgroups.exclude="broken,skip"

# Both
mvn clean test -Dgroups.include="smoke" -Dgroups.exclude="broken"
```

### With Specific Test Class
```bash
mvn clean test -Dtest=SearchTest
mvn clean test -Dtest=SearchTest#testMethod
mvn clean test -Dtest=SearchTest,LoginTest
```

### With Exclude Test Classes
```bash
mvn clean test -DexcludeTestClasses="BrokenTest"
mvn clean test -DexcludeTestClasses="BrokenTest,SkipTest"
```

### With Retry
```bash
mvn clean test -Dsurefire.rerunFailingTestsCount=2
```

### With Headless Mode
```bash
mvn clean test -Dheadless=true
```

### Combined Commands - Most Used

#### Smoke Tests - Fast
```bash
mvn clean test \
  -Dgroups.include="smoke" \
  -DthreadCount=2 \
  -Dheadless=true
```

#### Regression - Comprehensive
```bash
mvn clean test \
  -DsuiteXmlFile=src/test/resources/testng/regression-suite.xml \
  -DthreadCount=6 \
  -Dgroups.exclude="broken" \
  -Dsurefire.rerunFailingTestsCount=2
```

#### API Tests - High Parallelization
```bash
mvn clean test \
  -Dgroups.include="api" \
  -DthreadCount=8 \
  -Dheadless=true
```

#### UI Tests - Standard
```bash
mvn clean test \
  -Dgroups.include="ui" \
  -DthreadCount=4 \
  -Dbrowser=chrome
```

#### Developer Testing - Single Test
```bash
mvn clean test \
  -Dtest=SearchTest \
  -DthreadCount=1 \
  -X
```

#### Production Smoke - Conservative
```bash
mvn clean test \
  -Dgroups.include="smoke" \
  -Denvironment=prod \
  -DthreadCount=1 \
  -Dheadless=true
```

#### Exclude Problematic Tests
```bash
mvn clean test \
  -DexcludeTestClasses="BrokenTest,LegacyTest" \
  -DthreadCount=5
```

---

## Docker Commands

### Start Selenium Grid
```bash
# Start all services
docker-compose up -d

# Start specific services
docker-compose up -d selenium-hub chrome-node firefox-node
```

### Check Status
```bash
docker-compose ps
docker logs -f selenium-hub
docker logs -f chrome-node
```

### Stop Services
```bash
# Stop all
docker-compose down

# Stop specific
docker-compose stop chrome-node

# Remove everything
docker-compose down -v
```

---

## TestNG Group Examples

### How to Mark Tests

```java
// Smoke test
@Test(groups = {"smoke"})
public void testCriticalFeature() { }

// Regression test
@Test(groups = {"regression"})
public void testDetailedFeature() { }

// API test
@Test(groups = {"api"})
public void testAPIEndpoint() { }

// Multiple groups
@Test(groups = {"smoke", "regression", "api"})
public void testImportantFeature() { }

// Broken test
@Test(groups = {"broken"})
public void testBrokenFeature() { }

// Skip test
@Test(groups = {"skip"})
public void testSkipFeature() { }
```

---

## Test Suite Configuration

### Run Smoke + Regression
```xml
<test name="All Critical Tests">
    <groups>
        <run>
            <include name="smoke"/>
            <include name="regression"/>
            <exclude name="broken"/>
        </run>
    </groups>
</test>
```

### Run All Except Broken
```xml
<test name="All Valid Tests">
    <groups>
        <run>
            <exclude name="broken"/>
            <exclude name="skip"/>
            <exclude name="wip"/>
        </run>
    </groups>
</test>
```

### Run Specific Groups
```xml
<test name="API Tests">
    <groups>
        <run>
            <include name="api"/>
            <exclude name="broken"/>
        </run>
    </groups>
</test>
```

---

## Performance Tuning

### Low-Spec Machine
```bash
mvn clean test \
  -DthreadCount=2 \
  -Dheadless=true \
  -Dgroups.include="smoke"
```

### Standard Machine
```bash
mvn clean test \
  -DthreadCount=4 \
  -Dheadless=true
```

### High-Spec / Docker
```bash
mvn clean test \
  -DthreadCount=8 \
  -Dheadless=true
```

### Memory Issues
```bash
export MAVEN_OPTS="-Xmx2048m -Xms512m"
mvn clean test -DthreadCount=2
```

---

## Common Scenarios

### Scenario 1: Quick Smoke Test
```bash
mvn clean test -Dgroups.include="smoke" -DthreadCount=2 -Dheadless=true
```

### Scenario 2: Full Regression
```bash
mvn clean test -DthreadCount=6 -Dgroups.exclude="broken" -Dsurefire.rerunFailingTestsCount=2
```

### Scenario 3: Fix a Specific Test
```bash
mvn clean test -Dtest=SearchTest#testSearchWithValidProduct -DthreadCount=1
```

### Scenario 4: Verify Bug Fix
```bash
mvn clean test -Dtest=SearchTest -Dsurefire.rerunFailingTestsCount=2
```

### Scenario 5: Exclude Known Issues
```bash
mvn clean test -DexcludeTestClasses="BrokenTest,KnownIssueTest" -DthreadCount=4
```

### Scenario 6: Production Verification
```bash
mvn clean test -Dgroups.include="smoke" -Denvironment=prod -DthreadCount=1 -Dheadless=true
```

### Scenario 7: API Testing Only
```bash
mvn clean test -Dgroups.include="api" -DthreadCount=8 -Dheadless=true
```

### Scenario 8: UI Testing Only
```bash
mvn clean test -Dgroups.include="ui" -DthreadCount=3 -Dbrowser=chrome
```

---

## Parameter Values Reference

### Browser Options
- chrome (default)
- firefox
- edge

### Environment Options
- qa (default)
- staging
- prod

### Test Suite Options
- testng.xml (default)
- smoke-suite.xml
- regression-suite.xml

### Parallel Threads Options
- 1 (single thread)
- 2, 3, 4, 5 (standard)
- 6, 8 (high parallelization)

### Include/Exclude Groups
- smoke
- regression
- api
- ui
- broken
- skip
- wip

### Retry Count Options
- 0 (no retry)
- 1, 2, 3 (retry attempts)

---

## Reporting

### View Reports
```bash
# Extent Report
open test-output/extent-reports/ExtentReport_*.html

# Screenshots on failure
ls -la test-output/screenshots/

# Logs
tail -f test-output/automation.log
```

### Export Results
```bash
# Archive for storage
tar -czf test-results-$(date +%Y%m%d_%H%M%S).tar.gz test-output/

# Copy to shared location
cp -r test-output/ /shared/results/$(date +%Y%m%d_%H%M%S)/
```

---

## Troubleshooting

### Tests Running Too Slow
- Reduce thread count → `DthreadCount=2`
- Run only smoke tests → `-Dgroups.include="smoke"`
- Use headless mode → `-Dheadless=true`

### Tests Timing Out
- Increase timeout in code
- Reduce thread count
- Increase memory: `export MAVEN_OPTS="-Xmx2048m"`

### Memory Errors
```bash
export MAVEN_OPTS="-Xmx2048m -Xms512m -XX:+UseG1GC"
mvn clean test
```

### Thread Safety Issues
- Ensure WebDriver is ThreadLocal
- Don't share objects between threads
- Use `-DthreadCount=1` to test single-threaded

### Docker Grid Issues
```bash
# Check containers
docker ps

# Check logs
docker logs selenium-hub

# Restart
docker-compose restart
```

---

## Quick Copy-Paste Templates

### Azure DevOps - Copy to Pipeline
```yaml
parameters:
  - name: parallelThreads
    type: number
    default: 3
  - name: includeGroups
    type: string
    default: ''
  - name: excludeGroups
    type: string
    default: ''
```

### Jenkins - Copy to Jenkinsfile
```groovy
string(
    name: 'PARALLEL_THREADS',
    defaultValue: '3',
    description: 'Number of parallel threads'
)
```

---

## Command Builder

Use this format to build your command:
```
mvn clean test \
  [OPTIONAL: -Dbrowser=<chrome|firefox|edge>] \
  [OPTIONAL: -Denvironment=<qa|staging|prod>] \
  [OPTIONAL: -DthreadCount=<1-8>] \
  [OPTIONAL: -Dheadless=<true|false>] \
  [OPTIONAL: -Dgroups.include="<group1,group2>"] \
  [OPTIONAL: -Dgroups.exclude="<group1,group2>"] \
  [OPTIONAL: -Dtest=<TestClass>] \
  [OPTIONAL: -DexcludeTestClasses="<Class1,Class2>"] \
  [OPTIONAL: -Dsurefire.rerunFailingTestsCount=<0-3>] \
  [OPTIONAL: -X]
```

---

## Emergency Commands

### Run Fastest Possible Tests
```bash
mvn clean test -Dgroups.include="smoke" -DthreadCount=1 -Dheadless=true
```

### Run Most Comprehensive Tests
```bash
mvn clean test -DthreadCount=8 -Dsurefire.rerunFailingTestsCount=2
```

### Reset Everything
```bash
mvn clean
docker-compose down -v
rm -rf test-output/
```

### Debug Single Test
```bash
mvn clean test -Dtest=SearchTest#testMethod -X -DthreadCount=1
```
