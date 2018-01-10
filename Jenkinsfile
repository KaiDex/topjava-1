#!/usr/bin/env groovy

// Declarative //
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building stage'
                bat 'make'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }
}
//// Script //
//node {
//    stage('Build') {
//        echo 'Building....'
//        bat 'make'
//        archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
//    }
//}
