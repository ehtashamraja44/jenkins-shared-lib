def setup() {
    stage('Setup') { bat 'pip install -r requirements.txt' }
}

def dev() {
    setup()
    stage('Data Ingest')  { bat 'python src/ingest.py' }
    stage('Model Train')  { bat 'python src/train.py Challenger-candidate' }
    stage('Model Deploy') { bat 'python src/deploy.py Challenger-candidate' }
    stage('Model Test')   { bat 'python src/test_model.py Challenger-candidate' }
    stage('Register as Challenger') { bat 'python src/promote.py Challenger-candidate Challenger' }
}

def preprod() {
    setup()
    stage('Data Ingest') { bat 'python src/ingest.py' }
    stage('Register as Challenger-pre-test') { bat 'python src/promote.py Challenger Challenger-pre-test' }
    stage('Model Deploy') { bat 'python src/deploy.py Challenger-pre-test' }
    stage('Model Test')   { bat 'python src/test_model.py Challenger-pre-test' }
    stage('Update to post-test') { bat 'python src/promote.py Challenger-pre-test Challenger-post-test' }
}

def prod() {
    setup()
    stage('Register as Champion') { bat 'python src/promote.py Challenger-post-test Champion' }
    stage('Model Deploy') { bat 'python src/deploy.py Champion' }
}
