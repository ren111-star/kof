import { Player } from './base.js'
import { useStore } from "vuex";
import { GIF } from '../utils/gif.js'

export class Kyo extends Player {
    constructor(root, info) {
        super(root, info);
        this.store = useStore()
        this.init_animations();
    }

    init_animations () {
        let outer = this;
        let offsets = [0, -5, -5, -30, 0, 0, 0]
        switch (this.play_used) {
            case 'kyo':
                offsets = [0, -5, -5, -30, 0, 0, 0]
                break;
            case 'bashen':
                offsets = [0, 0, 0, -30, 0, 0, -30]
        }
        for (let i = 0; i < 7; i ++ ) {
            let gif = GIF();
            let player = outer.play_used
            gif.load(require(`@/assets/images/player/${player}/${i}.gif`))
            this.animations.set(i, {
                gif: gif,
                frame_cnt: 0,
                frame_rate: 10,
                offset_y: offsets[i],
                loaded: false,
                scale: 2 / 5,
            })

            gif.onload = function () {
                let obj = outer.animations.get(i);
                obj.frame_cnt = gif.frames.length;
                obj.loaded = true;
            }
        }
    }
}