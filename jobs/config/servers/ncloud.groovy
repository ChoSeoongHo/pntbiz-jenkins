package jobs.config.servers

/**
 * NCloud 서버 구성(configuration) 정보
 * <ul>
 * <li>cloudType: 클라우드 구분("ncloud"로 고정)</li>
 * <li>description: 서버에 대한 간략 설명</li>
 * <li>instanceNo: 서버 인스턴스 번호</li>
 * <li>ip: 서버의 공인 IP 주소</li>
 * <li>playbook: 해당 서버를 관리하는 Ansible Playbook 경로</li>
 * <li>inventory: Ansible Inventory 파일 경로</li>
 * <li>suffix: 서버 식별용 접미사</li>
 * </ul>
 *
 * @return 서버별 설정 정보를 담은 Map 객체
 */
return [
        'demo'             : [cloudType: "ncloud", description: "Mercury-Demo", instanceNo: '20043698', ip: '223.130.131.173', playbook: '/etc/ansible/demo-mercury/playbook.yml', inventory: '/etc/ansible/demo-mercury/hosts-pnt-demo', suffix: 'demo-mercury', defaultBranch: 'pjt-01-product', env: 'product', site: 'common'],
        'demo2'            : [cloudType: "ncloud", description: "demo2", instanceNo: '20043705', ip: '175.45.202.38', playbook: '/etc/ansible/demo2/playbook.yml', inventory: '/etc/ansible/demo2/hosts-pnt-demo2', suffix: 'demo2', defaultBranch: 'product', env: 'product', site: 'common'],
        'demo3'            : [cloudType: "ncloud", description: "demo3", instanceNo: '20043710', ip: '110.165.17.210', playbook: '/etc/ansible/demo3/playbook.yml', inventory: '/etc/ansible/demo3/hosts-pnt-demo3', suffix: 'demo3', defaultBranch: 'product', env: 'product', site: 'common'],
        'demo4'            : [cloudType: "ncloud", description: "demo4", instanceNo: '20043715', ip: '175.45.203.245', playbook: '/etc/ansible/demo4/playbook.yml', inventory: '/etc/ansible/demo4/hosts-pnt-demo4', suffix: 'demo4', defaultBranch: 'product', env: 'product', site: 'common'],
        'dev-so-pm'        : [cloudType: "ncloud", description: "dev-so-pm", instanceNo: '23810121', ip: '223.130.155.51', playbook: '/etc/ansible/dev-so-pm/playbook.yml', inventory: '/etc/ansible/dev-so-pm/hosts-dev-so-pm', suffix: 'dev-so-pm', defaultBranch: 'pjt-01-develop', env: 'develop', site: 'common'],
        'dev-01'           : [cloudType: "ncloud", description: "Server-01", instanceNo: '19762496', ip: '110.165.16.192', playbook: '/etc/ansible/server-01/playbook.yml', inventory: '/etc/ansible/server-01/hosts-server-01', suffix: 'server-01', defaultBranch: 'pjt-01-develop', env: 'server', site: '1'],
        'dev-02'           : [cloudType: "ncloud", description: "Server-02", instanceNo: '19827960', ip: '223.130.137.110', playbook: '/etc/ansible/server-02/playbook.yml', inventory: '/etc/ansible/server-02/hosts-server-02', suffix: 'server-02', defaultBranch: 'pjt-01-develop', env: 'server', site: '2'],
        'dev-04'           : [cloudType: "ncloud", description: "Saturn", instanceNo: '19827979', ip: '223.130.138.245', playbook: '/etc/ansible/server-04/playbook.yml', inventory: '/etc/ansible/server-04/hosts-server-04', suffix: 'server-04', defaultBranch: 'pjt-02-develop', env: 'server', site: '4'],
        'dev-05'           : [cloudType: "ncloud", description: "Server-05", instanceNo: '19827989', ip: '101.79.8.98', playbook: '/etc/ansible/server-05/playbook.yml', inventory: '/etc/ansible/server-05/hosts-server-05', suffix: 'server-05', defaultBranch: 'pjt-02-develop', env: 'server', site: '5'],
        'dev-06'           : [cloudType: "ncloud", description: "Server-06", instanceNo: '19827993', ip: '223.130.131.201', playbook: '/etc/ansible/server-06/playbook.yml', inventory: '/etc/ansible/server-06/hosts-server-06', suffix: 'server-06', defaultBranch: 'pjt-02-develop', env: 'server', site: '6'],
        'dev-07'           : [cloudType: "ncloud", description: "Server-07", instanceNo: '19827996', ip: '175.106.98.53', playbook: '/etc/ansible/server-07/playbook.yml', inventory: '/etc/ansible/server-07/hosts-server-07', suffix: 'server-07', defaultBranch: 'pjt-02-develop', env: 'server', site: '7'],
        'dev-08'           : [cloudType: "ncloud", description: "Server-08", instanceNo: '19827999', ip: '223.130.130.42', playbook: '/etc/ansible/server-08/playbook.yml', inventory: '/etc/ansible/server-08/hosts-server-08', suffix: 'server-08', defaultBranch: 'pjt-02-develop', env: 'server', site: '8'],
        'dev-09'           : [cloudType: "ncloud", description: "Server-09", instanceNo: '20270329', ip: '110.165.18.45', playbook: '/etc/ansible/server-09/playbook.yml', inventory: '/etc/ansible/server-09/hosts-server-09', suffix: 'server-09', defaultBranch: 'pjt-05-0002', env: 'develop', site: 'common'],
        'dev-10'           : [cloudType: "ncloud", description: "Mercury", instanceNo: '20270492', ip: '175.45.205.235', playbook: '/etc/ansible/server-10/playbook.yml', inventory: '/etc/ansible/server-10/hosts-server-10', suffix: 'server-10', defaultBranch: 'pjt-01-develop', 'runTestBeforeBuild': true],
        'dev-11'           : [cloudType: "ncloud", description: "Server-11", instanceNo: '20270495', ip: '101.79.11.236', playbook: '/etc/ansible/server-11/playbook.yml', inventory: '/etc/ansible/server-11/hosts-server-11', suffix: 'server-11', defaultBranch: 'pjt-01-develop', env: 'server', site: '11'],
        'dev-12'           : [cloudType: "ncloud", description: "Server-12", instanceNo: '21792477', ip: '175.45.203.5', playbook: '/etc/ansible/server-12/playbook.yml', inventory: '/etc/ansible/server-12/hosts-server-12', suffix: 'server-12', defaultBranch: 'pjt-01-develop', env: 'server', site: '12'],
        'dev-13'           : [cloudType: "ncloud", description: "Server-13", instanceNo: '21792488', ip: '223.130.129.39', playbook: '/etc/ansible/server-13/playbook.yml', inventory: '/etc/ansible/server-13/hosts-server-13', suffix: 'server-13', defaultBranch: 'pjt-01-develop', env: 'server', site: '13'],
        'dev-14'           : [cloudType: "ncloud", description: "Server-14", instanceNo: '21792498', ip: '223.130.140.171', playbook: '/etc/ansible/server-14/playbook.yml', inventory: '/etc/ansible/server-14/hosts-server-14', suffix: 'server-14', defaultBranch: 'pjt-01-develop', env: 'server', site: '14'],
        'dev-15'           : [cloudType: "ncloud", description: "Server-15", instanceNo: '22962470', ip: '175.106.98.20', playbook: '/etc/ansible/server-15/playbook.yml', inventory: '/etc/ansible/server-15/hosts-server-15', suffix: 'server-15', defaultBranch: 'pjt-01-develop', env: 'server', site: '15'],
        'dev-16'           : [cloudType: "ncloud", description: "Server-16", instanceNo: '22962476', ip: '223.130.163.90', playbook: '/etc/ansible/server-16/playbook.yml', inventory: '/etc/ansible/server-16/hosts-server-16', suffix: 'server-16', defaultBranch: 'pjt-01-develop', env: 'server', site: '16'],
        'dev-17'           : [cloudType: "ncloud", description: "Server-17", instanceNo: '24096149', ip: '101.79.9.105', playbook: '/etc/ansible/server-17/playbook.yml', inventory: '/etc/ansible/server-17/hosts-server-17', suffix: 'server-17', defaultBranch: 'pjt-01-develop', env: 'server', site: '17'],
        'dev-18'           : [cloudType: "ncloud", description: "Server-18", instanceNo: '22962522', ip: '110.165.16.76', playbook: '/etc/ansible/server-18/playbook.yml', inventory: '/etc/ansible/server-18/hosts-server-18', suffix: 'server-18', defaultBranch: 'pjt-02-develop', env: 'server', site: '18'],
        'dev-19'           : [cloudType: "ncloud", description: "Server-19", instanceNo: '24096157', ip: '175.106.96.160', playbook: '/etc/ansible/server-19/playbook.yml', inventory: '/etc/ansible/server-19/hosts-server-19', suffix: 'server-19', defaultBranch: 'pjt-01-develop', env: 'server', site: '19'],
        'dev-20'           : [cloudType: "ncloud", description: "Server-20", instanceNo: '22962554', ip: '175.106.97.24', playbook: '/etc/ansible/server-20/playbook.yml', inventory: '/etc/ansible/server-20/hosts-server-20', suffix: 'server-20', defaultBranch: 'pjt-01-develop', env: 'server', site: '20'],
        'dev-21'           : [cloudType: "ncloud", description: "Server-21", instanceNo: '19016093', ip: '223.130.160.253', playbook: '/etc/ansible/server-21/playbook.yml', inventory: '/etc/ansible/server-21/hosts-server-21', suffix: 'server-21', defaultBranch: 'pjt-05-0001', env: 'develop', site: 'common'],
        'dev-22'           : [cloudType: "ncloud", description: "Server-22", instanceNo: '19016096', ip: '101.79.10.158', playbook: '/etc/ansible/server-22/playbook.yml', inventory: '/etc/ansible/server-22/hosts-server-22', suffix: 'server-22', defaultBranch: 'pjt-01-develop', env: 'server', site: '22'],
        'dev-23'           : [cloudType: "ncloud", description: "Server-23", instanceNo: '19016099', ip: '175.45.201.119', playbook: '/etc/ansible/server-23/playbook.yml', inventory: '/etc/ansible/server-23/hosts-server-23', suffix: 'server-23', defaultBranch: 'pjt-01-develop', env: 'server', site: '23'],
        'dev-24'           : [cloudType: "ncloud", description: "Server-24", instanceNo: '19115409', ip: '175.106.97.9', playbook: '/etc/ansible/server-24/playbook.yml', inventory: '/etc/ansible/server-24/hosts-server-24', suffix: 'server-24', defaultBranch: 'pjt-01-develop', env: 'server', site: '24'],
        'dev-25'           : [cloudType: "ncloud", description: "Server-25", instanceNo: '19115433', ip: '175.45.200.152', playbook: '/etc/ansible/server-25/playbook.yml', inventory: '/etc/ansible/server-25/hosts-server-25', suffix: 'server-25', defaultBranch: 'pjt-01-develop', env: 'server', site: '25'],
        'dev-smart-sensing': [cloudType: "ncloud", description: "Smart Sensing", instanceNo: '102941732', ip: '211.188.61.215', playbook: '/etc/ansible/dev-smart-sensing/playbook.yml', inventory: '/etc/ansible/dev-smart-sensing/hosts-dev-smart-sensing', suffix: 'dev-smart-sensing'],
        'dev-semi-rpa'     : [cloudType: "ncloud", description: "병원효율화", instanceNo: '19012706', ip: '223.130.128.47', playbook: '/etc/ansible/dev-semi-rpa/playbook.yml', inventory: '/etc/ansible/dev-semi-rpa', suffix: 'dev-semi-rpa'],
        'anam-rtls'        : [cloudType: "ncloud", description: "고대안암병원 상용", ip: '223.130.160.97', playbook: '/etc/ansible/pnt-kump-main/playbook.yml', inventory: '/etc/ansible/pnt-kump-main/', suffix: 'anam-rtls'],
        'aisa1'            : [cloudType: "ncloud", description: "asia1", ip: '3.39.96.110', playbook: '/etc/ansible/asia1-rtls/playbook.yml', inventory: '/etc/ansible/asia1-rtls/', suffix: 'asia1'],
        'dev-mfhs'         : [cloudType: "ncloud", description: "mfhs AM (고려대 유연의료 과제)", ip: '175.45.200.89', playbook: '/etc/ansible/mfhs/playbook.yml', inventory: '/etc/ansible/mfhs/', suffix: 'dev-mfhs'],
        'dev-roadpavement' : [cloudType: "ncloud", description: "도로포장IoT-dev_API(v3)", ip: '175.45.203.224', playbook: '/etc/ansible/roadpavement-dev/playbook.yml', inventory: '/etc/ansible/roadpavement-dev/', suffix: 'dev-roadpavement'],
        'stage-01' : [description: "Mercury stage 서버", ip: '49.50.129.241', playbook: '/etc/ansible/stage-01/playbook.yml', inventory: '/etc/ansible/stage-01/', suffix: 'stage-01', defaultBranch: 'pjt-01-product', 'runTestBeforeBuild': true],

]
