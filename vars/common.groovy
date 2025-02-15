def lintchecks() {
    stage('Lint Checks') {
        sh '''
            echo Installing Lint Checker
            echo Performing Lint Checks 
            echo Lint Checks Completed
        ''' 
    }
}

def testcases() {
        stage('test cases') {
        def stages = [:]

        stages["Unit Testing"] = {
            echo "Unit Testing In Progress"
            echo "Unit Testing Is Completed"
        }

        stages["Integration Testing"] = {
            echo "Integration Testing In Progress"
            echo "Integration Testing Is Completed"
        }

        stages["Functional Testing"] = {
            echo "Functional Testing In Progress"
            echo "InteFunctionalration Testing Is Completed"
        }

        parallel(stages)
    }
}

def artifacts() {
    stage('Checkting Artifact On Nexus') {
        env.upload_status=sh(returnStdout: true, script: "curl -s -L http://172.31.38.109:8081/service/rest/repository/browse/${COMPONENT}/ | grep ${COMPONENT}-${TAG_NAME}.zip || true")
        print upload_status
    }
    
    if(env.upload_status == "") {
        stage('Generate Artifacts') {
            if(env.APPTYPE == "nodejs") {
                sh "ls -ltr"
                sh "npm install"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip node_modules/ server.js systemd.service"
            }
            else if(env.APPTYPE == "python") {
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip *.py *.ini requirements.txt"
                sh "ls -ltr"
            }
            else if(env.APPTYPE == "maven") {
                sh "mvn clean package"
                sh "mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar"
                sh "zip -r ${COMPONENT}-${TAG_NAME}.zip  ${COMPONENT}.jar systemd.service"
            }
            else if(env.APPTYPE == "angularjs") {
                sh '''cd static/
                      zip -r ../${COMPONENT}-${TAG_NAME}.zip *
                      ls -ltr 
                   ''' 

            }
        }

        stage('Publish Artifacts') {
            withCredentials([usernamePassword(credentialsId: 'NEXUS_CRED', passwordVariable: 'NEXUS_PASSWORD', usernameVariable: 'NEXUS_USERNAME')]) {
                sh "echo Publishing Artifacts"
                sh "curl -f -v -u ${NEXUS_USERNAME}:${NEXUS_PASSWORD} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip"
            }
        }
    }
}