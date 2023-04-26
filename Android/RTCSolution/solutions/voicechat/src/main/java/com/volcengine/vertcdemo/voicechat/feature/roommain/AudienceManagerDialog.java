// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roommain;

import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.DISABLE_ALLOW_APPLY;
import static com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager.ENABLE_ALLOW_APPLY;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.volcengine.vertcdemo.common.SolutionToast;
import com.volcengine.vertcdemo.common.BaseDialog;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.ErrorTool;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyEvent;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedEvent;
import com.volcengine.vertcdemo.voicechat.bean.GetAudienceResponse;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatUserInfo;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.databinding.DialogVoiceChatAudienceManagerBinding;
import com.volcengine.vertcdemo.voicechat.event.UserStatusChangedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 观众管理对话框
 */
@SuppressWarnings("unused")
public class AudienceManagerDialog extends BaseDialog {

    public static final int TABLE_ONLINE_USERS = 0;
    public static final int TABLE_APPLY_USERS = 1;

    public static final int SEAT_ID_BY_SERVER = -1;

    @IntDef({TABLE_ONLINE_USERS, TABLE_APPLY_USERS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UserManagerTable {
    }

    private final int SELECTED_TEXT_COLOR = Color.parseColor("#4080FF");

    private DialogVoiceChatAudienceManagerBinding mViewBinding;

    @UserManagerTable
    private int mTable = TABLE_ONLINE_USERS;
    private String mRoomId;
    private boolean mAllowApply;
    private boolean hasNewApply;
    private int mSeatId = SEAT_ID_BY_SERVER;

    private final IRequestCallback<GetAudienceResponse> mGetOnlineUsersCallback = new IRequestCallback<GetAudienceResponse>() {
        @Override
        public void onSuccess(GetAudienceResponse data) {
            setOnlineUserList(data.audienceList);
        }

        @Override
        public void onError(int errorCode, String message) {
            mViewBinding.managerOnlineEmptyListView.setVisibility(View.VISIBLE);
        }
    };

    private final IRequestCallback<GetAudienceResponse> mGetApplyUsersCallback = new IRequestCallback<GetAudienceResponse>() {
        @Override
        public void onSuccess(GetAudienceResponse data) {
            setApplyUserList(data.audienceList);

            boolean hasNewApply = data.audienceList != null && !data.audienceList.isEmpty();
            setHasNewApply(hasNewApply);
        }

        @Override
        public void onError(int errorCode, String message) {
            mViewBinding.managerApplyEmptyListView.setVisibility(View.VISIBLE);
        }
    };

    private final IAction<VoiceChatUserInfo> mUserInfoOption = userInfo -> {
        if (userInfo == null) {
            return;
        }
        int status = userInfo.userStatus;
        IRequestCallback<VoiceChatResponse> callback = new IRequestCallback<VoiceChatResponse>() {
            @Override
            public void onSuccess(VoiceChatResponse data) {
                if (userInfo.userStatus != VoiceChatUserInfo.USER_STATUS_APPLYING) {
                    SolutionToast.show(R.string.Invitation_has_been_sent_to_the_audience);
                    cancel();
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                SolutionToast.show(ErrorTool.getErrorMessageByErrorCode(errorCode, message));
            }
        };
        if (status == VoiceChatUserInfo.USER_STATUS_NORMAL) {
            VoiceChatRTCManager.ins().getRTSClient().inviteInteract(mRoomId, userInfo.userId, mSeatId, callback);
        } else if (status == VoiceChatUserInfo.USER_STATUS_APPLYING) {
            VoiceChatRTCManager.ins().getRTSClient().agreeApply(mRoomId, userInfo.userId, userInfo.roomId, callback);
        }
    };

    private final AudienceManagerAdapter mOnlineAudienceAdapter = new AudienceManagerAdapter(mUserInfoOption);
    private final AudienceManagerAdapter mApplyAudienceAdapter = new AudienceManagerAdapter(mUserInfoOption);

    public AudienceManagerDialog(Context context) {
        super(context);
    }

    public AudienceManagerDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AudienceManagerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mViewBinding = DialogVoiceChatAudienceManagerBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
        
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mViewBinding.managerOnlineList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mViewBinding.managerApplyList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mViewBinding.managerOnlineList.setAdapter(mOnlineAudienceAdapter);
        mViewBinding.managerApplyList.setAdapter(mApplyAudienceAdapter);

        mViewBinding.managerOnlineTab.setOnClickListener((v) -> changeTable(TABLE_ONLINE_USERS));
        mViewBinding.managerApplyTab.setOnClickListener((v) -> changeTable(TABLE_APPLY_USERS));
        mViewBinding.managerSwitch.setChecked(mAllowApply);
        mViewBinding.managerSwitch.setOnCheckedChangeListener(this::onManagerSwitchChanged);
    }

    /**
     * 是否开启连线需要申请开关
     * @param view 开关view
     * @param isChecked 是否选中
     */
    private void onManagerSwitchChanged(CompoundButton view, boolean isChecked) {
        if (!view.isEnabled()) {
            return;
        }
        view.setEnabled(false);
        int type = isChecked ? DISABLE_ALLOW_APPLY : ENABLE_ALLOW_APPLY;
        VoiceChatRTCManager.ins().getRTSClient().manageInteractApply(mRoomId, type, new IRequestCallback<VoiceChatResponse>() {
            @Override
            public void onSuccess(VoiceChatResponse data) {
                mAllowApply = isChecked;
                view.setEnabled(true);
                VoiceChatDataManager.ins().setAllowUserApply(mAllowApply);
            }

            @Override
            public void onError(int errorCode, String message) {
                mAllowApply = !isChecked;
                view.setChecked(!isChecked);
                view.setEnabled(true);
                VoiceChatDataManager.ins().setAllowUserApply(mAllowApply);
            }
        });
    }

    public void setData(String roomId, boolean allowApply, boolean hasNewApply, int seatId) {
        mRoomId = roomId;
        mAllowApply = allowApply;
        mSeatId = seatId;
        setHasNewApply(hasNewApply);
        if (isShowing()) {
            mViewBinding.managerSwitch.setChecked(mAllowApply);
        }
    }

    @Override
    public void show() {
        super.show();
        SolutionDemoEventManager.register(this);
        changeTable(hasNewApply ? TABLE_APPLY_USERS : TABLE_ONLINE_USERS);
        setHasNewApply(hasNewApply);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        SolutionDemoEventManager.unregister(this);
    }

    private void changeTable(@UserManagerTable int table) {
        final int normalTextColor = Color.WHITE;
        mTable = table;
        if (table == TABLE_ONLINE_USERS) {
            requestOnlineUserList();
            mViewBinding.managerOnlineTab.setTextColor(SELECTED_TEXT_COLOR);
            mViewBinding.managerApplyTab.setTextColor(normalTextColor);
            mViewBinding.managerOnlineList.setVisibility(View.VISIBLE);
            mViewBinding.managerOnlineEmptyListView.setVisibility(View.GONE);
            mViewBinding.managerApplyList.setVisibility(View.GONE);
            mViewBinding.managerApplyEmptyListView.setVisibility(View.GONE);

            mViewBinding.managerOnlineIndicator.setVisibility(View.VISIBLE);
            mViewBinding.managerApplyIndicator.setVisibility(View.INVISIBLE);
        } else if (table == TABLE_APPLY_USERS) {
            requestApplyUserList();
            mViewBinding.managerOnlineTab.setTextColor(normalTextColor);
            mViewBinding.managerApplyTab.setTextColor(SELECTED_TEXT_COLOR);
            mViewBinding.managerOnlineList.setVisibility(View.GONE);
            mViewBinding.managerOnlineEmptyListView.setVisibility(View.GONE);
            mViewBinding.managerApplyList.setVisibility(View.VISIBLE);
            mViewBinding.managerApplyEmptyListView.setVisibility(View.GONE);

            mViewBinding.managerOnlineIndicator.setVisibility(View.INVISIBLE);
            mViewBinding.managerApplyIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void requestOnlineUserList() {
        VoiceChatRTCManager.ins().getRTSClient().requestAudienceList(mRoomId, mGetOnlineUsersCallback);
    }

    private void requestApplyUserList() {
        VoiceChatRTCManager.ins().getRTSClient().requestApplyAudienceList(mRoomId, mGetApplyUsersCallback);
    }

    private void setOnlineUserList(List<VoiceChatUserInfo> users) {
        mOnlineAudienceAdapter.setData(users);
        if (users == null || users.isEmpty()) {
            mViewBinding.managerOnlineEmptyListView.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.managerOnlineEmptyListView.setVisibility(View.GONE);
        }
    }

    private void setApplyUserList(List<VoiceChatUserInfo> users) {
        mApplyAudienceAdapter.setData(users);
        if (users == null || users.isEmpty()) {
            mViewBinding.managerApplyEmptyListView.setVisibility(View.VISIBLE);
        } else {
            mViewBinding.managerApplyEmptyListView.setVisibility(View.GONE);
        }
    }

    public void setHasNewApply(boolean hasNewApply) {
        this.hasNewApply = hasNewApply;
        VoiceChatDataManager.ins().setNewApply(hasNewApply);
        AudienceApplyEvent broadcast = new AudienceApplyEvent();
        broadcast.hasNewApply = hasNewApply;
        SolutionDemoEventManager.post(broadcast);
        if (isShowing()) {
            mViewBinding.managerApplyRedDot.setVisibility(hasNewApply ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserStatusChangedEvent(UserStatusChangedEvent event) {
        if (event.status == VoiceChatUserInfo.USER_STATUS_NORMAL
                || event.status == VoiceChatUserInfo.USER_STATUS_INVITING) {
            mOnlineAudienceAdapter.addOrUpdateUser(event.userInfo);
            mApplyAudienceAdapter.removeUser(event.userInfo);
        } else if (event.status == VoiceChatUserInfo.USER_STATUS_APPLYING) {
            mOnlineAudienceAdapter.removeUser(event.userInfo);
            mApplyAudienceAdapter.addOrUpdateUser(event.userInfo);
        } else if (event.status == VoiceChatUserInfo.USER_STATUS_INTERACT) {
            mOnlineAudienceAdapter.addOrUpdateUser(event.userInfo);
            mApplyAudienceAdapter.removeUser(event.userInfo);
            if (mApplyAudienceAdapter.getItemCount() == 0) {
                setHasNewApply(false);
            }
        }
        updateEmptyViewVis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedEvent event) {
        if (event.isJoin) {
            mOnlineAudienceAdapter.addOrUpdateUser(event.userInfo);
        } else {
            mOnlineAudienceAdapter.removeUser(event.userInfo);
            mApplyAudienceAdapter.removeUser(event.userInfo);
        }
        if (mTable == TABLE_APPLY_USERS) {
            if (mApplyAudienceAdapter.getItemCount() == 0) {
                setApplyUserList(null);
            }
        } else {
            if (mOnlineAudienceAdapter.getItemCount() == 0) {
                setOnlineUserList(null);
            }
        }
        updateEmptyViewVis();
    }

    private void updateEmptyViewVis() {
        if (mTable == TABLE_APPLY_USERS && mApplyAudienceAdapter != null) {
            int size = mApplyAudienceAdapter.getItemCount();
            mViewBinding.managerApplyEmptyListView.setVisibility(size > 0 ? View.GONE : View.VISIBLE);
        } else if (mTable == TABLE_ONLINE_USERS && mOnlineAudienceAdapter != null) {
            int size = mOnlineAudienceAdapter.getItemCount();
            mViewBinding.managerOnlineEmptyListView.setVisibility(size > 0 ? View.GONE : View.VISIBLE);
        }
    }

    private static class AudienceManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<VoiceChatUserInfo> mData = new ArrayList<>();
        private final IAction<VoiceChatUserInfo> mUserOption;

        public AudienceManagerAdapter(IAction<VoiceChatUserInfo> userOption) {
            mUserOption = userOption;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_chat_main_audience, parent, false);
            return new AudienceManagerViewHolder(view, mUserOption);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AudienceManagerViewHolder) {
                ((AudienceManagerViewHolder) holder).bind(mData.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void setData(@Nullable List<VoiceChatUserInfo> users) {
            mData.clear();
            if (users != null) {
                mData.addAll(users);
            }
            notifyDataSetChanged();
        }

        public void addOrUpdateUser(VoiceChatUserInfo userInfo) {
            if (userInfo == null || TextUtils.isEmpty(userInfo.userId)) {
                return;
            }
            for (int i = 0; i < mData.size(); i++) {
                if (TextUtils.equals(userInfo.userId, mData.get(i).userId)) {
                    mData.get(i).userStatus = userInfo.userStatus;
                    notifyItemChanged(i);
                    return;
                }
            }
            mData.add(userInfo);
            notifyItemInserted(mData.size() - 1);
        }

        public void removeUser(VoiceChatUserInfo userInfo) {
            if (userInfo == null || TextUtils.isEmpty(userInfo.userId)) {
                return;
            }
            for (int i = 0; i < mData.size(); i++) {
                if (TextUtils.equals(userInfo.userId, mData.get(i).userId)) {
                    mData.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
        }

        private static class AudienceManagerViewHolder extends RecyclerView.ViewHolder {
            private VoiceChatUserInfo mUserInfo;
            private final TextView mUserNamePrefix;
            private final TextView mUserName;
            private final TextView mUserOption;

            public AudienceManagerViewHolder(@NonNull View itemView, IAction<VoiceChatUserInfo> userInfoIAction) {
                super(itemView);
                mUserNamePrefix = itemView.findViewById(R.id.item_voice_chat_demo_user_prefix);
                mUserName = itemView.findViewById(R.id.item_voice_chat_demo_user_name);
                mUserOption = itemView.findViewById(R.id.item_voice_chat_demo_user_option);
                mUserOption.setOnClickListener((v) -> {
                    if (mUserInfo != null && userInfoIAction != null) {
                        userInfoIAction.act(mUserInfo);
                    }
                });
            }

            public void bind(VoiceChatUserInfo userInfo) {
                mUserInfo = userInfo;
                if (userInfo != null) {
                    String userName = userInfo.userName;
                    mUserNamePrefix.setText(TextUtils.isEmpty(userName) ? "" : userName.substring(0, 1));
                    mUserName.setText(userName);
                    updateOptionByStatus(userInfo.userStatus);
                } else {
                    mUserNamePrefix.setText("");
                    mUserName.setText("");
                    updateOptionByStatus(VoiceChatUserInfo.USER_STATUS_NORMAL);
                }
            }

            private void updateOptionByStatus(@VoiceChatUserInfo.UserStatus int status) {
                if (status == VoiceChatUserInfo.USER_STATUS_INTERACT) {
                    mUserOption.setText(R.string.already_on_mic);
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                } else if (status == VoiceChatUserInfo.USER_STATUS_INVITING) {
                    mUserOption.setText(R.string.invited);
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_unselected_bg);
                } else if (status == VoiceChatUserInfo.USER_STATUS_APPLYING) {
                    mUserOption.setText(R.string.accept);
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                } else {
                    mUserOption.setText(R.string.invite_to_mic);
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                }
            }
        }
    }
}
