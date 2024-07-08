def lintchecks () {
          sh "echo performing lint checks for python code"
          sh "pip install pylint && pylint *.py"
          sh "echo lint check is completed for $COMPONENT"
}

def call () {
pipeline { 
    agent any
    stages {
        stage('Lint Checks') {
            steps {
               script {
                lintchecks ()
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