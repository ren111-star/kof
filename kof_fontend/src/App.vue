<template>
  <NavBar/>
  <router-view/>
</template>

<script>
import NavBar from "@/components/NavBar";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap";
import {useStore} from "vuex";
import router from "@/router";
import $ from "jquery";

export default {
  components: {
    NavBar
  },
  setup() {
    console.log("flush")
    const store = useStore();
    let access_token = localStorage.getItem("access_token");
    let refresh_token = localStorage.getItem("refresh_token")
    if (access_token) {
      console.log("获取 localStorage 的 access_token 成功")
      store.commit("updateToken", access_token)
      store.commit("updateRefresh", refresh_token)
      store.dispatch("getinfo", {
        success(resp) {
          console.log(resp)
          router.push({name: 'home'})
          setInterval(() => {
            $.ajax({
              url: "http://127.0.0.1:3000/api/token/refresh",
              type: "post",
              headers: {
                authorization: "Bearer " + localStorage.getItem("refresh_token")
              },
              success(resp) {
                console.log("--------------------------------success update token")
                store.commit("updateToken", resp.access_token)
                localStorage.setItem("access_token", resp.access_token)
              }
            })
          }, 5 * 60 * 1000)
        },
        error(resp) {
          console.log(resp)
        }
      })

    }
  }
}
</script>

<style>
body {
  background-image: url("@/assets/images/1-13121H05009.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  background-attachment: fixed;
}
</style>
