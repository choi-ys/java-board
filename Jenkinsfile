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
        sh './gradlew clean build' //run a gradle task
        sh "echo 'Build jar'"
    }
    stage('Docker build') {
        app = docker.build("iam-number.dkr.ecr.ap-northeast-2.amazonaws.com/cloudm-admin-api")
        sh "echo 'Build docker'"
    }
    stage('ECR Push') {
        sh 'rm  ~/.dockercfg || true'
        sh 'rm ~/.docker/config.json || true'

        docker.withRegistry('https://iam-number.dkr.ecr.ap-northeast-2.amazonaws.com', 'ecr:ap-northeast-2:iam-ecr-credentials') {
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
        sh "echo 'Push image to ECR'"
    }
    stage('[TODO]Image pull and docker run') {
        sh "echo '#1 connect destination server by sshAgent plugin'"
        sh "echo '#2 pull imgae to ECR'"
        sh "echo '#3 stop current docker image'"
        sh "echo '#4 run new docker image'"
    }
}