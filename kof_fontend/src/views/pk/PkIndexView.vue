<template>
  <PlayGround v-if="$store.state.pk.status === 'playing'"/>
  <MatchGround v-if="$store.state.pk.status === 'matching'"/>
</template>

<script>
import PlayGround from "@/components/PlayGround";
import {onMounted, onUnmounted} from "vue";
import {useStore} from "vuex";
import MatchGround from "@/components/MatchGround";

export default {
  name: "PkIndexView",
  components: {
    MatchGround,
    PlayGround
  },
  setup() {
    const store = useStore();
    console.log(store.state.user.id)
    const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.access_token}`
    let socket = null;
    onMounted(() => {
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
      })
      socket = new WebSocket(socketUrl);

      store.commit("updateSocket", socket)

      socket.onopen = () => {
        console.log("connected")
      }
      store.commit("updateResult", false)
      socket.onmessage = msg => {
        const data = JSON.parse(msg.data);
        if (data.event === "success_matching") {
          console.log("success_matching")
          store.commit("updateOpponent", {
            username: data.opponent_username,
            photo: data.opponent_photo
          })
          store.commit("updateGame", data.game)
          setTimeout(() => {
            store.commit("updateStatus", "playing")
          }, 500)
        } else if (data.event === "move") {
          const game = store.state.pk.gameObject
          const [player0, player1] = game.players
          if (data.opponent_id === store.state.pk.a_id) {
            let set = data.opponent_operate
            console.log(set, "<-------set")
            player0.set_direction(set)
          } else if (data.opponent_id === store.state.pk.b_id) {
            let set = data.opponent_operate
            console.log(set, "<-------set")
            player1.set_direction(set)
          }
          console.log(data)
        } else if (data.event === "result") {
          if (store.state.pk.status === "playing") {
            store.commit("updateLoser", data.loser)
            store.commit("updateResult", true)
          }
        } else if (data.event === "if_exit") {
          if (!store.state.pk.status.result) {
            socket.send(JSON.stringify({
              event: "if_exit",
              id: store.state.user.id
            }))
          }
        }
      }

      socket.onclose = () => {
        console.log("disconnected")
      }
    })
    onUnmounted(() => {
      socket.close()
    })
  }
}
</script>

<style scoped>

</style>