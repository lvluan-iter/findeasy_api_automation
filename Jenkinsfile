pipeline {
    agent any

    environment {
        SLACK_WEBHOOK = credentials('slack-webhook')
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Test') {
            steps {
                bat """
                    mvn clean test -Dgroups=%groups%
                """
            }
        }
    }

    post {
        always {
            script {
                def message = """
                              *FindEasy API Automation*
                              *Suite:* ${suite}
                              *Groups:* ${groups}
                              *Status:* ${currentBuild.currentResult}
                              *Build URL:* ${env.BUILD_URL}
                              """

                httpRequest(
                    httpMode: 'POST',
                    url: SLACK_WEBHOOK,
                    contentType: 'APPLICATION_JSON',
                    requestBody: """
                    {
                        "text": ${groovy.json.JsonOutput.toJson(message)}
                    }
                    """
                )
            }
        }
    }
}