pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvnw clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvnw test'
            }
        }

        stage('Deploy') {
            steps {
                sh 'java -jar target/hazelcast-server-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
