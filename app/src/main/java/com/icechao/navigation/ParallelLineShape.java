package com.icechao.navigation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

public class ParallelLineShape {


    private Path baseLinePath = new Path();
    private Path parallelLinePath = new Path();
    private Path parallelLineAreaPath = new Path();
    private float[] startPoint = new float[2];
    private float[] endPoint = new float[2];
    private Paint shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private PathMeasure baseLineMeasue;
    private float distance = 50;
    private Region parallelLineArea = new Region();

    public ParallelLineShape() {
        shapePaint.setStrokeWidth(10);
        shapePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shapePaint.setColor(Color.GREEN);
        shapePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void drawShape(Canvas canvas) {
        canvas.drawPath(baseLinePath, shapePaint);
        canvas.drawPath(parallelLinePath, shapePaint);
    }

    private boolean dragLine;
    private boolean checkChangeDistance;

    public void setPoint(float x, float y) {

        if (parallelLineArea.contains((int) x, (int) y)) {
            dragLine = true;
        } else {
            dragLine = false;
            startPoint[0] = x;
            startPoint[1] = y;
        }
    }

    public void updatePoint(float x, float y, float distanceX, float distanceY) {

        if (dragLine) {

            distance += (Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2))) / 3;

        } else {
            endPoint[0] = x;
            endPoint[1] = y;
        }
        baseLinePath.rewind();
        baseLinePath.moveTo(startPoint[0], startPoint[1]);
        baseLinePath.lineTo(endPoint[0], endPoint[1]);
        baseLineMeasue = new PathMeasure(baseLinePath, false);
        float length = baseLineMeasue.getLength();
        float diffX = (startPoint[1] - endPoint[1]) / length * distance;
        float diffY = (startPoint[0] - endPoint[0]) / length * distance;
        parallelLinePath.rewind();
        float parallelLineStartX = startPoint[0] - diffX;
        float parallelLineStartY = startPoint[1] + diffY;
        float parallelLineEndX = parallelLineStartX + endPoint[0] - startPoint[0];
        float parallelLineEndY = startPoint[1] + diffY + endPoint[1] - startPoint[1];
        parallelLinePath.moveTo(parallelLineStartX, parallelLineStartY);
        parallelLinePath.lineTo(parallelLineEndX, parallelLineEndY);

        parallelLineAreaPath.rewind();
        parallelLineAreaPath.moveTo(startPoint[0], startPoint[1]);
        parallelLineAreaPath.lineTo(endPoint[0], endPoint[1]);
        parallelLineAreaPath.lineTo(parallelLineEndX, parallelLineEndY);
        parallelLineAreaPath.lineTo(parallelLineStartX, parallelLineStartY);
        parallelLineAreaPath.close();

        RectF bounds = new RectF();
        parallelLinePath.computeBounds(bounds, true);
        parallelLineArea.setPath(parallelLineAreaPath, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
    }

    private void moveLine(float distanceX, float distanceY) {
        startPoint[0] -= distanceX;
        startPoint[1] -= distanceY;
        endPoint[0] -= distanceX;
        endPoint[1] -= distanceY;
    }


}
