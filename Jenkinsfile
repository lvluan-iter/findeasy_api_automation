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

        stage('Publish Extent Report') {
            steps {
                publishHTML(target: [
                    reportDir: 'report',
                    reportFiles: 'extent-report.html',
                    reportName: 'Extent Report',
                    keepAll: true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }

    post {
        always {
            script {
                def testResult = junit 'target/surefire-reports/*.xml'

                def total   = testResult.totalCount
                def failed  = testResult.failCount
                def skipped = testResult.skipCount
                def passed  = total - failed - skipped

                def extentLink = "${env.BUILD_URL}artifact/report/extent-report.html"

                def message = """
                              *FindEasy API Automation Result*
                              *Status:* ${currentBuild.currentResult}
                              *Passed:* ${passed}
                              *Failed:* ${failed}
                              *Skipped:* ${skipped}
                              *Total:* ${total}
                              *Report:* ${extentLink}
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
