export default {
    state: {
        status: "matching",  // matching < --- > playing
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        result: false,
        gamemap: "",
        loser: "",
        a_id: 0,
        a_x: 0,
        a_y: 0,
        b_id: 0,
        b_x: 0,
        b_y: 0,
        gameObject: null,
    },
    getters: {},
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket
        },
        updateResult(state, result) {
          state.result = result
        },
        updateLoser(state, loser) {
            state.loser = loser
        },
        updateOpponent (state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo
        },
        updateStatus (state, status) {
            state.status = status
        },
        updateGame(state, game) {
            state.gamemap = game.game_map;
            state.a_id = game.a_id;
            state.a_x = game.a_x;
            state.a_y = game.a_y;
            state.b_id = game.b_id;
            state.b_x = game.b_x;
            state.b_y = game.b_y
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
        }
    },
    actions: {

    }
}