/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
export default function access(initialState) {
  const { currentUser } = initialState || {};
  return {
    canAdmin: currentUser && currentUser.access === 'admin',
    adminRouteFilter: () => currentUser?currentUser.access?.indexOf("admin")!==-1:false, // 只有管理员可访问
    qualityRouteFilter: () => currentUser?currentUser.access?.indexOf("quality")!==-1:false, // 访问单病种
    wxRouteFilter: () => currentUser?currentUser.access?.indexOf("wx")!==-1:false, // 访问单病种
  };
}
