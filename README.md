# elibraryback

CI : Frequently merging code changes, le code compile? quelque chose est cassé? vérifier tout ça après un merge? tests automatiques??
CD (delivery) On peut déployer toutes les versions de notre code, 
Jenkins Solution C'est nécessaire pour du déploiement continue le code est envoyé en prod


Log in to the Jenkins instance using the credentials provided in a new browser tab:
<PUBLIC_IP_ADDRESS>:8080
Log in to the Jenkins server using the credentials provided to retrieve the temporary admin password for the Jenkins instance:
ssh cloud_user@<PUBLIC_IP_ADDRESS>


Configure Jenkins to run the new dockerized train-schedule pipeline.
1. Access the Jenkins instance and create the first admin user. 
2. Using the cloud_user password, create a global Jenkins credential for the production server. That Jenkins will use to authenticate to the server
    * Kind: Username with password
    * Scope: Global
    * Username: deploy
    * Password: <CLOUD_USER_PASSWORD>
    * ID: webserver_login
    * Description: Webserver Login
3. Create a global Jenkins credential for the Docker image registry (Docker Hub).
    * Kind: Username with password
    * Username: <DOCKER_HUB_USERNAME>
    * Password: <DOCKER_HUB_PASSWORD>
    * ID: docker_hub_login
    * Description: Docker Hub Login
    * 
4. Note: You will need a Docker hub account in order to use Docker Hub as an image registry.  
5. Configure a global property in Jenkins to store the production server IP by navigating to Manage Jenkins > Configure System and adding a environment variable.
    * Name: prod_ip
    * Value: <PRODUCTION_SERVER_PUBLIC_IP_ADDRESS>


1. Make a personal fork of the GitHub repo at: https://github.com/linuxacademy/cicd-pipeline-train-schedule-dockerdeploy 
2. Generate a new GitHub API key to allow Jenkins to access the forked repo by navigating to Profile > Settings > Developer Settings > Personal Access Tokens > Generate New Token.
    * Token Description: Jenkins
    * Permissions: admin-repo_hook
3. Copy the GitHub token. 
4. In Jenkins, create a Multibranch Pipeline project named train-schedule. 
5. Under Credentials, add the GitHub account used to fork the repo to Jenkins.
    * Kind: Username with password
    * Scope: Global
    * Username: <GITHUB_USERNAME>
    * Password: <GITHUB_API_KEY>
    * ID: github_key
    * Description: Github Key
6. Select the Github Key and the forked repository.
    * Owner: <GITHUB_USERNAME>
    * Repository: cicd-pipeline-train-schedule-dockerdeploy
7. Click save. 
Successfully deploy the train-schedule app to production as a Docker container using the Jenkins Pipeline.
Modify the Jenkinsfile in GitHub to build and push the Docker image to Docker Hub, and commit the changes.

    stages {
        stage('Build') {
            steps {
                echo 'Running build automation'
                sh './gradlew build --no-daemon'
                archiveArtifacts artifacts: 'dist/trainSchedule.zip'
            }
         }
         stage('Build Docker Image') {
             when {
                 branch 'master'
             }
             steps {
                 script {
                     app = docker.build("<DOCKER_HUB_USERNAME>/train-schedule")
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
     }   


 In Jenkins, click Build Now. Note: The initial build may take several minutes to complete.  
 In Docker Hub, under Repositories, select the train-schedule app. 
 Click the Tags tab to verify that the build was pushed successfully. 
 In GitHub, modify the Jenkinsfile to include a stage that pushes the build to the production server, and commit the changes. 
 
 
 stage ('DeployToProduction') {
     when {
         branch 'master'
     }
     steps {
         input 'Deploy to Production'
         milestone(1)
         withCredentials ([usernamePassword(credentialsId: 'webserver_login', usernameVariable: 'USERNAME', passwordVariable: 'USERPASS')]) {
             script {
                 sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.prod_ip} \"docker pull <DOCKER_HUB_USERNAME>/train-schedule:${env.BUILD_NUMBER}\""
                 try {
                    sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.prod_ip} \"docker stop train-schedule\""
                    sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.prod_ip} \"docker rm train-schedule\""
                 } catch (err) {
                     echo: 'caught error: $err'
                 }
                 sh "sshpass -p '$USERPASS' -v ssh -o StrictHostKeyChecking=no $USERNAME@${env.prod_ip} \"docker run --restart always --name train-schedule -p 8080:8080 -d <DOCKER_HUB_USERNAME>/train-schedule:${env.BUILD_NUMBER}\""
             }
         }
     }
     
 In Jenkins, click Build Now. 
 Once the build is complete, using a web browser, verify that the application has been deployed successfully. <PRODUCTION_SERVER_PUBLIC_IP_ADDRESS>:8080 
Conclusion
Congratulations — you've completed this hands-on lab!
