package com.hdfc
@Library('demogroovy') _

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
                // Demo.info 'Starting' 
                script { 
                    Demo.info 'Starting'
                    Demo.warning 'Nothing to do!'
                }
            }
        }
    }
}