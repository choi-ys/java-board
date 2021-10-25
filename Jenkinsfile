node {
    stage("Checkout") {
        checkout changelog: false, poll: false, scm: [
            $class: 'GitSCM',
            branches: [[name: '*/master']],
            extensions: [],
            userRemoteConfigs: [[credentialsId: 'Github-Repo', url: 'https://github.com/choi-ys/java-board.git']]]
        sh "echo 'Checkout SCM'"
    }
    stage('Gradle build') {
        sh './gradlew clean build'
        sh "echo 'Build jar'"
    }
    stage('Docker build') {
        app = docker.build("413809178474.dkr.ecr.ap-northeast-2.amazonaws.com/cloudm-admin-api")
        sh "echo 'Build docker'"
    }
    stage('ECR Push') {
        sh 'rm  ~/.dockercfg || true'
        sh 'rm ~/.docker/config.json || true'

        docker.withRegistry('https://413809178474.dkr.ecr.ap-northeast-2.amazonaws.com', 'ecr:ap-northeast-2:iam-ecr-credentials') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
        sh "echo 'Push image to ECR'"
    }
    stage('Deplopy'){
        sshagent(['cloudm-admin-server']) {
            sh 'ssh -o StrictHostKeyChecking=no centos@172.31.22.224 ./deploy.sh'
        }
        sh "echo 'Pull image to ECR and Run new image'"
    }
}