package com.apkglobal.cheruvu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by HellBlazer on 19-04-2018.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyHolder>{

    List<Listdata> listdata;

    public RecyclerviewAdapter(List<Listdata> listdata) {
        this.listdata = listdata;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myview,parent,false);

        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }


    public void onBindViewHolder(MyHolder holder, int position) {
        Listdata data = listdata.get(position);
        holder.serial.setText(data.getSerial());
        holder.name.setText(data.getName());
        holder.age.setText(data.getAge());
        holder.mandal.setText(data.getMandal());
        holder.village.setText(data.getVillage());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{
        TextView serial,name,age,mandal,village;

        public MyHolder(View itemView) {
            super(itemView);
            serial = (TextView) itemView.findViewById(R.id.serial);
            name = (TextView) itemView.findViewById(R.id.name);
            age = (TextView) itemView.findViewById(R.id.age);
            mandal = (TextView) itemView.findViewById(R.id.mandal);
            village = (TextView) itemView.findViewById(R.id.village);

        }
    }


}
