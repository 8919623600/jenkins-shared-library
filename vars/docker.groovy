def call() {
    node {
        git branch: "main" , url: "https://github.com/8919623600/${COMPONENT}.git"
        common.lintchecks()
        common.testcases()
        if (env.TAG_NAME != null) {
            stage("generating and publishing artifact")
            if (env.APPTYPE == "nodejs") {
                sh "echo generating node artifacts"
                sh "npm install"
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
        sh "docker build -t 355449129696.dkr.ecr.us-east-1.amazonaws.com/${COMPONENT}:${TAG_NAME} ."
    }
}