package com.pabloaraya.client;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pablo on 2/22/15.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageHolder>{

    private Context context;
    private List<MessageModel> messages;

    private final static int VIEW_LAYOUT_MINE_MESSAGE = 0;
    private final static int VIEW_LAYOUT_MESSAGE = 1;

    /* Typeface */
    private Typeface robotoLight;

    public MessageAdapter(Context context) {
        this.context    = context;
        this.messages   = new ArrayList<>();
        //robotoLight     = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view;
        if(i == VIEW_LAYOUT_MINE_MESSAGE){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_mine, null);
        } else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, null);
        }
        MessageHolder mh = new MessageHolder(view);
        return mh;
    }

    @Override
    public void onBindViewHolder(MessageHolder messageHolder, int i) {

        /* Message model */
        MessageModel message = messages.get(i);

        /* Set elements */
        messageHolder.textViewMessage.setText(message.getMessage());
        messageHolder.textViewName.setText(message.getName());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(MessageModel message){
        if(getItemCount() > 0) {
            /* Get last message */
            MessageModel lastMessage = messages.get(getItemCount() - 1);

            /* verify equals */
            if (message.getName().equals(lastMessage.getName())) {

                /* Concat the two messages */
                String concat = lastMessage.getMessage()
                        .concat("\n")
                        .concat(message.getMessage());

                messages.get(getItemCount() - 1).setMessage(concat);
            } else {
                messages.add(message);
            }
        }else{
            messages.add(message);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int i) {
        if(messages.get(i).getName().equals(App.getSession().getString(App.VAR_USERNAME, App.VAR_EMPTY))){
            return VIEW_LAYOUT_MINE_MESSAGE;
        }else{
            return VIEW_LAYOUT_MESSAGE;
        }
    }
}
