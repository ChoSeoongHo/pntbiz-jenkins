package config

/**
 * 모듈 정보
 */
return [
        sh                   : [repository: 'pntbiz-indoorplus-smart-hospital', sourceFilePath: 'sh.zip'],
        am                   : [repository: 'pntbiz-indoorplus-smart-asset-management', sourceFilePath: 'am.zip'],
        sm                   : [repository: 'pntbiz-indoorplus-smart-military', sourceFilePath: 'sm.zip'],
        ws                   : [repository: 'pntbiz-indoorplus-worker-safety', sourceFilePath: 'ws.zip'],
        ps                   : [repository: 'pntbiz-indoorplus-patient-safety', sourceFilePath: 'ps.zip'],
        ct                   : [repository: 'pntbiz-ct-front-end', sourceFilePath: 'ct.zip'],
        rms                  : [repository: 'pntbiz-indoorplus-smart-rms', sourceFilePath: 'rms.zip'],
        irds                 : [repository: 'pntbiz-irds-fe', sourceFilePath: 'irds.zip'],
        api                  : [repository: 'pntbiz-api', sourceFilePath: 'pntbiz-api/target/pntbiz_api-0.0.1-SNAPSHOT.war'],
        wms                  : [repository: 'pntbiz-wms', sourceFilePath: 'pntbiz-wms/target/pntbiz_wms-0.0.1-SNAPSHOT.war'],
        admin                : [repository: 'pntbiz-admin2', sourceFilePath: 'pntbiz-admin2/target/pntbiz_admin2-0.0.1-SNAPSHOT.war'],
        rtls                 : [repository: 'pntibz-rtls', sourceFilePath: 'rtls.zip'],
        socket               : [repository: 'pntbiz-socket', sourceFilePath: 'socket/socket.zip'],
        oauth                : [repository: 'pntbiz-oauth', sourceFilePath: 'oauth.zip'],
        so_pm                : [repository: 'pntbiz-so-pm', sourceFilePath: 'so-pm/so-pm.zip'],
        so_ts                : [repository: 'pntbiz-so-ts', sourceFilePath: 'so-ts/so-ts.zip'],
        smart_sensing_core   : [repository: 'indoorplus-smart-sensing-core-api', sourceFilePath: ''],
        smart_sensing_service: [repository: 'indoorplus-smart-sensing-service-api', sourceFilePath: ''],
        efm_api              : [repository: 'pntbiz-raas-efm', sourceFilePath: 'efm/efm.zip'],
        scanner_management   : [repository: 'pntbiz-scanner-manager', sourceFilePath: 'pntbiz-scanner-manager'],
        rpa_agent            : [repository: 'pntbiz-semi-rpa', sourceFilePath: 'semi-rpa-agent/semi-rpa-agent.zip'],
        rpa_manager          : [repository: 'pntbiz-semi-rpa', sourceFilePath: 'semi-rpa-manager/semi-rpa-manager.zip'],
        etag                 : [repository: 'pntbiz-etag-management', sourceFilePath: 'etag.zip'],
        api_v3               : [repository: 'pntbiz-server', sourceFilePath: 'pntbiz-root/pntbiz-api/target/pntbiz_api-0.0.1-SNAPSHOT.war'],
        socket_v3            : [repository: 'pntbiz-server', sourceFilePath: 'socket/socket.zip'],
        admin_v3             : [repository: 'pntbiz-server', sourceFilePath: 'pntbiz-root/pntbiz-admin2/target/pntbiz_admin2-0.0.1-SNAPSHOT.war'],
        wms_v3               : [repository: 'pntbiz-server', sourceFilePath: 'pntbiz-root/pntbiz-wms/target/pntbiz_wms-0.0.1-SNAPSHOT.war'],
]
