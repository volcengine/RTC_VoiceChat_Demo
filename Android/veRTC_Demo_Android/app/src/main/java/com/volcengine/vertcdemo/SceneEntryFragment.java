package com.volcengine.vertcdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.SolutionDataManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class SceneEntryFragment extends Fragment {
    public static final String TAG = "SceneEntryFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scene_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setPackage(BuildConfig.APPLICATION_ID);
        intent.addCategory(Actions.CATEGORY_SCENE);

        Context context = requireContext();
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> scenes = packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        LinearLayout cards = view.findViewById(R.id.cards);
        LayoutInflater inflater = LayoutInflater.from(context);

        Collections.sort(scenes, (o1, o2) -> readSolutionIndex(o1) - readSolutionIndex(o2));

        for (ResolveInfo scene : scenes) {
            View card = inflater.inflate(R.layout.item_scene_entry, cards, false);
            ImageView icon = card.findViewById(R.id.icon);
            TextView label = card.findViewById(R.id.text);

            final int iconRes = scene.getIconResource();
            if (iconRes != ResourcesCompat.ID_NULL) {
                icon.setImageResource(iconRes);
            } else {
                icon.setImageDrawable(scene.loadIcon(packageManager));
            }

            label.setText(scene.loadLabel(packageManager));
            card.setOnClickListener(createSceneHandler(scene));
            cards.addView(card);
        }
    }

    private View.OnClickListener createSceneHandler(ResolveInfo scene) {
        return v -> {
            if (scene == null || scene.activityInfo == null || TextUtils.isEmpty(scene.activityInfo.name)) {
                SafeToast.show("Enter scene failed by activityInfo is empty!");
                return;
            }
            String sceneNameAbbr = extractSceneNameAbbr(scene);
            if (TextUtils.isEmpty(sceneNameAbbr)) {
                SafeToast.show("SceneCode not set");
                return;
            }
            String token = SolutionDataManager.ins().getToken();
            if (TextUtils.isEmpty(token)) {
                SafeToast.show("Token not found.");
                return;
            }
            v.setEnabled(false);
            IAction<Object> action = (o) -> v.setEnabled(true);
            startScene(scene.activityInfo.name, action);
        };
    }

    /***
     * 开启业务场景
     * @param targetActivity 开启目标业务场景的入口Activity类名
     * @param doneAction 开启目标业务场景的执行后执行的代码块
     */
    private void startScene(String targetActivity, IAction<Object> doneAction) {
        boolean res = invokePrepareSolutionParams(targetActivity, doneAction);
        if (!res) {
            SafeToast.show("enter solution failed");
        }
    }

    @Nullable
    private static String extractSceneNameAbbr(ResolveInfo scene) {
        Bundle metaData = scene.activityInfo.metaData;
        return metaData == null ? null : metaData.getString("scene_name_abbr");
    }

    /**
     * 反射执行要启动 activity 的 prepareSolutionParams(activity) 方法
     *
     * @param targetActivity 要启动类的类名
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private boolean invokePrepareSolutionParams(String targetActivity, IAction doneAction) {
        try {
            Class clz = Class.forName(targetActivity);
            if (clz == null) {
                return false;
            }
            Method method = clz.getMethod("prepareSolutionParams", Activity.class, IAction.class);
            if (method == null) {
                return false;
            }
            method.invoke(null, getActivity(), doneAction);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "can not find class");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, "can not find method");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e(TAG, "method invoke error");
        }
        return false;
    }

    /**
     * 读取指定 ResolveInfo 中配置的 scene_name_abbr 信息，并获取该缩略词在配置文件中的顺序信息
     * @param info 各个场景的ResolveInfo
     * @return 该场景在配置文件中的下标
     */
    static int readSolutionIndex(ResolveInfo info) {
        String abbr = extractSceneNameAbbr(info);
        String[] solutionAbbrs = BuildConfig.SOLUTION_ORDER.split(",");
        for (int i = 0; i < solutionAbbrs.length; i++) {
            if (TextUtils.equals(abbr, solutionAbbrs[i])) {
                return i;
            }
        }
        return -1;
    }
}