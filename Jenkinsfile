pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK11'
    }
    
    environment {
        BROWSER = 'chrome'
        MAVEN_HOME = tool 'Maven'
        JAVA_HOME = tool 'JDK11'
        PATH = "${env.JAVA_HOME}\\bin;${env.MAVEN_HOME}\\bin;${env.PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        
        stage('Test') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    cucumber 'target/cucumber-reports/*.json'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/cucumber-reports',
                        reportFiles: 'cucumber-pretty.html',
                        reportName: 'Cucumber Report'
                    ])
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
    }
} 