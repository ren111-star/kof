import {AcGameObject} from '../ac_game_object/base.js';
import $ from 'jquery'

export class Player extends AcGameObject {
    constructor(root, info) {
        super();

        this.root = root;
        this.id = info.id;
        this.x = info.x;
        this.y = info.y;
        this.width = info.width;
        this.height = info.height;
        this.color = info.color;
        this.store = root.store;
        this.result = "none";
        this.rend_count = 0;

        this.play_used = info.play_used;

        this.direction = 1;

        this.vx = 0;
        this.vy = 0;

        this.speedx = 100;  // 水平速度
        this.speedy = -200;  // 跳起的初始速度

        this.gravity = 7;

        this.ctx = this.root.ctx;
        // this.pressed_keys = this.root.controller.pressed_keys;

        this.status = 3; // 0: idle, 1: 向前 2: 向后 3: 跳跃 4：攻击 5：被打 6：死亡

        this.frame_current_cnt = 0; // 渲染次数
        this.animations = new Map();

        this.pressed_keys = this.root.controller.pressed_keys;
        this.opponent_keys = new Set()

        this.$hp = $(`.kof-head-hp-${this.id} > div`)

        this.hp = 100;
        this.lastSet = new Set();
    }

    set_direction(new_keys) {
        this.opponent_keys = new Set(JSON.parse(JSON.stringify([...new_keys])))
    }

    print_count () {
        let data = this
        setInterval(function () {
            console.log(data.rend_count)
        }, 1000)
    }

    start() {
        this.print_count();
        console.log(typeof this.store.state.user.id, "<-----这是user id")
        console.log(typeof this.store.state.pk.a_id, "<-----这是aid")
        console.log(typeof this.store.state.pk.b_id, "<-----这是bid")
        console.log(this.store.state.user.id === this.store.state.pk.a_id ? "我是A" : "我不是A");
        console.log(this.store.state.user.id === this.store.state.pk.b_id ? "我是B" : "我不是B");
    }

    update_controller() {
        const isSameSet = (s1, s2) => {
            const isSame = (a, b) => {
                const values = [...a];
                for (let val of values) {
                    if (!b.has(val)) return false;
                }
                return true;
            }

            return isSame(s1, s2) && isSame(s2, s1)
        }

        if (!isSameSet(this.lastSet, this.pressed_keys)) {
            const direction = [...this.pressed_keys]
            this.store.state.pk.socket.send(JSON.stringify({
                event: "move",
                id: this.store.state.user.id,
                direction: direction
            }))
        }

        this.lastSet = new Set(JSON.parse(JSON.stringify([...this.pressed_keys])))

        let w, a, d, space;

        if (parseInt(this.store.state.user.id) === this.store.state.pk.a_id) {
            if (this.id === 0) {
                w = this.pressed_keys.has('w');
                a = this.pressed_keys.has('a')
                d = this.pressed_keys.has('d');
                space = this.pressed_keys.has(' ')
            }
            if (this.id === 1) {
                w = this.opponent_keys.has('w');
                a = this.opponent_keys.has('a')
                d = this.opponent_keys.has('d');
                space = this.opponent_keys.has(' ')
            }
        } else if (parseInt(this.store.state.user.id) === this.store.state.pk.b_id) {

            if (this.id === 1) {
                w = this.pressed_keys.has('w');
                a = this.pressed_keys.has('a')
                d = this.pressed_keys.has('d');
                space = this.pressed_keys.has(' ')
            }
            if (this.id === 0) {
                w = this.opponent_keys.has('w');
                a = this.opponent_keys.has('a')
                d = this.opponent_keys.has('d');
                space = this.opponent_keys.has(' ')
            }
        }

        if (this.status === 0 || this.status === 1 || this.status === 2) {
            if (space) {
                this.frame_current_cnt = 0;
                this.status = 4;
                this.vx = 0
            } else if (w) {
                this.frame_current_cnt = 0;
                if (d) {
                    this.vx = this.speedx;
                } else if (a) {
                    this.vx = -this.speedx
                } else {
                    this.vx = 0;
                }
                this.vy = this.speedy
                this.status = 3;
            } else if (d) {
                this.vx = this.speedx;
                this.status = 1;
            } else if (a) {
                this.vx = -this.speedx;
                this.status = 1;
            } else {
                this.vx = 0;
                this.status = 0
            }
        }
    }

    update_move() {
        this.vy += this.gravity;
        if (this.status === 6 && this.vx !== 0) {
            if (this.vx > 0) this.vx = Math.max(this.vx - 3, 0)
            else this.vx = Math.min(this.vx + 3, 0)
        }
        this.x += this.vx * this.timedelta / 1000;
        this.y += this.vy * this.timedelta / 1000;

        if (this.y > 90) {
            this.y = 90;
            this.vy = 0;
            if (this.status === 3) this.status = 0
        }

        if (this.x < 0) {
            this.x = 0;
        } else if (this.x + this.width > this.root.$canvas.width() / 4.25) {
            this.x = this.root.$canvas.width() / 4.25 - this.width
        }
    }

