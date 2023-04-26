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
				script{
				log.info("sharedlibs is working now...")
			    log.warning("need to upgrade further...")
				}
            }
			
        }

        stage ('Build') {
            steps {
			    script {
				cleanWs()
				sh "mvn -version"
			    sh "echo $JAVA_HOME"  
                sh 'mvn -Dmaven.test.failure.ignore=true clean package' 
				}
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                }
            }
        }
    }
}






