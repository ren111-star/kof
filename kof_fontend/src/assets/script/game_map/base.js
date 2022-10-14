import { AcGameObject } from '../ac_game_object/base.js';
import $ from 'jquery'
import { Kyo} from "@/assets/script/player/kyo";
import {Controller} from "@/assets/script/controller/base";
import { useStore } from "vuex";

export class GameMap extends AcGameObject {
    constructor(ctx, parent) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.$canvas =$('#canvas')
        this.$canvas.focus()
        this.width = this.parent.clientWidth
        this.height = this.parent.clientHeight
        this.$timer = $('.kof-head-timer')
        this.controller = new Controller(this.$canvas)
        this.store = useStore()
        console.log('store opponent', this.store.state.pk.opponent_username)
        console.log('store gamemap', this.store.state.pk.gamemap)
        this.players = [
            new Kyo(this, {
                id: 0,
                x: 30,
                y: 0,
                width: 30,
                height: 50,
                color: 'red'
            }),
            new Kyo(this, {
                id: 1,
                x: 230,
                y: 0,
                width: 30,
                height: 50,
                color: 'blue'
            })
        ]
        this.time_left = 60 * 1000;
    }

    start() {
    }

    update() {
        this.time_left -= this.timedelta;
        if (this.time_left < 0) {
            this.time_left = 0;

            let [a, b] = this.players;
            if (a.status !== 6 && b.status !== 6) {
                if (a.hp === b.hp) {
                    a.status = b.status = 6;
                    this.store.commit("updateLoser", "all")
                } else if (b.hp < a.hp) {
                    b.status = 6;
                    this.store.commit("updateLoser", "B")
                } else {
                    a.status = 6;
                    this.store.commit("updateLoser", "A")
                }
                a.frame_current_cnt = b.frame_current_cnt = 0;
            }
        }
        this.$timer.text(parseInt(this.time_left / 1000))

        this.render();
    }

    render() {
        this.ctx.clearRect(0, 0, this.width, this.height);
    }
}