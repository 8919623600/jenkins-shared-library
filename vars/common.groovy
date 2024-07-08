def nodejs_lintchecks () {
     sh "echo Installing Lint Checker for nodejs"
    //  sh "npm i jslint"                       // this command will do the lint check for nodejs code
     sh "echo This is lint check for $COMPONENT"  // this variable is called from the jenkins pipeline
     sh "node_modules/jslint/bin/jslint.js server.js || true"
}

def java_lintchecks () {
        sh "echo This is a java based project"
    sh "echo performing lint checks for $COMPONENT"
    sh "mvn checkstyle:check || true"
    sh "echo lintcheck is over with the xyz report"
}

