@Library('sharedlibs') _
pipeline {
    agent any
	
    stages {
	    
		stage("Clone the project") {
          git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
        }
	  
        stage ('demo') {
            steps {
                welcome("Avinash Kumar")
            }
        }
		stage("Compilation") {
             sh "./mvnw clean install -DskipTests"
       }
	   
	     stage("Tests") {
               sh "./mvnw test -Punit"
	}
    }
}