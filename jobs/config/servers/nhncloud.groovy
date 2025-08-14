package jobs.config.servers

/**
 * NHN Cloud 서버 구성(configuration) 정보
 * <ul>
 * <li>cloudType: 클라우드 구분("nhncloud"로 고정)</li>
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
        'mercury-x-app-01': [cloudType: "nhncloud", projectType: "mercuryx", description: "mercury-x-app-01", instanceNo: '27baef88-1e73-481a-b23f-e8c8007d9253', ip: '133.186.213.163'],
]
