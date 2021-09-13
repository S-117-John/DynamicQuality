export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        path: '/user',
        routes: [
          {
            name: 'login',
            path: '/user/login',
            component: './user/Login',
          },
        ],
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    component: './Welcome',
  },
  {
    name:'支付数据分析',
    path: '/pay/dashboard',
    component: './module/ali/dashboard',
    access: 'wxRouteFilter',
  },
  {
    name:'单病种数据分析',
    path: 'dashboard',
    component: './module/quality/dashboard',
    access: 'qualityRouteFilter',
  },
  {
    name:'系统设置',
    path: '/sys',
    access: 'adminRouteFilter',
    routes: [
      {
        name:'部门管理',
        path: 'dept',
        component: './system/dept/index'
      },
      {
        name:'用户管理',
        path: 'user',
        component: './system/user/index'
      },
      {
        name:'系统字典',
        path: 'dict',
        component: './system/dict/index'
      },
      {
        name:'字典数据',
        path: 'dict/data',
        component: './system/dict/data',
        hideInMenu:true
      },
      {
        name:'系统参数',
        path: 'config',
        component: './system/config/index'
      },
      {
        name:'角色管理',
        path: 'role',
        component: './system/role/index'
      },
    ]
  },
  {
    name:'单病种管理',
    path: '/quality',
    access: 'qualityRouteFilter',
    routes: [
      {
        name:'指标填报',
        path: 'report',
        component: './module/quality/report',
      },
      {
        name:'数据管理',
        path: 'data',
        component: './module/quality/dataList',
      },
      {
        name:'单病种设置',
        path: 'config',
        component: './module/quality/config',
      },
      {
        name:'患者列表',
        path: 'patient',
        component: './module/quality/patient',
        hideInMenu:true
      },
      {
        name:'病种上报',
        path: 'view',
        component: './module/quality/view',
        hideInMenu:true
      },
      {
        name:'表单创建',
        path: 'form',
        component: './module/quality/createList',
      },
      {
        name:'新建表单',
        path: 'form/new',
        component: './module/quality/createForm',
        hideInMenu:true
      },
      {
        name:'编辑表单',
        path: 'form/edit',
        component: './module/quality/createForm',
        hideInMenu:true
      },


    ]
  },
  {
    name: '微信管理',
    path: '/wx',
    access: 'wxRouteFilter',
    routes: [
      {
        name: '微信公众号配置信息',
        path: 'config',
        component: './module/wx/config/index'
      },
      {
        name: '微信支付配置信息',
        path: 'pay',
        component: './module/wx/pay/index'
      },
      {
        name: '微信支付交易记录',
        path: 'trade',
        component: './module/wx/record/payRecord'
      },
      {
        name: '微信支付退款记录',
        path: 'refund',
        component: './module/wx/record/refundRecord'
      },
      {
        name: '微信订单',
        path: 'bill',
        component: './module/wx/record/payBill'
      },
    ]
  },
  {
    name: '支付宝管理',
    path: '/ali',
    access: 'wxRouteFilter',
    routes: [
      {
        name: '支付宝支付配置信息',
        path: 'pay',
        component: './module/ali/config/index'
      },
      {
        name: '支付宝支付记录',
        path: 'trade',
        component: './module/ali/record/tradeRecord'
      },
      {
        name: '支付宝退款记录',
        path: 'refund',
        component: './module/ali/record/refundRecord'
      },
      {
        name: '支付宝账单',
        path: 'bill',
        component: './module/ali/bill/aliPayBill'
      }
    ]
  },
  {
    path: '/',
    redirect: '/welcome',
  },

  // {
  //   path: '/admin',
  //   name: 'admin',
  //   icon: 'crown',
  //   access: 'canAdmin',
  //   component: './Admin',
  //   routes: [
  //     {
  //       path: '/admin/sub-page',
  //       name: 'sub-page',
  //       icon: 'smile',
  //       component: './Welcome',
  //     },
  //   ],
  // },
  // {
  //   name: 'list.table-list',
  //   icon: 'table',
  //   path: '/list',
  //   component: './TableList',
  // },


  {
    component: './404',
  },
];
