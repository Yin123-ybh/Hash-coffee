import Vue from 'vue'
import Router from 'vue-router'
import 'normalize.css'
import ElementUI from 'element-ui'
import SvgIcon from 'vue-svgicon'
import VueAreaLinkage from 'vue-area-linkage'
import moment from 'moment'
import '@/styles/element-variables.scss'
import '@/styles/index.scss'
import '@/styles/home.scss'
import 'vue-area-linkage/dist/index.css'

import * as echarts from 'echarts'
// 瑞吉外卖样式表
import '@/styles/newRJWMsystem.scss'
import '@/styles/icon/iconfont.css'
import '@/styles/force-navbar-coffee.css'
import '@/utils/force-navbar-coffee.js'
import App from '@/App.vue'
import store from '@/store'
import router from '@/router'
import '@/icons/components'
import '@/permission'
import { checkProcessEnv } from '@/utils/common'

Vue.use(ElementUI)
Vue.use(VueAreaLinkage)
Vue.use(SvgIcon, {
  'tagName': 'svg-icon',
  'defaultWidth': '1em',
  'defaultHeight': '1em'
})

Vue.config.productionTip = false
Vue.prototype.moment = moment
Vue.prototype.$checkProcessEnv = checkProcessEnv
const routerPush = Router.prototype.push
Router.prototype.push = function push(location) {
 return routerPush.call(this, location).catch(error => error)
}
Vue.prototype.$echarts = echarts

// 强制显示导航栏并设置蓝色背景
Vue.mixin({
  mounted() {
    this.$nextTick(() => {
      const navbar = document.querySelector('.navbar') as HTMLElement
      if (navbar) {
        navbar.style.display = 'block'
        navbar.style.visibility = 'visible'
        navbar.style.opacity = '1'
        navbar.style.background = '#8B4513'
        navbar.style.backgroundColor = '#8B4513'
        navbar.style.backgroundImage = 'none'
        navbar.style.backgroundAttachment = 'scroll'
        navbar.style.backgroundClip = 'border-box'
        navbar.style.backgroundOrigin = 'padding-box'
        navbar.style.backgroundPosition = '0% 0%'
        navbar.style.backgroundRepeat = 'repeat'
        navbar.style.backgroundSize = 'auto'
        navbar.style.position = 'fixed'
        navbar.style.top = '0'
        navbar.style.left = '0'
        navbar.style.right = '0'
        navbar.style.zIndex = '1000'
        navbar.style.height = '60px'

        // 强制移除任何可能覆盖的样式
        navbar.removeAttribute('style')
        navbar.setAttribute('style', 'background: #8B4513 !important; background-color: #8B4513 !important; background-image: none !important; display: block !important; visibility: visible !important; opacity: 1 !important; position: fixed !important; top: 0 !important; left: 0 !important; right: 0 !important; z-index: 1000 !important; height: 60px !important;')
      }
    })
  }
})

// 持续强制设置导航栏颜色
setInterval(() => {
  const navbar = document.querySelector('.navbar') as HTMLElement
  if (navbar) {
    navbar.style.background = '#8B4513'
    navbar.style.backgroundColor = '#8B4513'
    navbar.style.backgroundImage = 'none'
    navbar.style.backgroundAttachment = 'scroll'
    navbar.style.backgroundClip = 'border-box'
    navbar.style.backgroundOrigin = 'padding-box'
    navbar.style.backgroundPosition = '0% 0%'
    navbar.style.backgroundRepeat = 'repeat'
    navbar.style.backgroundSize = 'auto'
    navbar.setAttribute('data-bg-color', '#8B4513')
  }
  
  // 强制覆盖所有可能的navbar元素
  const allNavbars = document.querySelectorAll('.navbar, .navbar-blue, .navbar-coffee, [class*="navbar"]')
  allNavbars.forEach((el: HTMLElement) => {
    el.style.background = '#8B4513'
    el.style.backgroundColor = '#8B4513'
    el.style.backgroundImage = 'none'
    el.setAttribute('data-bg-color', '#8B4513')
  })
}, 50)

new Vue({
  router,
  store,
  'render': (h) => h(App)
}).$mount('#app')
