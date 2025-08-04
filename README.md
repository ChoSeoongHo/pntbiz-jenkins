# pntbiz-jenkins


## 1. 개요
이 저장소는 **Groovy 기반의 Jenkins Job DSL**로 구성된 백엔드 관련 Jenkins CI/CD job 정의들을 포함합니다.  
핵심은 `seed-job`이 `backend.groovy`를 실행해 실제 서비스별, 서버별 job을 동기화하는 구조입니다.

## 2. 디렉토리 구조 요약
```
jobs/
  seed-backend.groovy              # seed job 정의 (Jenkins가 이 job을 통해 DSL을 실행)
  backend.groovy                   # 전체 Job 생성 로직 (modules/servers 정보와 템플릿 조합)
  config/
    modules.groovy                # 모듈의 repository 정보
    servers.groovy                # 서버별 메타 정보 (IP, branch, ansible inventory 등)
    serverMatrix.groovy           # 서버와 모듈 매트릭스 (어떤 서버에 어떤 job을 만들지)
  templates/                      # 서비스 유형별 job 템플릿
    maven/
    gradle/
    nodejs/
    bash/
```

## 3. 핵심 흐름 (seed → 실제 job)
1. `seed-backend.groovy`가 Jenkins에서 주기적으로 (SCM polling: `H/5 * * * *`) 실행된다.
2. seed job은 `jobs/backend.groovy`를 `dsl` step으로 불러온다.
3. `backend.groovy`는 config들(`modules.groovy`, `servers.groovy`, `serverMatrix.groovy`)과 템플릿들을 읽어와 각각의 조합에 대해 실제 Jenkins job 정의를 생성한다.

## 4. 주요 구성 파일 설명

### 4.1 `jobs/seed-backend.groovy`
- 이름: `seed-job`
- 역할: Git에서 이 DSL 리포지토리를 체크아웃하고 `jobs/backend.groovy`를 실행해 job을 동기화.
- 핵심 설정
    - Git 리포지토리: `git@github.com:ChoSeoongHo/pntbiz-jenkins.git`
    - 브랜치: `main`

### 4.2 `jobs/backend.groovy`
- 전체 job 생성 엔진.
- 다음 정보를 불러와서 job generator를 구성:
    - `modules`: 서비스/모듈별 저장소, 기본 배포 artifact 정보
    - `servers`: 서버별 메타 (기본 브랜치, 환경, Ansible 플레이북/인벤토리 등)
    - `serverMatrix`: 어떤 서버에 어떤 모듈들을 동기화할지 매핑
    - 템플릿: Maven, Gradle, Node.js, Bash 등 서비스 유형별 job 템플릿을 조합

### 4.3 `jobs/config/modules.groovy`
- 각 모듈(서비스) 정의. 예:
  ```groovy
  sh: [repository: 'pntbiz-indoorplus-smart-hospital', sourceFilePath: 'sh.zip']
  api: [repository: 'pntbiz-api', sourceFilePath: 'pntbiz-api/target/pntbiz_api-0.0.1-SNAPSHOT.war']
  ```
- 키: 모듈 식별자 (`api`, `wms`, 등)
- 값: repo 정보 및 빌드 결과물 위치 등

### 4.4 `jobs/config/servers.groovy`
- 서버별 설정. 주요 속성 예시:
    - `description`: 설명
    - `instanceNo`, `ip`: 인스턴스 식별
    - `playbook`, `inventory`: Ansible 관련 경로
    - `defaultBranch`: 기본 git 브랜치 (예: `pjt-01-develop`)
    - `env`, `site`, `suffix` 등 deploy/템플릿에 주입되는 값

### 4.5 템플릿 (`jobs/templates/...`)
- 각 서비스 유형별로 재사용 가능한 job 정의를 반환하는 클로저 형태.
- 공통 패턴:
    - `return { Map config -> job(config.jobName) { ... } }`
    - 파라미터 정의 (`BRANCH`, `ENV`, `SITE`, `NODE_VERSION`, 등)
    - SCM 설정 (git checkout)
    - 빌드/배포 스크립트
    - 슬랙 알림 설정 (`slackNotifier`)
