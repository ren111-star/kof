import $ from "jquery";
import jwt_decode from "jwt-decode";

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        access_token: "",
        refresh_token: "",
        is_login: false
    },
    getters: {},
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login
        },
        updateToken(state, token) {
            state.access_token = token
        },
        updateRefresh(state, token) {
            state.refresh_token = token
        },
        logout(state) {
            state.id = "";
            state.username = "";
            state.photo = "";
            state.access_token = "";
            state.refresh_token = "";
            state.is_login = false
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/api/login",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password
                },
                success(resp) {
                    context.commit("updateToken", resp.access_token);
                    context.commit("updateRefresh", resp.refresh_token);

                    const access_obj = jwt_decode(resp.access_token)
                    console.log(access_obj)
                    setInterval(() => {
                        $.ajax({
                            url: "http://127.0.0.1:3000/api/token/refresh",
                            type: "post",
                            headers: {
                                authorization: "Bearer " + context.state.refresh_token
                            },
                            success(resp) {
                                console.log("--------------------------------success update token")
                                context.commit("updateToken", resp.access_token)
                                localStorage.setItem("access_token", resp.access_token)
                            }
                        })
                    }, 5 * 60 * 1000)

                    $.ajax({
                        url: "http://127.0.0.1:3000/api/user/getinfo",
                        type: "get",
                        headers: {
                            authorization: "Bearer " + context.state.access_token
                        },
                        success(resp) {
                            context.commit("updateUser", {
                                ...resp,
                                is_login: true
                            });
                            data.success(resp)
                            console.log("抓取信息成功", resp.error_message)
                            localStorage.setItem("access_token", context.state.access_token)
                            localStorage.setItem("refresh_token", context.state.refresh_token)
                        },
                        error(resp) {
                            data.error(resp)
                        }
                    })

                },
                error(resp) {
                    data.error(resp)
                }
            })
        },
        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/api/user/getinfo",
                type: "get",
                headers: {
                    authorization: "Bearer " + context.state.access_token
                },
                success(resp) {
                    context.commit("updateUser", {
                        ...resp,
                        is_login: true
                    });
                    data.success(resp)
                    console.log("抓取信息成功", resp.error_message)
                },
                error(resp) {
                    data.error(resp)
                }
            })
        }
    }
}