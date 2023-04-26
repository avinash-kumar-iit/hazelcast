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
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
                {sh 'mvn -B -DskipTests clean package' }
            }
        }
    }
}