- 예시:
    - `maven/mavenJob.groovy`: Maven 기반 서비스의 기본 job 구조. 브랜치, ENV, SITE 등을 파라미터로 받음.
    - `gradle/oauth.groovy`: `gradleJob` 템플릿을 래핑하여 OAuth 서비스 전용 config를 추가로 주입.
    - `nodejs/socket.groovy`: Node.js 서비스용 job 템플릿. `NODE_VERSION` 선택 파라미터 포함.

## 5. 사용법

### 5.1 초기 실행 (seed job)
Jenkins에 이 리포지토리를 seed job으로 등록한 뒤, `seed-job`을 실행하면 `backend.groovy`가 돌아가고 모든 정의된 서비스/서버 조합에 대해 job을 생성/동기화한다.

### 5.2 새로운 서버 추가
1. `jobs/config/servers.groovy`에 새로운 서버 엔트리 추가:
   ```groovy
   'dev-99': [
     description: "새로운 99번 개발서버",
     instanceNo: '99',
     ip: '1.2.3.4',
     defaultBranch: 'develop',
     env: 'develop',
     site: 'common',
     playbook: '/path/playbook.yml',
     inventory: '/etc/ansible/dev-99',
     suffix: 'dev-99'
   ],
   ```
2. `jobs/config/serverMatrix.groovy` (또는 `modules.groovy` 하단의 서버별 모듈 매핑)에서 이 서버에 대해 필요한 모듈 나열.
3. seed job이 다시 실행되면 자동 반영됨.

### 5.3 새로운 모듈/서비스 추가
1. `modules.groovy`에 모듈 정의 추가 (repository, artifact 경로 등).
2. `serverMatrix.groovy` 또는 관련 매핑에 수행할 서버에 해당 모듈을 포함.
3. 해당 모듈에 맞는 템플릿이 없다면 `jobs/templates/<type>/`에 새 템플릿 작성하고 `backend.groovy`에서 jobGenerators에 추가.

### 5.4 템플릿 커스터마이징
- 템플릿은 `Map config`를 받아서 job을 구성한다. 기존 템플릿을 확장하려면 래핑 클로저를 만들고 baseConfig를 덮어쓰거나 추가 필드를 주입하면 된다. (예: `gradle/oauth.groovy` 방식)

## 6. 예시

#### 6.1 Maven 기반 API 서비스 job 생성 (요약)
```groovy
def mavenTemplate = evaluate(readFileFromWorkspace('jobs/templates/maven/mavenJob.groovy'))
def apiGenerator = evaluate(readFileFromWorkspace('jobs/templates/maven/api.groovy'))(mavenTemplate)
apiGenerator([
    jobName: 'api-deploy',
    description: 'API 서비스 배포',
    defaultBranch: 'main',
    env: 'prod',
    site: 'common'
])
```

#### 6.2 Node.js socket 서비스
```groovy
def socketGenerator = evaluate(readFileFromWorkspace('jobs/templates/nodejs/socket.groovy'))
socketGenerator([
    jobName: 'socket-deploy',
    description: 'Socket 서비스 배포',
    defaultBranch: 'feature/x',
    config: 'dev/common',
    shell: 'dev'
])
```

## 7. 요구사항 / 주의사항
- Jenkins에 다음 플러그인 필요: Job DSL, Git, Slack Notification, 관련 빌드 도구(Gradle, Maven) 실행 환경.
- SSH 키/credential이 Jenkins에 등록돼 있어야 함 (예: `ssh-pntbiz-jenkins`, `ssh-pntbiz-indoorplus-oauth` 등).
- Ansible 관련 deployment를 전제한 playbook/inventory 경로가 유효해야 함.
- Seed Job DSL을 수정한 경우, 변경 내용을 자동으로 적용하기 전에 **관리자의 명시적인 승인**을 받아야 함.