package com.volcengine.vertcdemo.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.Nullable;

public class Utils {

    /**
     * 用于将子View绑定到ViewGroup，避免强制移除和添加控件引起的闪烁现象
     *
     * @param viewGroup 要绑定的目标父布局
     * @param view      要绑定的控件
     * @param params    要绑定的控件参数
     */
    public static void attachViewToViewGroup(@Nullable ViewGroup viewGroup,
                                             @Nullable View view,
                                             @Nullable ViewGroup.LayoutParams params) {
        if (view == null || viewGroup == null) {
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent == null) {
            viewGroup.addView(view, params);
        } else if (viewParent instanceof ViewGroup) {
            ViewGroup parentViewGroup = (ViewGroup) viewParent;
            if (parentViewGroup != viewGroup) {
                parentViewGroup.removeView(view);
                viewGroup.addView(view, params);
            }
        }
    }

    public static void removeFromParent(View view) {
        if (view == null) {
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) viewParent;
            parent.removeView(view);
        }
    }

    /**
     * 用于将子View绑定到ViewGroup，避免强制移除和添加控件引起的闪烁现象（默认使用 ）
     *
     * @param viewGroup 要绑定的目标父布局
     * @param view      要绑定的控件
     */
    public static void attachViewToViewGroup(@Nullable ViewGroup viewGroup,
                                             @Nullable View view) {
        if (view == null || viewGroup == null) {
            return;
        }
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        attachViewToViewGroup(viewGroup, view, params);
    }
}
