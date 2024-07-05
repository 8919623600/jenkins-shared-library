def lintchecks() {
    sh "This is a java based project"
    sh "echo performing lint checks for $COMPONENT"
    sh "mvn checkstyle:check || true"
    sh "lintcheck is over with the xyz report"
}

def call() {
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