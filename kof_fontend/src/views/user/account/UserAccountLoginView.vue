<template>
    <section class="vh-80">
      <div class="container py-5 h-100">
        <div class="row d-flex align-items-center justify-content-center h-100">
          <div class="col-md-8 col-lg-7 col-xl-6">
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg"
                 class="img-fluid" alt="Phone image">
          </div>
          <div class="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
            <form @submit.prevent="login">
              <!-- Email input -->
              <div class="form-outline mb-4">
                <input type="text" id="form1Example13" v-model="username" class="form-control form-control-lg"/>
                <label class="form-label" for="form1Example13">username</label>
              </div>

              <!-- Password input -->
              <div class="form-outline mb-4">
                <input type="password" v-model="password" id="form1Example23" class="form-control form-control-lg"/>
                <label class="form-label" for="form1Example23">Password</label>
              </div>

              <div class="d-flex justify-content-around align-items-center mb-4">
                <!-- Checkbox -->
                <div class="form-check">
                  <input class="form-check-input" type="checkbox" value="" id="form1Example3" checked/>
                  <label class="form-check-label" for="form1Example3"> Remember me </label>
                </div>
                <a href="#!">Forgot password?</a>
              </div>
              <div class="error_message">{{ error_message }}</div>
              <!-- Submit button -->
              <button type="submit" class="btn btn-primary btn-lg btn-block">Sign in</button>


              <div class="divider d-flex align-items-center my-4">
                <p class="text-center fw-bold mx-3 mb-0 text-muted">OR</p>
              </div>

              <a class="btn btn-primary btn-lg btn-block" style="background-color: #55acee"
                 role="button">
                <i class="fab fa-twitter me-2"></i><router-link class="register" :to="{name:'register_index'}">Don't have an account yet ?</router-link> </a>

            </form>
          </div>
        </div>
      </div>
    </section>
</template>

<script>
import {ref} from "vue";
import {useStore} from 'vuex'
import router from "@/router";

export default {
  name: "UserAccountLoginView",
  setup() {
    let username = ref('');
    let password = ref('');
    let error_message = ref('');

    const store = useStore();

    const login = () => {
      store.dispatch("login", {
        username: username.value,
        password: password.value,
        success(resp) {
          console.log("回调函数------------")
          console.log(resp)
          if (resp.error_message === "success") {
            router.push({name: 'home'})
          }
        },
        error(resp) {
          console.log(resp)
          error_message.value = "用户名或密码错误"
        }
      })
    }


    return {
      username,
      password,
      error_message,
      login
    }
  }
}
</script>

<style scoped>
.divider:after,
.divider:before {
  content: "";
  flex: 1;
  height: 1px;
  background: #eee;
}

.vh-80 {
  height: 86vh;
}
button {
  width: 100%;
}
label {
  color: #ffffff;
  font-size: 15px;
}

.error_message {
  color: red;
  min-height: 25px
}

.register {
  color: white;
  text-decoration: none;
}
</style>