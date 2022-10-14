<template>
  <content-filed>
    <div class="row">
      <div class="col-3">
        <div class="card">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width: 100%">
          </div>
          <div class="card-body" style="font-size: 20px; color: white; text-align: center" >
            {{ $store.state.user.username }}
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card">
          <div class="card_header">
            <span style="font-size: 120%; color: white">我的帖子</span>
            <button type="button" class="btn btn-success float-end" data-bs-toggle="modal"
                    data-bs-target="#add_profile">
              <i class="fas fa-magic">写帖子</i>
            </button>
            <!-- Modal -->
            <div class="modal fade" id="add_profile" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1"
                 aria-labelledby="staticBackdropLabel" aria-hidden="true">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5" id="staticBackdropLabel">写点什么~</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <div class="form-group">
                        <label for="add_profile_title">标题</label>
                        <input type="text" v-model="profileAdd.title" class="form-control" id="add_profile_title"
                               aria-describedby="emailHelp"
                        >
                      </div>
                      <div class="form-group">
                        <label for="add_profile_description">简介</label>
                        <textarea v-model="profileAdd.description" class="form-control" id="add_profile_description"
                                  rows="3"/>
                      </div>
                      <div class="form-group">
                        <label for="add_profile_description">内容</label>
                        <textarea v-model="profileAdd.content" class="form-control" id="add_profile_description"
                                  rows="8"/>
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <div class="error_message">{{ profileAdd.error_message }}</div>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" @click="profile_add">提交</button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <hr>
          <div class="card_body">
            <section class="intro">
              <div class="bg-image h-100">
                <div class="mask d-flex align-items-center h-100">
                  <div class="container">
                    <div class="row justify-content-center">
                      <div class="col-12">
                        <div class="card mask-custom">
                          <div class="card-body" style="height: 75vh">
                            <div class="table-responsive">
                              <table class="table table-borderless text-white mb-0">
                                <thead>
                                <tr>
                                  <th scope="col">标题</th>
                                  <th scope="col">创建时间</th>
                                  <th scope="col">更新时间</th>
                                  <th scope="col">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="profile in profiles" :key="profile.id">
                                  <th scope="row">{{ profile.title }}</th>
                                  <td>{{ profile.createTime }}</td>
                                  <td>{{ profile.modifyTime }}</td>
                                  <td>

                                    <button type="button" class="btn btn-outline-danger btn-rounded"
                                            data-bs-toggle="modal"
                                            :data-bs-target="'#update_profile-' + profile.id"
                                            data-mdb-ripple-color="dark" style="margin-right: 10px">修改
                                    </button>
                                    <div class="modal fade" :id="'update_profile-' + profile.id" data-bs-backdrop="static"
                                         data-bs-keyboard="false" tabindex="-1"
                                         aria-labelledby="staticBackdropLabel" aria-hidden="true">
                                      <div class="modal-dialog">
                                        <div class="modal-content">
                                          <div class="modal-header">
                                            <h1 class="modal-title fs-5" id="staticBackdropLabel">写点什么~</h1>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                          </div>
                                          <div class="modal-body">
                                            <form>
                                              <div class="form-group">
                                                <label for="update_profile_title">标题</label>
                                                <input type="text" v-model="profileAdd.title" class="form-control"
                                                       id="update_profile_title" placeholder="标题" aria-describedby="emailHelp"
                                                >
                                              </div>
                                              <div class="form-group">
                                                <label for="update_profile_description">简介</label>
                                                <textarea v-model="profileAdd.description" placeholder="简介" class="form-control"
                                                          id="update_profile_description" rows="3"/>
                                              </div>
                                              <div class="form-group">
                                                <label for="update_profile_content">内容</label>
                                                <textarea v-model="profileAdd.content" placeholder="内容" class="form-control"
                                                          id="update_profile_content" rows="8"/>
                                              </div>
                                            </form>
                                          </div>
                                          <div class="modal-footer">
                                            <div class="error_message">{{ profileAdd.error_message }}</div>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                              取消
                                            </button>
                                            <button type="button" class="btn btn-primary" @click="profile_update(profile)">提交
                                            </button>
                                          </div>
                                        </div>
                                      </div>
                                    </div>
                                    <button type="button" class="btn btn-outline-warning btn-rounded"
                                            data-mdb-ripple-color="dark" @click="profile_remove(profile)">删除
                                    </button>
                                  </td>
                                </tr>
                                </tbody>
                              </table>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
  </content-filed>
</template>

<script>
import ContentFiled from "@/components/ContentFiled";
import {useStore} from "vuex";
import $ from 'jquery'
import {ref, reactive} from "vue";
import {Modal} from 'bootstrap/dist/js/bootstrap'

export default {
  name: "UserInfoIndexView",
  components: {
    ContentFiled
  },
  setup() {
    const store = useStore();
    let profiles = ref([]);

    let profileAdd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: ""
    });

    const refresh_profile = () => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/profile/getlist",
        type: "get",
        headers: {
          authorization: "Bearer " + store.state.user.access_token
        },
        success(resp) {
          console.log(resp)
          profiles.value = resp
        }
      })
    }

    refresh_profile();

    const profile_add = () => {
      profileAdd.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/api/profile/add",
        type: "post",
        data: {
          title: profileAdd.title,
          description: profileAdd.description,
          content: profileAdd.content
        },
        headers: {
          authorization: "Bearer " + store.state.user.access_token
        },
        success(resp) {
          if (resp.error_message === "success") {
            profileAdd.title = ""
            profileAdd.description = ""
            profileAdd.content = ""
            Modal.getInstance("#add_profile").hide()
            refresh_profile()
          } else {
            profileAdd.error_message = resp.error_message
          }
        }
      })
    }

    const profile_update = (profile) => {
      profileAdd.error_message = "";
      $.ajax({
        url: "http://127.0.0.1:3000/api/profile/update",
        type: "post",
        data: {
          profile_id: profile.id,
          title: profileAdd.title,
          description: profileAdd.description,
          content: profileAdd.content
        },
        headers: {
          authorization: "Bearer " + store.state.user.access_token
        },
        success(resp) {
          if (resp.error_message === "success") {
            Modal.getInstance("#update_profile-" + profile.id).hide()
            refresh_profile()
          } else {
            profileAdd.error_message = resp.error_message
          }
        }
      })
    }

    const profile_remove = (profile) => {
      $.ajax({
        url: "http://127.0.0.1:3000/api/profile/remove",
        type: "post",
        data: {
          profile_id: profile.id
        },
        headers: {
          authorization: "Bearer " + store.state.user.access_token
        },
        success(resp) {
          if (resp.error_message === "success") {
            refresh_profile()
          }
        }
      })
    }

    return {
      profiles,
      profileAdd,
      profile_add,
      profile_remove,
      profile_update
    }

  }
}
</script>

<style scoped>
hr {
  margin: 0;
}

table td,
table th {
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}

.error_message {
  color: red;
}

.mask-custom {
  background: rgba(24, 24, 16, .2);
  border-radius: 2em;
  backdrop-filter: blur(25px);
  border: 2px solid rgba(255, 255, 255, 0.05);
  background-clip: padding-box;
  box-shadow: 10px 10px 10px rgba(46, 54, 68, 0.03);
}

.card {
  background-image: url("@/assets/images/img7.jpg");
}
</style>