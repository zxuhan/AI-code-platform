<template>
  <a-layout class="basic-layout">
    <!-- Top navigation bar -->
    <GlobalHeader />
    <!-- Main content -->
    <a-layout-content class="main-content">
      <router-view />
    </a-layout-content>
    <!-- Footer -->
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
import GlobalFooter from '@/components/GlobalFooter.vue'
</script>

<style scoped>
.basic-layout {
  background: none;
}

.main-content {
  width: 100%;
  padding: 0;
  background: none;
  margin: 0;
}
</style>
