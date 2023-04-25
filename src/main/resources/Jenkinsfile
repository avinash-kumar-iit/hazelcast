@Library('demogroovy')_
def a = load('demo.groovy')
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
                // demo.info 'Starting' 
                script { 
                    a.info 'Starting'
                    a.warning 'Nothing to do!'
                }
            }
        }
    }
}