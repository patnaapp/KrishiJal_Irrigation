package bih.in.krishijal_irrigation.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;

public class takegpsListAdaptor extends RecyclerView.Adapter<takegpsListAdaptor.ViewHolder> {
    private ArrayList<InspectionDetailsModel> ListItem;
    private LayoutInflater mInflater;
    View view1;
    Context context;
    ViewHolder viewHolder1;

    String Landownername="";


    public takegpsListAdaptor(Context context1, ArrayList<InspectionDetailsModel> SubjectValues1){

        ListItem=SubjectValues1;
        context=context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView ad_txt_gpstype,ad_txt_plotno,ad_txt_channelNo,ad_txt_lat,ad_txt_long,lbl_txt_plotno,lbl_txt_channelNo;

        ImageView img;

        public ViewHolder(View v) {

            super(v);

            ad_txt_gpstype = (TextView) v.findViewById(R.id.ad_txt_gpstype);
            ad_txt_plotno = (TextView) v.findViewById(R.id.ad_txt_plotno);
            ad_txt_channelNo = (TextView) v.findViewById(R.id.ad_txt_channelNo);
            ad_txt_lat = (TextView) v.findViewById(R.id.ad_txt_lat);
            ad_txt_long = (TextView) v.findViewById(R.id.ad_txt_long);
            lbl_txt_plotno = (TextView) v.findViewById(R.id.lbl_txt_plotno);
            lbl_txt_channelNo = (TextView) v.findViewById(R.id.lbl_txt_channelNo);



        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view1 = LayoutInflater.from(context).inflate(R.layout.adaptor_take_gps_list, parent, false);

        viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final InspectionDetailsModel info = ListItem.get(position);
        holder.ad_txt_gpstype.setText(info.getGPSTypeName());
        holder.ad_txt_lat.setText(info.getLatitude());
        holder.ad_txt_long.setText(info.getLongitude());
        if(info.getPlotNo()!=null) {
            holder.ad_txt_plotno.setText(info.getPlotNo());
        }else {
            holder.lbl_txt_plotno.setVisibility(View.GONE);
        }
        if(info.getChannelName()!=null) {
            holder.ad_txt_channelNo.setText(info.getChannelName());
        }else {
            holder.lbl_txt_channelNo.setVisibility(View.GONE);
        }





    }


    @Override
    public int getItemCount() {

        return ListItem.size();
    }





}
