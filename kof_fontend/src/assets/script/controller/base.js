export class Controller {
    constructor($canvas) {
        this.$canvas = $canvas;

        this.start();
        this.pressed_keys = new Set();
    }

    start() {
        let outer = this;
        this.$canvas.keydown(function (e) {
            outer.pressed_keys.add(e.key);
        })

        this.$canvas.keyup(function (e) {
            outer.pressed_keys.delete(e.key)
        })
    }

}