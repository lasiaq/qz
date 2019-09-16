package com.qqzz.qz.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.qqzz.qz.base.BaseApplication;

/**
 * 类描述：资源工具类
 * 作者： Sting
 * 时间： 2019/6/5 16:22
 */
public class ResourceUtil {


    /**
     * 获取color
     * @param colorId 颜色值资源
     * @return int
     */
    public static int getColor(@ColorRes int colorId) {
        return BaseApplication.getAppContext().getResources().getColor(colorId);
    }

    /**
     * 获取string
     * @param stringId 字符串资源
     * @return string
     */
    public static String getString(@StringRes int stringId) {
        return BaseApplication.getAppContext().getResources().getString(stringId);
    }

    /**
     * SVG 动态着色
     *
     * @param context
     * @param imgSrc
     * @param colorSrc
     * @return
     */
    public static Drawable svgTintColor(Context context, int imgSrc, int colorSrc) {
        Drawable vectorDrawableCompat = ContextCompat.getDrawable(context, imgSrc);
        DrawableCompat.setTint(DrawableCompat.wrap(vectorDrawableCompat).mutate(), context.getResources().getColor(colorSrc));
        return vectorDrawableCompat;
    }

    /**
     * 根据color_id 获取十六进制色值
     *
     * @param id
     * @return
     */
    public static StringBuffer  getHexColorByResourceId(int id) {
        StringBuffer stringBuffer = new StringBuffer();
        int color = getColor(id);

        stringBuffer.append("#");
        stringBuffer.append(Integer.toHexString(Color.alpha(color)));
        stringBuffer.append(Integer.toHexString(Color.red(color)));
        stringBuffer.append(Integer.toHexString(Color.green(color)));
        stringBuffer.append(Integer.toHexString(Color.blue(color)));
        return stringBuffer;
    }

    /**
     * 根据抽屉拖拽进度返回透明度的十六进制值
     *
     * @param alpha 1.0f-100f
     * @return
     */
    public static String getAlpha(float alpha) {
        float temp = 255 * alpha * 1.0f / 100f;
        int round = Math.round(temp);//四舍五入
        String hexString = Integer.toHexString(round);
        if (hexString.length() < 2) {
            hexString += "0";
        }
        return hexString;
    }
}
