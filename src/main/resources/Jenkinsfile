@Library('sharedlibs') _

pipeline {
    agent any
	
	environment {
	   PATH = "C:/Program Files/Java/jdk-11.0.1/bin:$PATH"
	}
  
    stages {
	
	  stage('git clone') {
            steps {
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
            }
        }
       
		stage ('demo') {
            steps {
                welcome("Avinash Kumar")
				script{
				log.info("sharedlibs is working now...")
			    log.warning("need to upgrade further...")
				}
            }
			
        }

        stage ('Build') {
            steps {
                sh "mvn clean install" 
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}
