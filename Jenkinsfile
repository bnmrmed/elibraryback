pipeline {
    agent any
    stages {
         stage('Build Docker Image') {
             when {
                 branch 'master'
             }
             steps {
                 script {
                     app = docker.build("bnmrmed/nawfel-library")
                     app.inside {
                         sh 'echo $(curl localhost:8080)'
                     }
                 }
             }
         }
     }   
 }
