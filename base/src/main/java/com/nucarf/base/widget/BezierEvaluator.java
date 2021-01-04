package com.nucarf.base.widget;

import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.PointF;
import android.os.Build;

/**
 * Created tt on 16/6/6.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BezierEvaluator implements TypeEvaluator {
    private PointF pointF1;
    private PointF pointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.pointF1 = pointF1;
        this.pointF2 = pointF2;
    }

    @Override
    public PointF evaluate(float time, Object startValue1, Object endValue1) {
        float timeLeft = 1.0f - time;
        PointF startValue =(PointF) startValue1;
        PointF endValue =(PointF) endValue1;
        PointF point = new PointF();
        point.x = timeLeft * timeLeft * timeLeft * (startValue.x) + 3 * timeLeft * timeLeft * time * (pointF1.x) + 3 * timeLeft * time * time * (pointF2.x) + time * time * time * (endValue.x);
        point.y = timeLeft * timeLeft * timeLeft * (startValue.y) + 3 * timeLeft * timeLeft * time * (pointF1.y) + 3 * timeLeft * time * time * (pointF2.y) + time * time * time * (endValue.y);
        return point;
    }
}
