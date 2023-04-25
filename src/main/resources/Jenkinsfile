pipeline {
    agent any

    stages {
	
	    stage("Clone the project") {
		steps {
        git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
		}
  }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Deploy') {
            steps {
                sh 'java -jar target/hazelcast-server-0.0.1-SNAPSHOT'
            }
        }
    }
}
