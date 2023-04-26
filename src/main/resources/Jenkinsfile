@Library('sharedlibs') _
pipeline {
    agent any
    stages {
        stage ('demo') {
            steps {
                welcome("Avinash Kumar")
            }
        }
		stage('Build') {
            steps {
                sh "mvn -Dmaven.test.failure.ignore=true clean install"
            }
        }
    }
}