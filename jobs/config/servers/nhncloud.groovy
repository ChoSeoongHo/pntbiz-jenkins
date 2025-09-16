package jobs.config.servers

/**
 * NHN Cloud 서버 구성(configuration) 정보
 * <ul>
 * <li>cloudType: 클라우드 구분("nhncloud"로 고정)</li>
 * <li>projectType: 프로젝트 구분("rnd" | "mercuryx" 중 하나)</li>
 * <li>description: 서버에 대한 간략 설명</li>
 * <li>instanceNo: 서버 인스턴스 번호</li>
 * <li>ip: 서버의 공인 IP 주소</li>
 * </ul>
 *
 * @return 서버별 설정 정보를 담은 Map 객체
 */
return [
        'dev-yh'             : [cloudType: 'nhncloud', projectType: 'rnd', description: '용인세브란스 개발서버(신규)', playbook: '/etc/ansible/dev-yh/playbook.yml', inventory: '/etc/ansible/dev-yh/hosts-yh', suffix: 'dev-yh', defaultBranch: 'SN_YonginSev', instanceNo: 'c89473cd-83e7-40a4-bd5e-f41733a4390e', ip: '103.218.159.21', startAt: '0 8 * * *', stopAt: '30 19 * * *'],
        'prod-scanner-manage': [cloudType: 'nhncloud', projectType: 'rnd', description: 'Scanner Management', instanceNo: '11be6fd0-8064-44da-b51b-a201ea5cebe2', ip: '133.186.209.181', startAt: '0 8 * * *', stopAt: '30 19 * * *'],
        'mercury-x-app-01'   : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-app-01', suffix: 'mercury-x-app-01', defaultBranch: 'develop-mercuryX', 'site': 'mercuryx', playbook: '/etc/ansible/mercury-x-app-01/playbook.yml', inventory: '/etc/ansible/mercury-x-app-01/hosts', instanceNo: '739ec127-4a44-4588-88b4-0683196e689b', ip: '133.186.213.163', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-app-02'   : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-app-02', suffix: 'mercury-x-app-02', defaultBranch: 'develop-mercuryX', 'site': 'mercuryx', playbook: '/etc/ansible/mercury-x-app-02/playbook.yml', inventory: '/etc/ansible/mercury-x-app-02/hosts', instanceNo: 'c9a95d17-d624-44e3-8340-99c8178340aa', ip: '133.186.213.182', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-app-03'   : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-app-03', suffix: 'mercury-x-app-03', defaultBranch: 'develop-mercuryX', 'site': 'mercuryx', playbook: '/etc/ansible/mercury-x-app-03/playbook.yml', inventory: '/etc/ansible/mercury-x-app-03/hosts', instanceNo: '85e0b0c6-dcfb-4f2b-b278-bbc9f2daa974', ip: '133.186.135.245', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-app-04'   : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-app-04', suffix: 'mercury-x-app-04', defaultBranch: 'develop-mercuryX', 'site': 'mercuryx', playbook: '/etc/ansible/mercury-x-app-04/playbook.yml', inventory: '/etc/ansible/mercury-x-app-04/hosts', instanceNo: 'fb68747a-5057-44af-9fa5-6017c9d7d5f7', ip: '133.186.143.6', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-k6-01'    : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-k6-01', instanceNo: '3a60f84a-b311-4df5-b6a0-a51fe96d6e52', ip: '133.186.213.97', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-mqdb-01'  : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-mqdb-01', instanceNo: 'fa3b5b6d-b9e3-42cf-8f6d-bde73f96981d', ip: '125.6.36.25', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-mqdb-02'  : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-mqdb-02', instanceNo: '7ee7ce79-07ed-42de-8598-c9d59179f044', ip: '125.6.36.161', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-mqdb-03'  : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-mqdb-03', instanceNo: '726877e1-4edc-49dc-bb54-50a014493037', ip: '125.6.36.24', startAt: '', stopAt: '30 19 * * *'],
        'mercury-x-maria-01' : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-maria-01', instanceNo: 'b4d854f4-5b67-4b2c-91bd-2b62bb90b048', ip: '133.186.213.236', startAt: '', stopAt: '30 19 * * *'],
]
