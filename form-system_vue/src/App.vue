<template>
  <div id="app">
    <a-layout>
      <a-layout-header style="color: white; font-size: 20px; display: flex; align-items: center;">
        自定义表单系统
      </a-layout-header>
      <a-layout-content style="padding: 20px;">
        <router-view />
      </a-layout-content>
      <a-layout-footer style="text-align: center;">
        自定义表单系统 ©2025
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
#app {
  min-height: 100vh;
}
</style>