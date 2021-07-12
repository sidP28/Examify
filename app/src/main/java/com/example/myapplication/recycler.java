package com.example.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycler extends RecyclerView.Adapter<recycler.adapter> {
    Context context;
    ArrayList<questions> arraylist;

    public recycler(Context context, ArrayList<questions> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @Override
    public adapter onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.questions_dialog,parent,false);
        return new adapter(view);
    }
    @Override
    public void onBindViewHolder(adapter holder, int position) {
        questions obj = arraylist.get(position);
        holder.a.setText(obj.a);
        holder.b.setText(obj.b);
        holder.c.setText(obj.c);
        holder.d.setText(obj.d);
        holder.ques.setText(obj.ques);
    }
    @Override
    public int getItemCount() {
        return arraylist.size();
    }
    public static class adapter extends RecyclerView.ViewHolder{
        TextView ques;
        TextView a;
        TextView b;
        TextView c;
        TextView d;
        public adapter(View view1) {
            super(view1);
            d = view1.findViewById(R.id.d);
            c = view1.findViewById(R.id.c);
            b = view1.findViewById(R.id.b);
            a = view1.findViewById(R.id.a);
            ques = view1.findViewById(R.id.q);
        }
    }
}

