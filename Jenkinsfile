pipeline {
    agent any

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

        stage('Allure Report') {
            steps {
                allure([
                    includeProperties: true,
                    results          : [[path: 'target/allure-results']]
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

                def allureLink = "${env.BUILD_URL}allure"

                def emailBody = readFile 'src/test/resources/email-template.html'

                emailBody = emailBody
                    .replace('${STATUS}', currentBuild.currentResult)
                    .replace('${TOTAL}', total.toString())
                    .replace('${PASSED}', passed.toString())
                    .replace('${FAILED}', failed.toString())
                    .replace('${SKIPPED}', skipped.toString())
                    .replace('${REPORT}', allureLink)

                emailext(
                    subject: "[FindEasy API] ${suite} – ${currentBuild.currentResult}",
                    body: emailBody,
                    mimeType: "text/html",
                    to: "lvluanpy2003@gmail.com"
                )
            }
        }
    }
}