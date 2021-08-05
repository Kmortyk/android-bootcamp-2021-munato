function draw(ctx, cw, ch) {
    let colors = [
        "#4caf50",
        "black"
    ]

    ctx.font = "42px serif";

    ctx.fillStyle = colors[0];
    ctx.fillRect(0, 0, cw * 0.5, ch);

    ctx.fillStyle = colors[1];
    ctx.fillText("GREEN", 5, 50);

    ctx.fillRect(cw * 0.5, 0, cw * 0.5, ch);

    ctx.fillStyle = colors[0];
    ctx.fillText("TEA", cw * 0.5 + 30, 50);

    // draw glass
    let tw = 60

    ctx.fillStyle = colors[0];
    ctx.fillRect(cw * 0.5, ch * 0.5, tw, 200);

    ctx.fillStyle = "white";
    ctx.fillRect(cw * 0.5 - tw, ch * 0.5, tw, 200);
}