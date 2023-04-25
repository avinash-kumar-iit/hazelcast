@Library('demogroovy') import static com.hdfc.foo.Utilities.*
pipeline {
    agent none
    stages {
        stage ('Example') {
            steps {
                // log.info 'Starting' 
                script { 
                    Utilities.info 'Starting'
                    Utilities.warning 'Nothing to do!'
                }
            }
        }
    }
}