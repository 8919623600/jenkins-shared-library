def lintchecks () {
     sh "echo Installing Lint Checker for nodejs"
     sh "npm i jslint"                       // this command will do the lint check for nodejs code
     sh "echo This is lint check for $COMPONENT"  // this variable is called from the jenkins pipeline
     sh "node_modules/jslint/bin/jslint.js server.js || true"
}

def call () {
pipeline { 
    agent any
    stages {
        stage('Lint Checks') {
            steps {
                script {
                     lintchecks()        // this will call the lintcheck function. Though it is defined in same file that is why filename is not required while calling the function
                }
            }
        }
        stage('Static Code Analysis') {
            steps {
                sh "echo Static Checks ...."
            }
        }

        stage ('Building package') {
            steps {
                sh "docker build -t cart:1.0 ."
            }
        }
    }
} 

}