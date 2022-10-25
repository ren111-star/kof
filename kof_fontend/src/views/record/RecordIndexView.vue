<template>
  <ContentFiled>
    <table class="table table-striped">
      <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">First</th>
        <th scope="col">Last</th>
        <th scope="col">Handle</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <th scope="row">1</th>
        <td>Mark</td>
        <td>Otto</td>
        <td>@mdo</td>
      </tr>
      <tr>
        <th scope="row">2</th>
        <td>Jacob</td>
        <td>Thornton</td>
        <td>@fat</td>
      </tr>
      <tr>
        <th scope="row">3</th>
        <td>Larry</td>
        <td>the Bird</td>
        <td>@twitter</td>
      </tr>
      </tbody>
    </table>
  </ContentFiled>
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
  setup() {
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