    update_direction() {
        if (this.status === 6) return;
        let players = this.root.players;
        if (players[0] && players[1]) {
            let me = this, you = players[1 - this.id];
            if (me.x < you.x) me.direction = 1;
            else me.direction = -1
        }
    }

    is_collision(r1, r2) {
        if (Math.max(r1.x1, r2.x1) > Math.min(r1.x2, r2.x2))
            return false;
        else if (Math.max(r1.y1, r2.y1) > Math.min(r1.y2, r2.y2))
            return false;
        return true;
    }

    is_attack() {
        if (this.status === 6) return;
        this.status = 5;
        this.frame_current_cnt = 0;
        this.hp -= 14;
        this.$hp.animate({
            width: this.$hp.parent().width() * this.hp / 100
        }, 'slow')
        this.$hp.children().animate({
            width: this.$hp.parent().width() * this.hp / 100
        }, 'quick')
        if (this.hp <= 0) {
            if (this.id === 1) {
                this.store.commit("updateLoser", "B")
            } else if (this.id === 0) {
                this.store.commit("updateLoser", "A")
            }
            // 发送游戏结束信息
            if (parseInt(this.store.state.user.id) === this.store.state.pk.a_id) {
                console.log("我是", this.store.state.user.id)
                if (this.id === 0) {
                    console.log(`I am ${this.id}, and I am loser`)
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "result",
                        loser_id: this.store.state.user.id
                    }))
                }
            }
            if (parseInt(this.store.state.user.id) === this.store.state.pk.b_id) {
                console.log("我是", this.store.state.user.id)
                if (this.id === 1) {
                    console.log(`I am ${this.id}, and I am loser`)
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "result",
                        loser_id: this.store.state.user.id
                    }))
                }
            }
            this.hp = 0;
            this.frame_current_cnt = 0;
            this.status = 6;
        }
    }

    update_attack() {
        if (this.status === 4 && this.frame_current_cnt === 31) {
            let me = this, you = this.root.players[1 - this.id]
            let r1;
            if (this.direction > 0) {
                r1 = {
                    x1: me.x + me.width,
                    y1: me.y + 8,
                    x2: me.x + me.width + 17,
                    y2: me.y + 8 + 6
                }
            } else {
                r1 = {
                    x1: me.x - 17,
                    y1: me.y + 8,
                    x2: me.x,
                    y2: me.y + 8 + 6
                }
            }
            let r2 = {
                x1: you.x,
                y1: you.y,
                x2: you.x + you.width,
                y2: you.y + you.width
            }

            if (this.is_collision(r1, r2)) {
                you.is_attack();
            }
        }
    }

    update_result() {
        if (this.store.state.pk.loser !== "" && this.result === "none") {
            this.result = this.store.state.pk.loser;
            if ((this.result === 'A' || this.result === 'all') && this.id === 0) {
                this.frame_current_cnt = 0;
                this.status = 6;
            }
            if ((this.result === 'B' || this.result === 'all') && this.id === 1) {
                this.frame_current_cnt = 0;
                this.status = 6;
            }
        }
    }

    testRate () {
        this.rend_count ++;
    }

    update() {
        this.testRate();
        this.update_attack()
        this.update_direction();
        this.update_move();
        this.update_controller()
        this.render();
        this.update_result()
    }

    render() {
        let status = this.status
        let obj = this.animations.get(status)
        if (obj && obj.loaded) {
            if (this.direction === 1) {
                let k = parseInt(this.frame_current_cnt / obj.frame_rate) % obj.frame_cnt;
                let image = obj.gif.frames[k].image;
                this.ctx.drawImage(image, this.x, this.y + obj.offset_y, image.width * obj.scale, image.height * obj.scale)
            } else {
                this.ctx.save();
                this.ctx.scale(-1, 1)
                this.ctx.translate(-this.root.$canvas.width(), 0);

                let k = parseInt(this.frame_current_cnt / obj.frame_rate) % obj.frame_cnt;
                let image = obj.gif.frames[k].image;
                this.ctx.drawImage(image, this.root.$canvas.width() - this.x - this.width, this.y + obj.offset_y, image.width * obj.scale, image.height * obj.scale)

                this.ctx.restore()
            }
        }
        if (this.status === 3) {
            obj.frame_rate = 4
        }

        if (this.status === 1 || this.status === 2) {
            obj.frame_rate = 7
        }

        if (status === 4 || status === 5) {
            if (parseInt(this.frame_current_cnt / obj.frame_rate) === obj.frame_cnt - 1)
                this.status = 0;
        }

        if (this.status === 6) {
            if (parseInt(this.frame_current_cnt / obj.frame_rate) === obj.frame_cnt - 1)
                this.frame_current_cnt--;
        }

        this.frame_current_cnt++;
    }
}