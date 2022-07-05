package com.volcengine.vertcdemo.voicechatdemo.feature.roommain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.volcengine.vertcdemo.voicechat.R;

import java.util.LinkedList;
import java.util.List;

public class VCChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mMsgList = new LinkedList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_chat_demo_chat,
                parent, false);
        return new VCChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VCChatViewHolder) {
            ((VCChatViewHolder) holder).bind(mMsgList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    public void addChatMsg(String info) {
        if (info == null) {
            return;
        }
        mMsgList.add(info);
        notifyItemInserted(mMsgList.size() - 1);
    }

    private static class VCChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView mChatTv;

        public VCChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatTv = (TextView) itemView;
        }

        public void bind(String msg) {
            mChatTv.setText(msg);
        }
    }
}

