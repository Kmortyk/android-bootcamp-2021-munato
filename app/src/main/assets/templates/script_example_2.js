function draw(ctx, canvas) {
    let startAngle = 0
    let endAngle = 2 * Math.PI

    let startRadius = canvas.width * 0.3
    let endRadius = 1
    let stepRadius = 10

    ctx.fillStyle = "black";
    ctx.strokeStyle = "white";

    // draw background
    ctx.fillRect(
        0, 0,
        canvas.width,
        canvas.height
    );

    // draw circles
    for(r = startRadius; r > endRadius; r -= stepRadius) {
        ctx.beginPath();

        ctx.arc(
                canvas.width / 2,
                canvas.height / 2,
                r,
                startAngle,
                endAngle
        );

        ctx.stroke();
    }

}