node {
  stage("Clone the project") {
    git branch: 'main', url: 'https://github.com/avinash-kumar-iit/hazelcast.git'
  }

  stage("Build") {
    sh "./mvnw clean package"
  }
  
  stage("Tests and Deployment") {
    stage("Runing unit tests") {
      sh "./mvnw test -Punit"
    }
    stage("Deploy") {
      sh 'java -jar target/hazelcast-server-0.0.1-SNAPSHOT'
    }
  }
}