pipeline {
    agent any
    
    parameters {
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], 
               description: 'Select browser')
        choice(name: 'ENVIRONMENT', choices: ['qa', 'staging', 'prod'], 
               description: 'Select environment')
        choice(name: 'SUITE', choices: ['testng.xml', 'smoke-suite.xml', 'regression-suite.xml'], 
               description: 'Select test suite')
        booleanParam(name: 'HEADLESS', defaultValue: true, 
               description: 'Run in headless mode')
        string(name: 'THREAD_COUNT', defaultValue: '3', 
               description: 'Number of parallel threads')
    }
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-11'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                    url: 'https://github.com/your-repo/flipkart-automation.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Run Tests') {
            steps {
                script {
                    sh """
                        mvn clean test \
                        -Dbrowser=${params.BROWSER} \
                        -Denvironment=${params.ENVIRONMENT} \
                        -Dheadless=${params.HEADLESS} \
                        -DthreadCount=${params.THREAD_COUNT} \
                        -DsuiteXmlFile=src/test/resources/testng/${params.SUITE}
                    """
                }
            }
        }
        
        stage('Generate Reports') {
            steps {
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'test-output/extent-reports',
                    reportFiles: 'ExtentReport_*.html',
                    reportName: 'Extent Report',
                    reportTitles: 'Test Execution Report'
                ])
            }
        }
    }
    
    post {
        always {
            emailext(
                subject: "Test Execution Report - ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: """
                    Test execution completed.
                    
                    Job: ${env.JOB_NAME}
                    Build: ${env.BUILD_NUMBER}
                    Browser: ${params.BROWSER}
                    Environment: ${params.ENVIRONMENT}
                """,
                to: 'team@company.com'
            )
        }
        
        success {
            echo 'Tests passed successfully!'
        }
        
        failure {
            echo 'Tests failed!'
        }
    }
}
