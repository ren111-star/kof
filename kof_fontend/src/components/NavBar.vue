<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <router-link class="navbar-brand" :to="{name: 'home'}">拳皇</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name === 'pk_index' ? 'nav-link active' : 'nav-link'" aria-current="page" :to="{name: 'home'}">游戏</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name === 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对局列表</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name === 'rank_list_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'rank_list_index'}">排行榜</router-link>
        </li>
      </ul>

      <ul class="navbar-nav mb-2 " v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
            <li><router-link class="dropdown-item" :to="{name: 'userinfo_index'}">我的空间</router-link></li>
            <li><a class="dropdown-item" @click="logout" href="#">退出</a></li>
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav mb-2 " v-else >
        <li class="nav-item dropdown">
          <router-link class="nav-link" :to="{name: 'login_index'}" id="navbarDropdownMenuLink" role="button" >
            登录
          </router-link>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from "vue";
import {useStore} from "vuex";
import router from "@/router";

export default {
  name: "NavBar",
  setup () {
    const route = useRoute();
    let route_name = computed(() => route.name)
    const store = useStore();

    const logout = () => {
      store.commit("logout")
      router.push({name: 'login_index'})
      localStorage.removeItem("access_token")
    }

    return {
      route_name,
      logout
    }
  }
}
</script>

<style scoped>
nav {
  user-select: none;
}
.navbar {
  min-width: 1280px;
}
</style>