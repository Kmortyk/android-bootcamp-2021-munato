function draw(ctx, cw, ch) {
    const cl = new CanvasLightning(ctx, cw, ch);

    setupRAF();
    cl.init();
}

function CanvasLightning(ctx, cw, ch) {
    this.init = function () {
        this.loop();
    };

    // Variables
    const _this = this;
    this.ctx = ctx;
    this.cw = cw;
    this.ch = ch;

    this.lightning = [];
    this.lightTimeCurrent = 0;
    this.lightTimeTotal = 50;

    // Utility functions
    this.rand = function (rMi, rMa) {
        return ~~((Math.random() * (rMa - rMi + 1)) + rMi);
    };

    // Create Lightning
    this.createL = function (x, y, canSpawn) {
        this.lightning.push({
            x: x,
            y: y,
            xRange: this.rand(5, 30),
            yRange: this.rand(5, 25),
            path: [{
                x: x,
                y: y
            }],
            pathLimit: this.rand(10, 35),
            canSpawn: canSpawn,
            hasFired: false
        });
    };

    // Update Lightning
    this.updateL = function () {
        let i = this.lightning.length;
        while (i--) {
            const light = this.lightning[i];

            light.path.push({
                x: light.path[light.path.length - 1].x + (this.rand(0, light.xRange) - (light.xRange / 2)),
                y: light.path[light.path.length - 1].y + (this.rand(0, light.yRange))
            });

            if (light.path.length > light.pathLimit) {
                this.lightning.splice(i, 1)
            }
            light.hasFired = true;
        }
    };

    // Render Lightning
    this.renderL = function () {
        let i = this.lightning.length;
        while (i--) {
            const light = this.lightning[i];

            this.ctx.strokeStyle = 'hsla(0, 100%, 100%, ' + this.rand(10, 100) / 100 + ')';
            this.ctx.lineWidth = 1;
            if (this.rand(0, 30) === 0) {
                this.ctx.lineWidth = 2;
            }
            if (this.rand(0, 60) === 0) {
                this.ctx.lineWidth = 3;
            }
            if (this.rand(0, 90) === 0) {
                this.ctx.lineWidth = 4;
            }
            if (this.rand(0, 120) === 0) {
                this.ctx.lineWidth = 5;
            }
            if (this.rand(0, 150) === 0) {
                this.ctx.lineWidth = 6;
            }

            this.ctx.beginPath();

            const pathCount = light.path.length;
            this.ctx.moveTo(light.x, light.y);
            for (let pc = 0; pc < pathCount; pc++) {

                this.ctx.lineTo(light.path[pc].x, light.path[pc].y);

                if (light.canSpawn) {
                    if (this.rand(0, 100) === 0) {
                        light.canSpawn = false;
                        this.createL(light.path[pc].x, light.path[pc].y, false);
                    }
                }
            }

            if (!light.hasFired) {
                this.ctx.fillStyle = 'rgba(255, 255, 255, ' + this.rand(4, 12) / 100 + ')';
                this.ctx.fillRect(0, 0, this.cw, this.ch);
            }

            if (this.rand(0, 30) === 0) {
                this.ctx.fillStyle = 'rgba(255, 255, 255, ' + this.rand(1, 3) / 100 + ')';
                this.ctx.fillRect(0, 0, this.cw, this.ch);
            }

            this.ctx.stroke();
        }
    };

    // Lightning Timer
    this.lightningTimer = function () {
        this.lightTimeCurrent++;
        if (this.lightTimeCurrent >= this.lightTimeTotal) {
            const newX = this.rand(100, cw - 100);
            const newY = this.rand(0, ch / 2);
            let createCount = this.rand(1, 3);
            while (createCount--) {
                this.createL(newX, newY, true);
            }
            this.lightTimeCurrent = 0;
            this.lightTimeTotal = this.rand(30, 100);
        }
    }

    // Clear Canvas
    this.clearCanvas = function () {
        this.ctx.fillStyle = 'black';
        this.ctx.fillRect(0, 0, this.cw, this.ch);
        this.ctx.globalCompositeOperation = 'source-over';
    };

    // Animation Loop
    this.loop = function () {
        const loopIt = function () {
            requestAnimationFrame(loopIt);
            _this.clearCanvas();
            _this.updateL();
            _this.lightningTimer();
            _this.renderL();
        };
        loopIt();
    };

    window.addEventListener('resize', function(event){
        _this.cw = window.innerWidth;
        _this.ch = window.innerHeight;
    });
}

// Setup requestAnimationFrame
function setupRAF() {
    let lastTime = 0;
    const vendors = ['ms', 'moz', 'webkit', 'o'];
    for (let x = 0; x < vendors.length && !window.requestAnimationFrame; ++x) {
        window.requestAnimationFrame = window[vendors[x] + 'RequestAnimationFrame'];
        window.cancelAnimationFrame = window[vendors[x] + 'CancelAnimationFrame'] || window[vendors[x] + 'CancelRequestAnimationFrame'];
    }

    if (!window.requestAnimationFrame) {
        window.requestAnimationFrame = function (callback) {
            const currTime = new Date().getTime();
            const timeToCall = Math.max(0, 16 - (currTime - lastTime));
            const id = window.setTimeout(function () {
                callback(currTime + timeToCall);
            }, timeToCall);
            lastTime = currTime + timeToCall;
            return id;
        };
    }

    if (!window.cancelAnimationFrame) {
        window.cancelAnimationFrame = function (id) {
            clearTimeout(id);
        };
    }
}