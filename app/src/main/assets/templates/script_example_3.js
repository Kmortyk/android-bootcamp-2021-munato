function draw(ctx, canvas) {
    let w = canvas.width
    let h = canvas.height
    let colors = [
        "#4caf50",
        "black"
    ]

    ctx.font = "42px serif";

    ctx.fillStyle = colors[0];
    ctx.fillRect(0, 0, w * 0.5, h);

    ctx.fillStyle = colors[1];
    ctx.fillText("GREEN", 5, 50);

    ctx.fillRect(w * 0.5, 0, w * 0.5, h);

    ctx.fillStyle = colors[0];
    ctx.fillText("TEA", canvas.width * 0.5 + 30, 50);

    // draw glass
    let tw = 60

    ctx.fillStyle = colors[0];
    ctx.fillRect(w * 0.5, h * 0.5, tw, 200);

    ctx.fillStyle = "white";
    ctx.fillRect(w * 0.5 - tw, h * 0.5, tw, 200);
}