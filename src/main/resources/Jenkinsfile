pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                springBoot {
                    app = 'hazelcast-server-0.0.1-SNAPSHOT.jar'
                    args = '--server.port=8080'
                }
            }
        }
    }
}
