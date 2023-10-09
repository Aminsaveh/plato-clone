package com.example.ap_plato;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends  RecyclerView.Adapter{
    private ArrayList<Message> messages = ChatPage.messages;
    String otherSideUsername = ChatPage.otherSideUsername;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.chat_other_side_layout , parent , false);
            return new OtherSideViewHolder(view);
        }
        else{
            view = layoutInflater.inflate(R.layout.chat_this_side_layout , parent , false);
            return new ThisSideViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         if (this.messages != null){
            if (this.messages.get(position).username.equals(this.otherSideUsername)){
                OtherSideViewHolder otherSideViewHolder = (OtherSideViewHolder) holder;
                final Message message = this.messages.get(position);
                otherSideViewHolder.usernameTextView.setText(message.username);
                otherSideViewHolder.dateTextView.setText(message.date);
                otherSideViewHolder.msgTextView.setText(message.msg);
            }
            else{
                ThisSideViewHolder thisSideViewHolder = (ThisSideViewHolder) holder;
                final Message message = this.messages.get(position);
                thisSideViewHolder.usernameTextView.setText(message.username);
                thisSideViewHolder.dateTextView.setText(message.date);
                thisSideViewHolder.msgTextView.setText(message.msg);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (ChatPage.messages.get(position).username.equals(this.otherSideUsername)){
            return 0;
        }
        else {
            return 1;
        }
    }


    @Override
    public int getItemCount() {
        return ChatPage.messages.size();
    }

    public MessageAdapter(ArrayList<Message> messages){
        this.messages = ChatPage.messages;
    }
    class ThisSideViewHolder extends  RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView msgTextView;
        TextView dateTextView;
        public ThisSideViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usernameTextView = itemView.findViewById(R.id.this_side_username);
            this.msgTextView =itemView.findViewById(R.id.this_side_msg);
            this.dateTextView = itemView.findViewById(R.id.this_side_msg_date);
        }
    }
    class OtherSideViewHolder extends  RecyclerView.ViewHolder{
        TextView usernameTextView;
        TextView msgTextView;
        TextView dateTextView;
        public OtherSideViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usernameTextView = itemView.findViewById(R.id.other_side_username);
            this.msgTextView =itemView.findViewById(R.id.other_side_msg);
            this.dateTextView = itemView.findViewById(R.id.other_side_msg_date);
        }
    }
}
