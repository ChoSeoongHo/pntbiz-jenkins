return [
        'dev-10': [
                description: "Mercury",
                ip: '175.45.205.235',
                playbook: '/etc/ansible/server-10/playbook.yml',
                inventory: '/etc/ansible/server-10/hosts-server-10',
                suffix: 'server-10'
        ],
        'dev-20': [
                description: "Venus",
                ip: '175.45.205.240',
                playbook: '/etc/ansible/server-20/playbook.yml',
                inventory: '/etc/ansible/server-20/hosts-server-20',
                suffix: 'server-20'
        ],
]
