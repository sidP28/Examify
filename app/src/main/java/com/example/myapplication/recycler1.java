package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycler1 extends RecyclerView.Adapter<recycler1.viewholder> {

    ArrayList<testdisp> array;
    static String[] seloptions;
    recycler1(ArrayList<testdisp> array){
        this.array = array;
        this.seloptions = new String[array.size()];
    }
    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.test,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        testdisp obj = array.get(position);
        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.a1:
                        holder.a1.setBackgroundColor(Color.CYAN);
                        holder.b1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.c1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.d1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        seloptions[position] = holder.a1.getText().toString();
                        break;
                    case R.id.b1:
                        holder.b1.setBackgroundColor(Color.CYAN);
                        holder.a1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.c1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.d1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        seloptions[position] = holder.b1.getText().toString();
                        break;
                    case R.id.c1:
                        holder.c1.setBackgroundColor(Color.CYAN);
                        holder.b1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.a1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.d1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        seloptions[position] = holder.c1.getText().toString();
                        break;
                    case R.id.d1:
                        holder.d1.setBackgroundColor(Color.CYAN);
                        holder.b1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.c1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        holder.a1.setBackgroundColor(ContextCompat.getColor(group.getContext(),R.color.options));
                        seloptions[position] = holder.d1.getText().toString();
                        break;
                }
            }
        });
        holder.question.setText(obj.ques);
        holder.a1.setText(obj.a);
        holder.b1.setText(obj.b);
        holder.c1.setText(obj.c);
        holder.d1.setText(obj.d);
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView question;
        RadioGroup rg;
        RadioButton a1,b1,c1,d1;
        viewholder(View view){
            super(view);
            a1 = view.findViewById(R.id.a1);
            b1 = view.findViewById(R.id.b1);
            c1 = view.findViewById(R.id.c1);
            d1 = view.findViewById(R.id.d1);
            rg = view.findViewById(R.id.radiogrp);
            question = view.findViewById(R.id.question);
        }
    }
}

class abc{
    String ques,ans;
    abc(String ques,String ans){
        this.ques = ques;
        this.ans = ans;
    }
    void setans(String ans){
        this.ans = ans;
    }
}
