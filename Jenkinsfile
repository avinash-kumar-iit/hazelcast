@Library('sharedlibs') _

pipeline {
    agent any
    tools {
        maven 'Maven 3.9.1'
        jdk 'jdk-11.0.1'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
		stage ('demo') {
            steps {
                welcome("Avinash Kumar")
            }
        }

        stage ('Build') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install' 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}
