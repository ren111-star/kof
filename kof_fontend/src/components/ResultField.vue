<template>
  <div class="result">
    <span>Winner{{ $store.state.pk.loser==='A'?'B':'A'}}</span>
    <div>
      <button @click="restart">再来</button>
    </div>
  </div>
</template>

<script>
import {useStore} from "vuex";

export default {
  name: "ResultField",
  setup () {
    const store = useStore();

    const restart = () => {
      store.commit("updateResult", false);
      store.commit("updateLoser", "")
      store.commit("updateStatus", "matching")
      store.commit("updateOpponent", {
        username: "我的对手",
        photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
      })
    }

    return {
      restart
    }
  }
}

</script>

<style scoped>
.result {
  width: 700px;
  height: 40vh;
  background-color: rgba(0, 0, 0, 0.85);
  position: absolute;
  top: 140px;
  left: calc((100% - 700px) / 2);
  color: white;
  font-size: 100px;
  text-align: center;
  user-select: none;
}
.result button {
  font-family:OCR A Std, monospace
}
</style>