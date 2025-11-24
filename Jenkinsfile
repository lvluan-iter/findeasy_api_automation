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
            def testResult = junit 'target/surefire-reports/*.xml'

            def total   = testResult.totalCount
            def failed  = testResult.failCount
            def skipped = testResult.skipCount
            def passed  = total - failed - skipped

            def allureLink = "${env.BUILD_URL}allure"

            emailext(
                subject: "FindEasy API Automation - ${currentBuild.currentResult}",
                body: """
                    <h2>${suite}</h2>
                    <p><b>Status:</b> ${currentBuild.currentResult}</p>
                    <p><b>Total:</b> ${total}</p>
                    <p><b>Passed:</b> ${passed}</p>
                    <p><b>Failed:</b> ${failed}</p>
                    <p><b>Skipped:</b> ${skipped}</p>
                    <p><b>Report:</b> <a href="${allureLink}">${allureLink}</a></p>
                """,
                mimeType: 'text/html',
                to: "lvluanpy2003@gmail.com"
            )
        }
    }
}