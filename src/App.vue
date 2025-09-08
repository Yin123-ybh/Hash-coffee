<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  name: 'App'
})
export default class extends Vue {}
</script>

<style>
/* 强制设置顶部导航栏为蓝色背景 */
.navbar,
.navbar-blue,
div.navbar {
  background: #8B4513 !important;
  background-color: #8B4513 !important;
  background-image: none !important;
  display: block !important;
  visibility: visible !important;
  opacity: 1 !important;
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  right: 0 !important;
  z-index: 1000 !important;
  height: 60px !important;
}

/* 使用最高优先级的选择器 */
html body #app .app-wrapper .main-container .navbar,
html body #app .app-wrapper .main-container .navbar.navbar-blue,
html body #app .app-wrapper .main-container div.navbar {
  background: #8B4513 !important;
  background-color: #8B4513 !important;
  background-image: none !important;
  background-attachment: scroll !important;
  background-clip: border-box !important;
  background-origin: padding-box !important;
  background-position: 0% 0% !important;
  background-repeat: repeat !important;
  background-size: auto !important;
}

/* 使用CSS变量强制设置 */
:root {
  --navbar-bg-color: #8B4513;
}

.navbar,
.navbar-blue,
.navbar-coffee,
div.navbar,
[class*="navbar"] {
  background: var(--navbar-bg-color) !important;
  background-color: var(--navbar-bg-color) !important;
  background-image: none !important;
}

/* 使用最高优先级的选择器强制覆盖 */
html body #app .app-wrapper .main-container .navbar,
html body #app .app-wrapper .main-container .navbar.navbar-blue,
html body #app .app-wrapper .main-container .navbar.navbar-coffee,
html body #app .app-wrapper .main-container div.navbar,
html body #app .app-wrapper .main-container [class*="navbar"] {
  background: #8B4513 !important;
  background-color: #8B4513 !important;
  background-image: none !important;
  background-attachment: scroll !important;
  background-clip: border-box !important;
  background-origin: padding-box !important;
  background-position: 0% 0% !important;
  background-repeat: repeat !important;
  background-size: auto !important;
}

/* 使用属性选择器强制覆盖 */
[class*="navbar"][style*="background"] {
  background: #8B4513 !important;
  background-color: #8B4513 !important;
}

/* 使用data属性选择器 */
[data-bg-color] {
  background: #8B4513 !important;
  background-color: #8B4513 !important;
}
</style>