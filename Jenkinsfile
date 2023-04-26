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
                sh 'mvn clean package'
            }
        }
    }
}