package example.com.mymenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by Adminstrator on 2017/2/14.
 */

public class MainUI extends RelativeLayout {
    private FrameLayout leftmenu,middlemenu,rightmenu,middlemask;
    private Scroller mscroller;
    public static @android.support.annotation.IdRes final int LEFT_ID=0xaabbcc;
    public static  @android.support.annotation.IdRes final int M_ID=0xaaccbb;
    public static @android.support.annotation.IdRes final int R_ID=0xbbaacc;


    public MainUI(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }
    public MainUI(Context context) {
        super(context);
        initview(context);
    }
    public void initview(Context context){
            leftmenu=new FrameLayout(context);
            rightmenu=new FrameLayout(context);
            middlemenu=new FrameLayout(context);
            middlemask=new FrameLayout(context);
            mscroller=new Scroller(context,new DecelerateInterpolator());
            leftmenu.setBackgroundColor(Color.YELLOW);
            middlemenu.setBackgroundColor(Color.RED);
           rightmenu.setBackgroundColor(Color.YELLOW);
            middlemask.setBackgroundColor(Color.GRAY);
        leftmenu.setId(LEFT_ID);
        middlemenu.setId(M_ID);
        rightmenu.setId(R_ID);
        addView(middlemenu);
        addView(leftmenu);
        addView(rightmenu);
        addView(middlemask);
        middlemask.setAlpha(0);
    }
       public   float onMiddlemask(){
           System.out.println("透明度为"+middlemask.getAlpha());
           return middlemask.getAlpha();
       }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
       int scrollx=Math.abs(getScrollX());
        float alpha=scrollx/(float)leftmenu.getMeasuredWidth();
        middlemask.setAlpha(alpha);
        onMiddlemask();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middlemenu.measure(widthMeasureSpec,heightMeasureSpec);
        middlemask.measure(widthMeasureSpec,heightMeasureSpec);
        int realwidth=MeasureSpec.getSize(widthMeasureSpec);
        int tempwidthmeasure=MeasureSpec.makeMeasureSpec((int) (realwidth*0.8f),MeasureSpec.EXACTLY);
        leftmenu.measure(tempwidthmeasure, heightMeasureSpec);
        rightmenu.measure(tempwidthmeasure,heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        middlemenu.layout(l, t, r, b);
        middlemask.layout(l, t, r, b);
        leftmenu.layout(l-leftmenu.getMeasuredWidth(),t,r-middlemenu.getMeasuredWidth(),b);
        rightmenu.layout(l+middlemenu.getMeasuredWidth(),t,r+rightmenu.getMeasuredWidth(),b);
    }
    private boolean istextcompete=false;
    private boolean isleftrighteven=false;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            if (!istextcompete){
                geteventype(ev);
                return true;
            }
        if (isleftrighteven){

            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_MOVE:
                    int scrollx=getScrollX();
                    int dis_x= (int) (ev.getX()-point.x);
                    int expectx=-dis_x+scrollx;
                    int finalx=0;
                    if (expectx<0){
                        finalx=Math.max(expectx,-leftmenu.getMeasuredWidth());
                    }else {
                        finalx=Math.min(expectx,rightmenu.getMeasuredWidth());
                    }
                    scrollTo(finalx,0);
                    point.x= (int) ev.getX();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    scrollx=getScrollX();
                    if (Math.abs(scrollx)>leftmenu.getMeasuredWidth()>>1){
                            if (scrollx<0){
                                mscroller.startScroll(scrollx,0,-leftmenu.getMeasuredWidth()-scrollx,0);
                            }else {
                                mscroller.startScroll(scrollx,0,leftmenu.getMeasuredWidth()-scrollx,0);
                            }
                    }else {
                            mscroller.startScroll(scrollx,0,-scrollx,0);
                    }
                    invalidate();
                    isleftrighteven=false;
                    istextcompete=false;
                    break;
            }
        }else{
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_UP:
                    isleftrighteven=false;
                    istextcompete=false;
            }       }
        return super.dispatchTouchEvent(ev);
    }    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mscroller.computeScrollOffset()){
            return;
        }
        int temp=mscroller.getCurrX();
        scrollTo(temp,0);
    }
    private Point point=new Point();
    private static final int a=20;
    private void geteventype(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                super.dispatchTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) Math.abs(ev.getX() - point.x);
                int dy = (int) Math.abs(ev.getY() - point.y);
                if (dx > 20 && dx > dy) {
                    isleftrighteven = true;
                    istextcompete = true;
                } else if (dy > dx && dy > 20) {
                    isleftrighteven = false;
                    istextcompete = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                super.dispatchTouchEvent(ev);
                istextcompete = false;
                isleftrighteven = false;
                break;
        }
    }

}
