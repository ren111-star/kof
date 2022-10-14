<template>
  <div id="kof" ref="parent" class="game-map">
    <div class="kof-head">
      <div class="kof-head-hp-0">
        <div>
          <div></div>
        </div>
      </div>
      <div class="kof-head-timer">60</div>
      <div class="kof-head-hp-1">
        <div>
          <div></div>
        </div>
      </div>
    </div>
    <canvas id="canvas" ref="canvas" tabindex=0></canvas>
    <ResultField v-if="$store.state.pk.result" />
  </div>
</template>

<script>
import {GameMap} from "@/assets/script/game_map/base";
import {ref, onMounted} from "vue";
import {useStore} from "vuex";
import ResultField from "@/components/ResultField";

export default {
  name: "GameMap",
  components: {
    ResultField
  },
  setup() {
    let parent = ref(null)
    let canvas = ref(null)
    const store = useStore()

    onMounted(() => {
      store.commit("updateGameObject",
          new GameMap(canvas.value.getContext('2d'), parent.value)
      )
    })



    return {
      parent,
      canvas
    }
  }
}
</script>

<style scoped>
#kof {
  width: 1280px;
  height: 820px;

  background-image: url('/src/assets/images/0.gif');
  background-size: 200% 100%;
  background-position: top;
  position: absolute;
}

#canvas {
  width: 1280px;
  height: 820px;
}

#kof > .kof-head {
  width: 100%;
  height: 80px;
  position: absolute;
  top: 0;
  display: flex;
  align-items: center;
}

#kof > .kof-head > .kof-head-hp-0 {
  height: 40px;
  width: calc(50% - 60px);
  margin-left: 20px;
  border: 3px cornsilk solid;
  box-sizing: border-box;
}

#kof > .kof-head > .kof-head-timer {
  height: 60px;
  width: 80px;
  background-color: rgb(49, 48, 117);
  box-sizing: border-box;
  color: wheat;
  font-size: 30px;
  text-align: center;
  user-select: none;
  line-height: 60px;
  /* border: 2px rgb(29, 29, 29) solid; */
}

#kof > .kof-head > .kof-head-hp-1 {
  height: 40px;
  width: calc(50% - 60px);
  margin-right: 20px;
  border: 3px cornsilk solid;
  box-sizing: border-box;
}

#kof > .kof-head > .kof-head-hp-0 > div {
  height: 100%;
  width: 100%;
  float: right;
  background-color: rgba(17, 17, 17, 0.5);
}

#kof > .kof-head > .kof-head-hp-0 > div > div {
  height: 100%;
  width: 100%;
  float: right;
  background-color: rgb(206, 32, 32);
}

#kof > .kof-head > .kof-head-hp-1 > div {
  height: 100%;
  width: 100%;
  background-color: rgba(17, 17, 17, 0.712);
}

#kof > .kof-head > .kof-head-hp-1 > div > div {
  height: 100%;
  width: 100%;
  background-color: rgb(206, 32, 32);
}
</style>