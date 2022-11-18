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
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ss.video.rtc.demo.basic_module.utils.SafeToast;
import com.volcengine.vertcdemo.common.BaseDialog;
import com.volcengine.vertcdemo.common.IAction;
import com.volcengine.vertcdemo.core.eventbus.SolutionDemoEventManager;
import com.volcengine.vertcdemo.core.net.IRequestCallback;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.AudienceApplyBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.AudienceChangedBroadcast;
import com.volcengine.vertcdemo.voicechat.bean.GetAudienceResponse;
import com.volcengine.vertcdemo.voicechat.bean.VCUserInfo;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatResponse;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatDataManager;
import com.volcengine.vertcdemo.voicechat.core.VoiceChatRTCManager;
import com.volcengine.vertcdemo.voicechat.event.UserStatusChangedEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

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

    private View mOnlineEmptyView;
    private View mApplyEmptyView;
    private View mApplyRedDotView;
    private View mOnlineIndicator;
    private View mApplyIndicator;
    private TextView mOnlineAudienceTitle;
    private TextView mApplyAudienceTitle;
    private Switch mAllowApplySwitch;
    private RecyclerView mOnlineAudienceRv;
    private RecyclerView mApplyAudienceRv;

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
            mOnlineEmptyView.setVisibility(View.VISIBLE);
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
            mApplyEmptyView.setVisibility(View.VISIBLE);
        }
    };

    private final IAction<VCUserInfo> mUserInfoOption = userInfo -> {
        if (userInfo == null) {
            return;
        }
        int status = userInfo.userStatus;
        IRequestCallback<VoiceChatResponse> callback = new IRequestCallback<VoiceChatResponse>() {
            @Override
            public void onSuccess(VoiceChatResponse data) {
                if (userInfo.userStatus != VCUserInfo.USER_STATUS_APPLYING) {
                    SafeToast.show("已向观众发出邀请，等待对方应答");
                    cancel();
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                if (errorCode == 506) {
                    SafeToast.show("当前麦位已满");
                }
            }
        };
        if (status == VCUserInfo.USER_STATUS_NORMAL) {
            VoiceChatRTCManager.ins().getRTMClient().inviteInteract(mRoomId, userInfo.userId, mSeatId, callback);
        } else if (status == VCUserInfo.USER_STATUS_APPLYING) {
            VoiceChatRTCManager.ins().getRTMClient().agreeApply(mRoomId, userInfo.userId, userInfo.roomId, callback);
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
        setContentView(R.layout.dialog_voice_chat_audience_manager);
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mAllowApplySwitch = findViewById(R.id.manager_switch);
        mOnlineAudienceTitle = findViewById(R.id.manager_online_tab);
        mApplyAudienceTitle = findViewById(R.id.manager_apply_tab);
        mApplyRedDotView = findViewById(R.id.manager_apply_red_dot);
        mOnlineIndicator = findViewById(R.id.manager_online_indicator);
        mApplyIndicator = findViewById(R.id.manager_apply_indicator);
        mOnlineAudienceRv = findViewById(R.id.manager_online_list);
        mApplyAudienceRv = findViewById(R.id.manager_apply_list);
        mOnlineEmptyView = findViewById(R.id.manager_online_empty_list_view);
        mApplyEmptyView = findViewById(R.id.manager_apply_empty_list_view);

        mOnlineAudienceRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mApplyAudienceRv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mOnlineAudienceRv.setAdapter(mOnlineAudienceAdapter);
        mApplyAudienceRv.setAdapter(mApplyAudienceAdapter);

        mOnlineAudienceTitle.setOnClickListener((v) -> changeTable(TABLE_ONLINE_USERS));
        mApplyAudienceTitle.setOnClickListener((v) -> changeTable(TABLE_APPLY_USERS));
        mAllowApplySwitch.setChecked(mAllowApply);
        mAllowApplySwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if (!v.isEnabled()) {
                return;
            }
            v.setEnabled(false);
            int type = isChecked ? DISABLE_ALLOW_APPLY : ENABLE_ALLOW_APPLY;
            VoiceChatRTCManager.ins().getRTMClient().manageInteractApply(mRoomId, type, new IRequestCallback<VoiceChatResponse>() {
                @Override
                public void onSuccess(VoiceChatResponse data) {
                    mAllowApply = isChecked;
                    v.setEnabled(true);
                    VoiceChatDataManager.ins().setAllowUserApply(mAllowApply);
                }

                @Override
                public void onError(int errorCode, String message) {
                    mAllowApply = !isChecked;
                    v.setChecked(!isChecked);
                    v.setEnabled(true);
                    VoiceChatDataManager.ins().setAllowUserApply(mAllowApply);
                }
            });
        });
    }

    public void setData(String roomId, boolean allowApply, boolean hasNewApply, int seatId) {
        mRoomId = roomId;
        mAllowApply = allowApply;
        mSeatId = seatId;
        setHasNewApply(hasNewApply);
        if (mAllowApplySwitch != null) {
            mAllowApplySwitch.setChecked(mAllowApply);
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
            mOnlineAudienceTitle.setTextColor(SELECTED_TEXT_COLOR);
            mApplyAudienceTitle.setTextColor(normalTextColor);
            mOnlineAudienceRv.setVisibility(View.VISIBLE);
            mOnlineEmptyView.setVisibility(View.GONE);
            mApplyAudienceRv.setVisibility(View.GONE);
            mApplyEmptyView.setVisibility(View.GONE);

            mOnlineIndicator.setVisibility(View.VISIBLE);
            mApplyIndicator.setVisibility(View.INVISIBLE);
        } else if (table == TABLE_APPLY_USERS) {
            requestApplyUserList();
            mOnlineAudienceTitle.setTextColor(normalTextColor);
            mApplyAudienceTitle.setTextColor(SELECTED_TEXT_COLOR);
            mOnlineAudienceRv.setVisibility(View.GONE);
            mOnlineEmptyView.setVisibility(View.GONE);
            mApplyAudienceRv.setVisibility(View.VISIBLE);
            mApplyEmptyView.setVisibility(View.GONE);

            mOnlineIndicator.setVisibility(View.INVISIBLE);
            mApplyIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void requestOnlineUserList() {
        VoiceChatRTCManager.ins().getRTMClient().requestAudienceList(mRoomId, mGetOnlineUsersCallback);
    }

    private void requestApplyUserList() {
        VoiceChatRTCManager.ins().getRTMClient().requestApplyAudienceList(mRoomId, mGetApplyUsersCallback);
    }

    private void setOnlineUserList(List<VCUserInfo> users) {
        mOnlineAudienceAdapter.setData(users);
        if (users == null || users.isEmpty()) {
            mOnlineEmptyView.setVisibility(View.VISIBLE);
        } else {
            mOnlineEmptyView.setVisibility(View.GONE);
        }
    }

    private void setApplyUserList(List<VCUserInfo> users) {
        mApplyAudienceAdapter.setData(users);
        if (users == null || users.isEmpty()) {
            mApplyEmptyView.setVisibility(View.VISIBLE);
        } else {
            mApplyEmptyView.setVisibility(View.GONE);
        }
    }

    public void setHasNewApply(boolean hasNewApply) {
        this.hasNewApply = hasNewApply;
        VoiceChatDataManager.ins().setNewApply(hasNewApply);
        AudienceApplyBroadcast broadcast = new AudienceApplyBroadcast();
        broadcast.hasNewApply = hasNewApply;
        SolutionDemoEventManager.post(broadcast);
        if (isShowing()) {
            mApplyRedDotView.setVisibility(hasNewApply ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserStatusChangedEvent(UserStatusChangedEvent event) {
        if (event.status == VCUserInfo.USER_STATUS_NORMAL
                || event.status == VCUserInfo.USER_STATUS_INVITING) {
            mOnlineAudienceAdapter.addOrUpdateUser(event.userInfo);
            mApplyAudienceAdapter.removeUser(event.userInfo);
        } else if (event.status == VCUserInfo.USER_STATUS_APPLYING) {
            mOnlineAudienceAdapter.removeUser(event.userInfo);
            mApplyAudienceAdapter.addOrUpdateUser(event.userInfo);
        } else if (event.status == VCUserInfo.USER_STATUS_INTERACT) {
            mOnlineAudienceAdapter.addOrUpdateUser(event.userInfo);
            mApplyAudienceAdapter.removeUser(event.userInfo);
            if (mApplyAudienceAdapter.getItemCount() == 0) {
                setHasNewApply(false);
            }
        }
        updateEmptyViewVis();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAudienceChangedBroadcast(AudienceChangedBroadcast event) {
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
        if (mTable == TABLE_APPLY_USERS && mApplyEmptyView != null && mApplyAudienceAdapter != null) {
            int size = mApplyAudienceAdapter.getItemCount();
            mApplyEmptyView.setVisibility(size > 0 ? View.GONE : View.VISIBLE);
        } else if (mTable == TABLE_ONLINE_USERS && mOnlineEmptyView != null && mOnlineAudienceAdapter != null) {
            int size = mOnlineAudienceAdapter.getItemCount();
            mOnlineEmptyView.setVisibility(size > 0 ? View.GONE : View.VISIBLE);
        }
    }

    private static class AudienceManagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final List<VCUserInfo> mData = new ArrayList<>();
        private final IAction<VCUserInfo> mUserOption;

        public AudienceManagerAdapter(IAction<VCUserInfo> userOption) {
            mUserOption = userOption;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_chat_demo_main_audience, parent, false);
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

        public void setData(@Nullable List<VCUserInfo> users) {
            mData.clear();
            if (users != null) {
                mData.addAll(users);
            }
            notifyDataSetChanged();
        }

        public void addOrUpdateUser(VCUserInfo userInfo) {
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

        public void removeUser(VCUserInfo userInfo) {
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
            private VCUserInfo mUserInfo;
            private final TextView mUserNamePrefix;
            private final TextView mUserName;
            private final TextView mUserOption;

            public AudienceManagerViewHolder(@NonNull View itemView, IAction<VCUserInfo> userInfoIAction) {
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

            public void bind(VCUserInfo userInfo) {
                mUserInfo = userInfo;
                if (userInfo != null) {
                    String userName = userInfo.userName;
                    mUserNamePrefix.setText(TextUtils.isEmpty(userName) ? "" : userName.substring(0, 1));
                    mUserName.setText(userName);
                    updateOptionByStatus(userInfo.userStatus);
                } else {
                    mUserNamePrefix.setText("");
                    mUserName.setText("");
                    updateOptionByStatus(VCUserInfo.USER_STATUS_NORMAL);
                }
            }

            private void updateOptionByStatus(@VCUserInfo.UserStatus int status) {
                if (status == VCUserInfo.USER_STATUS_INTERACT) {
                    mUserOption.setText("已上麦");
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                } else if (status == VCUserInfo.USER_STATUS_INVITING) {
                    mUserOption.setText("已邀请");
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_unselected_bg);
                } else if (status == VCUserInfo.USER_STATUS_APPLYING) {
                    mUserOption.setText("接受");
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                } else {
                    mUserOption.setText("邀请上麦");
                    mUserOption.setBackgroundResource(R.drawable.item_voice_listener_option_selected_bg);
                }
            }
        }
    }
}
