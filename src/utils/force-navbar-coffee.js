// 强制设置顶部导航栏为咖啡色的JavaScript脚本

(function() {
  'use strict';
  
  // 强制设置navbar背景色的函数
  function forceNavbarCoffee() {
    // 查找所有可能的navbar元素
    const selectors = [
      '.navbar',
      '.navbar-blue', 
      '.navbar-coffee',
      'div.navbar',
      '[class*="navbar"]',
      '#navbar',
      '#navbar-coffee',
      '#navbar-blue'
    ];
    
    selectors.forEach(selector => {
      const elements = document.querySelectorAll(selector);
      elements.forEach(element => {
        if (element) {
          // 设置内联样式
          element.style.setProperty('background', '#8B4513', 'important');
          element.style.setProperty('background-color', '#8B4513', 'important');
          element.style.setProperty('background-image', 'none', 'important');
          element.style.setProperty('background-attachment', 'scroll', 'important');
          element.style.setProperty('background-clip', 'border-box', 'important');
          element.style.setProperty('background-origin', 'padding-box', 'important');
          element.style.setProperty('background-position', '0% 0%', 'important');
          element.style.setProperty('background-repeat', 'repeat', 'important');
          element.style.setProperty('background-size', 'auto', 'important');
          
          // 设置data属性
          element.setAttribute('data-bg-color', '#8B4513');
          element.setAttribute('data-forced-coffee', 'true');
        }
      });
    });
  }
  
  // 立即执行
  forceNavbarCoffee();
  
  // DOM加载完成后执行
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', forceNavbarCoffee);
  }
  
  // 页面完全加载后执行
  window.addEventListener('load', forceNavbarCoffee);
  
  // 定期执行，确保样式不被覆盖
  setInterval(forceNavbarCoffee, 100);
  
  // 监听DOM变化，当有新的navbar元素添加时自动设置
  const observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
      if (mutation.type === 'childList') {
        mutation.addedNodes.forEach(function(node) {
          if (node.nodeType === 1) { // Element node
            if (node.classList && (
              node.classList.contains('navbar') ||
              node.classList.contains('navbar-blue') ||
              node.classList.contains('navbar-coffee') ||
              node.className.includes('navbar')
            )) {
              forceNavbarCoffee();
            }
          }
        });
      }
    });
  });
  
  // 开始观察
  observer.observe(document.body, {
    childList: true,
    subtree: true
  });
  
  // 导出函数供外部调用
  window.forceNavbarCoffee = forceNavbarCoffee;
  
})();
