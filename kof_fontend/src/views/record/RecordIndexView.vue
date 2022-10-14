<template>
<ContentFiled>Record</ContentFiled>
</template>

<script>
import ContentFiled from "@/components/ContentFiled";
import $ from 'jquery'
import {useStore} from "vuex";

export default {
  name: "RecordIndexView",
  components: {
    ContentFiled
  },
  setup () {
    const store = useStore()

    $.ajax({
        url: "http://127.0.0.1:3000/api/profile/getlist",
        type: "get",
        headers: {
          authorization: "Bearer " + store.state.user.access_token
        },
        success(resp) {
          console.log(resp)
        }
      })
  }
}
</script>

<style scoped>

</style>