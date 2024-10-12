package com.example.ar_furniture_application.customizations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Request the parent (HorizontalScrollView) not to intercept touch events
        requestDisallowInterceptTouchEvent(true);

        // Handle WebView's own touch events
        return super.onTouchEvent(event);
    }
}
