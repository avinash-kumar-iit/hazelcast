@Library('demogroovy')package com.hdfc.demo
def dm = new demo(this)
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
                    dm.info 'Starting'
                    dm.warning 'Nothing to do!'
                }
            }
        }
    }
}