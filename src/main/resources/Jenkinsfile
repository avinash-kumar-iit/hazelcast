@Library('demo-groovy') _

pipeline {
    agent any
    stages {
	
	  stage('Checkout') {
            steps {
                 git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
            }
        }
        stage ('Example') {
            steps {
                // hello.info 'Starting' 
                script { 
                    hello.info 'Starting'
                    hello.warning 'Nothing to do!'
                }
            }
        }
    }
}