package com.fyp.sitwell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class report_adapter extends RecyclerView.Adapter<report_adapter.recViewHolder> {

    ArrayList<report_model> dataholder;

    public report_adapter(ArrayList<report_model> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public recViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_single_row,parent,false);
        return new recViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recViewHolder holder, int position) //holer means ui
    {
        holder.recID.setText("Record ID : "+dataholder.get(position).getRecordID());
        holder.duration.setText( "Duration : "+ dataholder.get(position).getDuration());
        holder.accuracy.setText("Sit Accuracy : " +dataholder.get(position).getSitAccuracy()+" %");
        holder.review.setText("Review : " + reviewMsg(dataholder.get(position).getNeckNum(), dataholder.get(position).getBackNum()
                ,dataholder.get(position).getSHLDRNum(),dataholder.get(position).getLeftArmNum(),dataholder.get(position).getRightArmNum()));

    }

    public String reviewMsg(int neckNum, int backNum, int SHLDRNum, int ltArm, int rtArm){
        String msg="";

        Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
        ht.put("neck",neckNum);
        ht.put("back",backNum);
        ht.put("shoulder",SHLDRNum);
        ht.put("leftArm", ltArm);
        ht.put("rightArm",rtArm);

        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(ht.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry1.getValue().compareTo( entry2.getValue() );
            }
        });

        Map<String, Integer> mapSortedByValues = new LinkedHashMap<String, Integer>();

        for( Map.Entry<String, Integer> entry : list  ){
            mapSortedByValues.put(entry.getKey(), entry.getValue());
        }

        System.out.println("Map sorted by values: " + mapSortedByValues);

        msg = mapSortedByValues.toString();

        return msg;
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class recViewHolder extends RecyclerView.ViewHolder
    {
        TextView recID,duration,accuracy, review;
        public recViewHolder(@NonNull View itemView)
        {
            super(itemView);
            recID=(TextView)itemView.findViewById(R.id.recordID);
            duration=(TextView)itemView.findViewById(R.id.duration);
            accuracy=(TextView)itemView.findViewById(R.id.sitAccuracy);
            review=(TextView)itemView.findViewById(R.id.comment);


        }
    }
}
