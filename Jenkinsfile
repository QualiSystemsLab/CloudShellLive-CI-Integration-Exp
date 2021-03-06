
node {
    def build_name = "${env.JOB_NAME}"
    def build_number = "${env.BUILD_NUMBER}"
    def branch_name = "${env.BRANCH_NAME}"

    stage ("Build")
    {
        echo "Pulling Changed files from GitHub"
        git credentialsId: '71edbaeb-4ec1-430a-aa14-ead37881efb1', url: 'https://github.com/QualiSystemsLab/CloudShellLive-CI-Integration-Exp'
        def server = Artifactory.server('Artifactory')
        load 'Test.Groovy'
        def uploadSpec = """
        {
        "files":[
                    {
                      "pattern": "index.html",
                      "target": "CloudShell-Live-CI/""" + build_number + """/",
                      "recursive": "true",
                      "flat": "false"
                    }
                ]
        }"""
        def buildInfo = server.upload(uploadSpec)
        server.publishBuildInfo buildInfo
    }
    stage ("Test - HA")
    {
        echo "Performing HA Testing"
        try {
        // do something that fails
            sandboxId = startSandbox(maxDuration: 30, name: 'test conflict', sandboxName: 'Flex - Test - HA_' + build_number, timeout: 1 )
            echo "Sandbox started"
            withEnv(['SANDBOX_ID='+sandboxId]) 
            {
                sh "python ./jenkins/jenkins_test.py"
            }
            sleep(10)
            stopSandbox(sandboxId)
        } catch (Exception err) {
            echo err.getClass().getName()

            echo "Conflict found... could not start sandbox"
            currentBuild.result = 'NOT_BUILT'
            throw err
        }



    }
    stage ("Test - Performance")
    {
        echo 'banch name is: ' + branch_name
        if (branch_name == 'master'){
            try {

                echo "Performing Performance Testing"
                sandboxId = startSandbox(maxDuration: 30, name: 'Flex Performance', sandboxName: 'Flex - Test Performance_' + build_number) 
                withEnv(['SANDBOX_ID='+sandboxId]) 
                {
                    sh "python ./jenkins/jenkins_test.py"
                }
                sleep(10)
                stopSandbox(sandboxId)
            }
            catch (Exception err) {

                echo "Conflict found... could not start sandbox"
                currentBuild.result = 'NOT_BUILT'
            }
        }

    }
    stage ("Deploy to production")
    {
        if (branch_name == 'master'){
            echo "Deploying build"
            echo "Deployment ended for build:${env.BUILD_NUMBER}"
            echo "Deployed file:index.html"
        }
    }
    deleteDir()
}
