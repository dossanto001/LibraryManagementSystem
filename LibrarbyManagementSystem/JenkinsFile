pipeline {

    agent any

    parameters {
        string(name: 'VERSION', defaultValue: '1.0.0', description: 'Build Version')
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building version ${parameters.VERSION}'
                sh './gradlew build'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing version ${parameters.VERSION}'
                sh './gradlew test'
            }
            //post {
            //    always {
            //        junit "build/reports/tests/**/*.xml"
            //    }
            //}
        }
    }
}