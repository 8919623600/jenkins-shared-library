def lintchecks () {
     sh "echo Installing Lint Checker for nodejs"
    //  sh "npm i jslint"                          // this command will do the lint check for nodejs code
     sh "node_modules/jslint/bin/jslint.js server.js || true"
}

def call () {
pipeline { 
    agent any
    stages {
        stage('Lint Checks') {
            steps {
                script {
                     lintchecks()
                }
            }
        }
        stage('Static Code Analysis') {
            steps {
                sh "echo Static Checks ...."
            }
        }
    }
} 

}