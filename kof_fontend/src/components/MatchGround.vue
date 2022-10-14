<template>
  <div class="match_ground">
    <div class="row">
      <div class="col-6">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="">
          </div>
          <div class="card-header">
            {{ $store.state.user.username }}
          </div>
        </div>
      </div>
      <div class="col-6">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.pk.opponent_photo" alt="">
          </div>
          <div class="card-header">
            {{ $store.state.pk.opponent_username }}
          </div>
        </div>
      </div>
      <div class="col-12 btn-group-lg btn-light" style="text-align: center; margin-top: 10vh">
        <button type="button" class="btn btn-dark" @click="click_match_btn">{{ match_btn_info }}</button>
      </div>
    </div>
  </div>
</template>

<script>
import {ref} from "vue";
import { useStore } from "vuex";

export default {
  name: "MatchGround",
  setup () {
    let match_btn_info = ref("matching")
    const store = useStore()

    const click_match_btn = () => {
      if (match_btn_info.value === "matching") {
        match_btn_info.value = "cancel"
        store.state.pk.socket.send(JSON.stringify({
          event: "start_matching"
        }))
      } else {
        match_btn_info.value = "matching"
        store.state.pk.socket.send(JSON.stringify({
          event: "stop_matching"
        }))
      }
    }

    return {
      match_btn_info,
      click_match_btn
    }
  }
}
</script>

<style scoped>
.match_ground {
  width: 60vw;
  height: 80vh;
  margin: 40px auto;
  background-image: url("@/assets/images/matching.jpg");
  background-repeat: no-repeat;
}

.card-body {
  text-align: center;
  background-color: rgba(0, 0, 0, 0.6);
}

.card {
  background-color: rgba(0, 0, 0, 0.3);
}

.card-body > img {
  border-radius: 50%;
  width: 17vw;
  margin-top: 5vh;
}

.card-header {
  text-align: center;
  height: 5vh;
  font-size: 20px;
  color: white;
  font-weight: bolder;
}
</style>