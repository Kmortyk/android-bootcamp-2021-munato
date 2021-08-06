function draw(ctx, cw, ch) {
    let startAngle = 0
    let endAngle = 2 * Math.PI

    let startRadius = cw * 0.3
    let endRadius = 1
    let stepRadius = 10

    ctx.fillStyle = "black";
    ctx.strokeStyle = "white";

    // draw background
    ctx.fillRect(
        0, 0,
        cw, ch
    );

    // draw circles
    for(let r = startRadius; r > endRadius; r -= stepRadius) {
        ctx.beginPath();

        ctx.arc(
                cw / 2,
                ch / 2,
                r,
                startAngle,
                endAngle
        );

        ctx.stroke();
    }

}