@Library('sharedlibs') _
pipeline {
    agent any
	environment {
		mavenHome = tool 'jenkins-maven'
	}
	
    stages {
        stage ('demo') {
            steps {
                welcome("Avinash Kumar")
            }
        }
		stage('Build') {
            steps {
                bat "mvn clean install -DskipTests"
            }
        }
    }
}