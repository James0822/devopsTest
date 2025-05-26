pipeline {
    agent { label 'jenkins-agent'}
    tools {
        jdk 'Java17'
        maven 'Maven3'
    }
    environment {
    APP_NAME = "devopsTest"
    RELEASE = "1.0.0"
    DOCKER_USER = "18621759267"
    DOCKER_PASS = "DockerHub"
    IMAGE_NAME = "${DOCKER_USER}" + "/" + "devopsapp"
    IMAGE_TAG = "${RELEASE}-${BUILD_NUMBER}" 
    }
    stages{
        stage("Cleanup Workspace"){
            steps {
                cleanWs()
            }
        }

        stage("Checkout from SCM"){
            steps {
                git branch: 'main', credentialsId: 'github', url: 'https://github.com/James0822/devopsTest'
            }
        }
        stage("Build Application"){
            steps {
                sh "mvn clean package"
            }
        }
        stage("Test Application"){
            steps {
                sh "mvn test"
            }
        }
        stage("Sonarqube Analysis"){
            steps {
            script {
                withSonarQubeEnv(credentialsId: 'sonarqube-jenkins-token') {
                    sh "mvn sonar:sonar"
                }
            }
          }
        }
        stage("Quality Gate"){
            steps {
            script {
                waitForQualityGate abortPipeline: false, credentialsId: 'sonarqube-jenkins'
            }
          }
        }
        stage("Build & Push Docker Images") {
            steps {
            script {
                docker.withRegistry('',DOCKER_PASS) {
                docker_image = docker.build "${IMAGE_NAME}"
                    }
                docker.withRegistry('',DOCKER_PASS){
                    docker_image.push("${IMAGE_TAG}")
                    docker_image.push('latest')
                    }
                }
            }
        }
        stage("Trvvy Scan"){
            steps {
            script {
               sh ('docker run -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image 18621759267/devopsapp:latest --no-progress --scanners vuln --exit-code 0 --severity HIGH,CRITICAL --format table')
            }
          }
        }
        stage("Cleanup Artifacts"){
            steps {
            script {
                sh "docker rmi ${IMAGE_NAME}:${IMAGE_TAG}"
                sh "docker rmi ${IMAGE_NAME}:latest"
            }
          }
        }
    }
}
