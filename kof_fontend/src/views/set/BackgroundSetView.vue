<template>
  <ContentFiled>
    <div class="row">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <div class="text-center" ref="abc">背景</div>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card-body">
            <div class="flex-wrap d-flex">
              <div ref="back" v-for="(conTop, i) of imgs" :key="i"
                   :style="conTop"
                   @click="select_back(i)" @mouseover="back_card(i)"
                   style="display: block" class="back_card">
                <div class="sel" :class="{'colordisplay': display}" :ref="'abc_' + i"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </ContentFiled>
</template>

<script>
import ContentFiled from "@/components/ContentFiled";
import {useStore} from "vuex";
import router from "@/router";

export default {
  name: "BackgroundSetView",
  components: {
    ContentFiled
  },
  setup() {
    const imgs = []
    const store = useStore()

    for (let size = 0; size <= 22; size++) {
      imgs.push(
          {
            backgroundImage: 'url(' + require(`/src/assets/images/background/${size}.gif`) + ')',
          }
      )
    }

    const select_back = (idx) => {
      console.log(idx)
      store.commit("updateBackgroundImage", `${idx}.gif`)
      alert("切换成功")
      router.push({name: 'userinfo_index'})
    }

    return {
      imgs,
      select_back,
    }
  },
  methods: {

  },
  data() {
    let display = true
    let back_card = () => {
      display = false
    }
    return {
      display,
      back_card
    }
  }
}
</script>

<style scoped>
.back_card {
  width: 14vw;
  height: 15vh;
  background-size: 200% 100%;
  background-position: top;
  background-repeat: no-repeat;
  margin-right: 15px;
  margin-bottom: 15px;
}

.back_card:hover {
  border: 1px solid black;
  box-shadow: 2px 2px 4px #000000;
}

.sel {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
}

.colordisplay {
  display: none;
}
</style>