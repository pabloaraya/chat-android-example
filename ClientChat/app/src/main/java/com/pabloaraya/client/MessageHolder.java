package com.pabloaraya.client;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pablo on 2/22/15.
 */
public class MessageHolder extends RecyclerView.ViewHolder{

    protected TextView textViewMessage;
    protected TextView textViewName;

    public MessageHolder(View view) {
        super(view);
        this.textViewMessage = (TextView)view.findViewById(R.id.textViewMessage);
        this.textViewName   = (TextView)view.findViewById(R.id.textViewName);
    }
}
