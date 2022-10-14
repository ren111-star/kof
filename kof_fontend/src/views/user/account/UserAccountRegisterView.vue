<template>
  <!-- Section: Design Block -->
  <section class="text-center text-lg-start">


    <!-- Jumbotron -->
    <div class="container py-4">
      <div class="row g-0 align-items-center">
        <div class="col-lg-6 mb-5 mb-lg-0">
          <div class="card cascading-right" style="
            background: hsla(0, 0%, 100%, 0.55);
            backdrop-filter: blur(30px);
            ">
            <div class="card-body p-5 shadow-5 text-center">
              <h2 class="fw-bold mb-5">Sign up now</h2>
              <form @submit.prevent="register">
                <!-- 2 column grid layout with text inputs for the first and last names -->
                <div class="row">
                  <div class="col-md-6 mb-4">
                    <div class="form-outline">
                      <input type="text" id="form3Example1" v-model="username" class="form-control"/>
                      <label class="form-label" for="form3Example1"> username </label>
                    </div>
                  </div>
                  <div class="col-md-6 mb-4">
                    <div class="form-outline">
                      <input type="text" v-model="name" id="form3Example2" class="form-control"/>
                      <label class="form-label" for="form3Example2"> Full name </label>
                    </div>
                  </div>
                </div>

                <!-- Email input -->
                <div class="form-outline mb-4">
                  <input type="password" v-model="password" id="form3Example3" class="form-control"/>
                  <label class="form-label" for="form3Example3">Password</label>
                </div>

                <!-- Password input -->
                <div class="form-outline mb-4">
                  <input type="password" v-model="confirmedPassword" id="form3Example4" class="form-control"/>
                  <label class="form-label" for="form3Example4">Password confirmed</label>
                </div>

                <!-- Checkbox -->
                <div class="form-check d-flex justify-content-center mb-4">
                  <input class="form-check-input me-2" type="checkbox" value="" id="form2Example33" checked/>
                  <label class="form-check-label" for="form2Example33">
                    Subscribe to our newsletter
                  </label>
                </div>

                <!-- Submit button -->
                <button type="submit" class="btn btn-primary btn-block mb-4">
                  Sign up
                </button>
                <div class="error_message">{{ error_message }}</div>

                <!-- Register buttons -->
                <div class="text-center">
                  <p>or sign up with:</p>
                  <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-facebook-f"></i>
                  </button>

                  <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-google"></i>
                  </button>

                  <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-twitter"></i>
                  </button>

                  <button type="button" class="btn btn-link btn-floating mx-1">
                    <i class="fab fa-github"></i>
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <div class="col-lg-6 mb-5 mb-lg-0">
          <img src="https://mdbootstrap.com/img/new/ecommerce/vertical/004.jpg" class="w-100 rounded-4 shadow-4"
               alt=""/>
        </div>
      </div>
    </div>
    <!-- Jumbotron -->
  </section>
  <!-- Section: Design Block -->
</template>

<script>
import {ref} from "vue";
import {useStore} from "vuex";
import router from "@/router";
import $ from 'jquery'

export default {
  name: "UserAccountRegisterView",
  setup() {
    const store = useStore();
    let name = ref('');
    let username = ref('')
    let password = ref('')
    let confirmedPassword = ref('')
    let error_message = ref('')

    const register = () => {
      $.ajax({
        url: 'http://127.0.0.1:3000/api/register',
        type: 'post',
        data: {
          name: name.value,
          username: username.value,
          password: password.value,
          confirmedPassword: confirmedPassword.value,
        },
        headers: {
          authorization: "Bearer " + store.state.user.access_token,
        },
        success(resp) {
          console.log(resp)
          if (resp.error_message === "success") {
            router.push({name: 'home'})
          } else
            error_message.value = resp.error_message
        },
        error(resp) {
          console.log(resp)
        }
      })
    }

    return {
      username,
      password,
      name,
      confirmedPassword,
      error_message,
      register
    }
  }
}
</script>

<style scoped>
.cascading-right {
  margin-right: -50px;
}

@media (max-width: 991.98px) {
  .cascading-right {
    margin-right: 0;
  }
}

.error_message {
  color: red;
  min-height: 25px;
}
</style>