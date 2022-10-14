import { createRouter, createWebHistory } from 'vue-router'
import NotFound from "@/views/error/NotFound";
import PkIndexView from "@/views/pk/PkIndexView";
import RecordIndexView from "@/views/record/RecordIndexView";
import RankListIndexView from "@/views/ranklist/RankListIndexView";
import UserInfoIndexView from "@/views/user/userInfo/UserInfoIndexView";
import UserAccountLoginView from "@/views/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/views/user/account/UserAccountRegisterView";
import store from '@/store/index'

const routes = [
  {
    path: '/',
    redirect: "/pk/",
    name: 'home',
    meta: {
      requestAuth: true
    }
  },{
    path: '/login/',
    name: 'login_index',
    component: UserAccountLoginView,
    meta: {
      requestAuth: false
    }
  },{
    path: '/register/',
    name: 'register_index',
    component: UserAccountRegisterView,
    meta: {
      requestAuth: false
    }
  },{
    path: '/pk/',
    name: 'pk_index',
    component: PkIndexView,
    meta: {
      requestAuth: true
    }
  },{
    path: '/404/',
    name: 'not_fount_index',
    component: NotFound,
    meta: {
      requestAuth: false
    }
  },{
    path: '/ranklist/',
    name: 'rank_list_index',
    component: RankListIndexView,
    meta: {
      requestAuth: true
    }
  },{
    path: '/userinfo/',
    name: 'userinfo_index',
    component: UserInfoIndexView,
    meta: {
      requestAuth: true
    }
  },{
    path: '/record/',
    name: 'record_index',
    component: RecordIndexView,
    meta: {
      requestAuth: true
    }
  },
  // {
  //   path: "/:catchAll(.*)",
  //   redirect: "/404/"
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.requestAuth && !store.state.user.is_login) {
    next({name: "login_index"})
  } else {
    next();
  }
})

export default router
