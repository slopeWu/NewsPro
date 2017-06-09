package wp.newspro.SelfView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import wp.newspro.R;


/**
 * Created by Administrator on 2017/6/9.
 */

public class TimerView extends View {
    private String content;
    private int padding = 5;
    private float diameter_inner;
    private float diameter_outer;
    private Paint paint_inner;
    private Paint paint_out;
    private RectF rectF;
    private float contentWidth;
    private TextPaint textPaint;
    private float degree;
    private ITimerViewListener mtimeViewListener;

    public ITimerViewListener getMtimeViewListener() {
        return mtimeViewListener;
    }

    public void setMtimeViewListener(ITimerViewListener mtimeViewListener) {
        this.mtimeViewListener = mtimeViewListener;
    }

    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerView);
        int innerColor = typedArray.getColor(R.styleable.TimerView_innerColor, Color.BLUE);
        int outerColor = typedArray.getColor(R.styleable.TimerView_outerColor, Color.GREEN);
        content = typedArray.getText(R.styleable.TimerView_text).toString();

        //文字画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.WHITE);

        //内圈圆画笔
        paint_inner = new Paint();
        paint_inner.setAntiAlias(true);
        paint_inner.setColor(innerColor);

        // 外圈圆
        paint_out = new Paint();
        paint_out.setAntiAlias(true);
        paint_out.setColor(outerColor);
        paint_out.setStyle(Paint.Style.STROKE);
        paint_out.setStrokeWidth(padding);

        //文字的尺寸
        contentWidth = textPaint.measureText(content);
        //内圆的直径
        diameter_inner = contentWidth + padding * 2;

        //外圆的直径
        diameter_outer = diameter_inner + padding * 2;
        rectF = new RectF(padding / 2, padding / 2, diameter_outer - padding / 2, diameter_outer - padding / 2);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置控件的大小
        setMeasuredDimension((int) diameter_outer, (int) diameter_outer);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //画内圆
        canvas.drawCircle(diameter_outer / 2, diameter_outer / 2, diameter_inner / 2, paint_inner);
        //外圆
        canvas.save();
        canvas.rotate(-90, diameter_outer / 2, diameter_outer / 2);
        canvas.drawArc(rectF, 0f, degree, false, paint_out);
        canvas.restore();
        //文字
        float ascent = textPaint.ascent();
        float descent = textPaint.descent();
        canvas.drawText(content, padding * 2, (diameter_outer - (ascent + descent)) / 2, textPaint);
    }

    public void setProgess(int total, int timers) {
        float preDegree = 360.0f / total;
        degree = preDegree * timers;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.3f);//设置自定义view的透明度
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1f);
                if (mtimeViewListener != null) {
                    mtimeViewListener.onClickListener();
                }
                break;
        }
        return true;
    }
}
