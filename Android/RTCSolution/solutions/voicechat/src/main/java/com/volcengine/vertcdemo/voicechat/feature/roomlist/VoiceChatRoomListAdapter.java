// Copyright (c) 2023 Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

package com.volcengine.vertcdemo.voicechat.feature.roomlist;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.volcengine.vertcdemo.utils.Utils;
import com.volcengine.vertcdemo.voicechat.R;
import com.volcengine.vertcdemo.voicechat.bean.VoiceChatRoomInfo;
import com.volcengine.vertcdemo.common.IAction;

import java.util.ArrayList;
import java.util.List;

/**
 * 房间列表adapter
 */
public class VoiceChatRoomListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<VoiceChatRoomInfo> mRoomList = new ArrayList<>();

    private final IAction<VoiceChatRoomInfo> mOnClickRoomInfo;

    public VoiceChatRoomListAdapter(IAction<VoiceChatRoomInfo> onClickRoomInfo) {
        mOnClickRoomInfo = onClickRoomInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_chat_room_list, parent, false);
        return new VoiceChatRoomListViewHolder(view, mOnClickRoomInfo);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VoiceChatRoomListViewHolder) {
            ((VoiceChatRoomListViewHolder) holder).bind(mRoomList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mRoomList.size();
    }

    public void setRoomList(List<VoiceChatRoomInfo> roomList) {
        mRoomList.clear();
        if (roomList != null) {
            mRoomList.addAll(roomList);
        }
        notifyDataSetChanged();
    }

    private static class VoiceChatRoomListViewHolder extends RecyclerView.ViewHolder {

        private final TextView mRoomTitle;
        private final TextView mHostPrefix;
        private final TextView mHostName;
        private final TextView mAudienceCount;
        private VoiceChatRoomInfo mRoomInfo;

        public VoiceChatRoomListViewHolder(@NonNull View itemView, IAction<VoiceChatRoomInfo> onClickRoomInfo) {
            super(itemView);
            mRoomTitle = itemView.findViewById(R.id.item_voice_chat_demo_room_tile);
            mHostPrefix = itemView.findViewById(R.id.item_voice_chat_demo_room_name_prefix);
            mHostName = itemView.findViewById(R.id.item_voice_chat_demo_room_name);
            mAudienceCount = itemView.findViewById(R.id.item_voice_chat_demo_room_audience_count);
            Drawable drawable = ContextCompat.getDrawable(itemView.getContext(),
                    R.drawable.voice_chat_room_list_audience_icon);
            drawable.setBounds(0, 0,
                    (int) Utils.dp2Px(9), (int) Utils.dp2Px(11));
            mAudienceCount.setCompoundDrawables(drawable, null, null, null);
            itemView.setOnClickListener((v) -> {
                if (mRoomInfo != null && onClickRoomInfo != null) {
                    onClickRoomInfo.act(mRoomInfo);
                }
            });
        }

        public void bind(@Nullable VoiceChatRoomInfo roomInfo) {
            mRoomInfo = roomInfo;
            if (roomInfo != null) {
                mRoomTitle.setText(roomInfo.roomName);
                String userName = TextUtils.isEmpty(roomInfo.hostUserName) ? " " : roomInfo.hostUserName;
                mHostPrefix.setText(userName.substring(0, 1));
                mHostName.setText(userName);
                mAudienceCount.setText(String.valueOf(roomInfo.audienceCount + 1));
            } else {
                mRoomTitle.setText("");
                mHostPrefix.setText("");
                mHostName.setText("");
                mAudienceCount.setText("0");
            }
        }
    }
}
