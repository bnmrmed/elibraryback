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
         stage('Push Docker Image') {
             when {
                 branch 'master'
             }
             steps {
                 script {
                     docker.withRegistry('https://registry.hub.docker.com', 'docker_hub_login') {
                         app.push("${env.BUILD_NUMBER}")
                         app.push("latest")
                     }
                 }
             }
         }
         stage('Deploy To Production') {
            when {
                branch 'master'
            }
            steps {
                input 'Deploy to Production?'
                milestone(1)
                withCredentials([usernamePassword(credentialsId: 'webserver_login', usernameVariable: 'USERNAME', passwordVariable: 'USERPASS')]) {
                    script {
                        sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@$prod_ip \"docker pull bnmrmed/nawfel-library:${env.BUILD_NUMBER}\""
                        try {
                            sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@$prod_ip \"docker stop nawfel-library\""
                            sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@$prod_ip \"docker rm nawfel-library\""
                        } catch (err) {
                            echo: 'caught error: $err'
                        }
                        sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@$prod_ip \"docker run --restart always --name nawfel-library -p 8080:8080 -d bnmrmed/nawfel-library:${env.BUILD_NUMBER}\""
                    }
                }
            }
        }
     }   
 }
