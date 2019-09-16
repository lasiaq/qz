package com.qqzz.qz.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.qqzz.qz.R;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<K extends ViewDataBinding> extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();
    public Context context;
    public TitleBar toolbar;
    public K dataBinding;

    public boolean toolbarLeftBack = true; //如果左边按钮不做返回操作，需要置为false

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }
        dataBinding = DataBindingUtil.setContentView((Activity) context, setContentView());
        if (dataBinding != null) {
            dataBinding.executePendingBindings();
        }

        initToolbar();
        onActivityCreate(savedInstanceState);
    }


    protected abstract void onActivityCreate(Bundle savedInstanceState);


    protected abstract int setContentView();


    public void initBundle() {
        EventBus.getDefault().register(context);
    }

    public void startActivity(Class c) {
        startActivity(new Intent(context, c));
    }

    public void startActivity(Class c, Object o) {
        EventBus.getDefault().postSticky(o);
        startActivity(new Intent(context, c));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(context);
    }

    /**
     * 设置标题栏
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        // 返回按钮
        if (toolbar != null) {
            toolbar.setOnTitleBarListener(new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    if (toolbarLeftBack) {
                        finish();
                    }
                }

                @Override
                public void onTitleClick(View v) {

                }

                @Override
                public void onRightClick(View v) {
                }
            });
        }
    }


    /**
     * 键盘自动弹出处理
     *
     * @param event
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return dispatchTouchEvent(ev, getCurrentFocus(), super.dispatchTouchEvent(ev));
    }

    public boolean dispatchTouchEvent(MotionEvent ev, View v, boolean dispatchTouchEvent) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return dispatchTouchEvent;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private static boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY()
                    < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
