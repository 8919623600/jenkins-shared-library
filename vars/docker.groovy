def call() {
    node {
        git branch: "main" , url: "https://github.com/8919623600/${COMPONENT}.git"
        common.lintchecks()
        common.testcases()
        if (env.TAG_NAME != null) {
            stage("generating and publishing artifact")
            label 'ws'
            if (env.APPTYPE == "nodejs") {
                sh "echo generating node artifacts"
                // sh "npm install"
            }
            else if(env.APPTYPE == "python") {
                    sh "echo Generating Artifacts"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"        
                } 
            else if(env.APPTYPE == "java") {
                    sh "mvn clean package"
                    sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                    sh "zip -r ${COMPONENT}-${TAG_NAME}.zip  ${COMPONENT}.jar"      
                }
            else if(env.APPTYPE == "angularjs") {
                    sh "cd static/"
                    sh "zip -r ../${COMPONENT}-${TAG_NAME}.zip *"
                }
            else { sh "echo Selected Component Type Doesnt Exist" }                        
        }
        stage('Login to ECR') {
            withCredentials([usernameId: 'AWS_CREDS', passwordVariable: 'AWS_ACCESS_KEY_ID', fileCredentialId: 'AWS_CREDS', secretFileVariable: 'AWS_SECRET_ACCESS_KEY']) {
        sh "echo Downloading the pen key file for DB Connectivity"
        sh "env"
        sh "wget https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
        sh "echo Authenticating To ECR"
        sh "aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 851725330688.dkr.ecr.us-east-1.amazonaws.com"
        sh "docker build -t 851725330688.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."
        sh "docker push 851725330688.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME}"
        
            }
        }
    }
  
}