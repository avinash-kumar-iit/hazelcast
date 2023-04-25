@Library('demogroovy')_

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
                    demo.info 'Starting'
                    demo.warning 'Nothing to do!'
                }
            }
        }
    }
}