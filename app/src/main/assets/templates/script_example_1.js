function draw(ctx, canvas) {
    let drawCircle = function(cx, cy, r) {
        ctx.fillStyle = "white";
        ctx.beginPath();
        ctx.arc(cx, cy, r, 0, 2 * Math.PI, false);
        ctx.fill();
    }

    let drawCloud = function(cx, cy) {
        drawCircle(cx - 55, cy + 10, 35);
        drawCircle(cx, cy, 50);
        drawCircle(cx + 55, cy + 7, 30);
    }

    // draw sky
    ctx.fillStyle = "#256fce";
    ctx.fillRect(
        0, 0,
        canvas.width,
        canvas.height
    );

    drawCloud(100, 100);
    drawCloud(200, 200);
    drawCloud(130, 350);
}