<template>
<div class="playground">
  <GameMap />
</div>
</template>

<script>
import GameMap from "@/components/GameMap";

export default {
  name: "PlayGround",
  components: {
    GameMap
  }
}
</script>

<style scoped>
.playground {
  width: 1280px;
  height: 820px;
  margin: 20px auto;
  background-color: rgba(0, 0, 0, 0.74);
}
</style>