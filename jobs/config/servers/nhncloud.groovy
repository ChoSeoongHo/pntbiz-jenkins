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
        'prod-scanner-manage': [cloudType: 'nhncloud', projectType: 'rnd', description: 'Scanner Management', instanceNo: '11be6fd0-8064-44da-b51b-a201ea5cebe2', ip: '133.186.209.181'],
        'mercury-x-app-01'   : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-app-01', instanceNo: '27baef88-1e73-481a-b23f-e8c8007d9253', ip: '133.186.213.163'],
        'mercury-x-k6-01'    : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-k6-01', instanceNo: 'cd6d25a3-ef8f-4adf-904b-cf6f5435a27d', ip: '133.186.213.97'],
        'mercury-x-lb-01'    : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-lb-01', instanceNo: '075fd10f-abe8-4fc0-9f73-bea3237b8364', ip: '133.186.213.174'],
        'mercury-x-mqdb'     : [cloudType: 'nhncloud', projectType: 'mercuryx', description: 'mercury-x-mqdb', instanceNo: '1f4cbcf2-cbde-4444-b468-af9c88c0748b', ip: '133.186.213.101'],
]
