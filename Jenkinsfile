@Library('sharedlibs') _

pipeline {
    agent any
    tools {
        maven 'MAVEN_HOME'
        jdk 'JAVA_HOME'
    }
    stages {
       
		stage ('demo') {
            steps {
                welcome("Avinash Kumar")
            }
			
        }

		stage ('logs') {
			steps {
			  log.info("sharedlibs is working now...")
			  log.warning("need to upgrade further...")
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
