@Library('demo-groovy') _

pipeline {
    agent none
    stages {
	
	  stage('Checkout') {
            steps {
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
            }
        }
        stage ('Example') {
            steps {
                // log.info 'Starting' 
                script { 
                    log.info 'Starting'
                    log.warning 'Nothing to do!'
                }
            }
        }
    }
}