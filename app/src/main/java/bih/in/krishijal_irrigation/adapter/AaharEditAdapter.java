package bih.in.krishijal_irrigation.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.ui.Aahar_Sinchaai_YojyaActivity;
import bih.in.krishijal_irrigation.utility.Utiilties;
import bih.in.krishijal_irrigation.web_services.WebServiceHelper;


public class AaharEditAdapter extends RecyclerView.Adapter<AaharEditAdapter.ViewHolder> {

    private ArrayList<InspectionDetailsModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public AaharEditAdapter(Context context, ArrayList<InspectionDetailsModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }


    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adaptor_ahar_sinchai_edit, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final InspectionDetailsModel info = mData.get(position);

        setDataToView(info, holder, position);

//        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(context)
//                        .setIcon(R.drawable.logo1)
//                        .setTitle(R.string.confirmation)
//                        .setMessage("क्या आप डाटा हटाना चाहते है?")
//                        .setCancelable(false)
//                        .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //deleteSaveData(position, "Deleted");
//                            }
//                        })
//                        .setNegativeButton("नहीं", null)
//                        .show();
//            }
//        });

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InspectionDetailsModel obj = mData.get(position);
                Intent intent = new Intent(context, Aahar_Sinchaai_YojyaActivity.class);
                intent.putExtra("KeyId",obj.getInspectionId());
                intent.putExtra("isEdit", "Yes");
                //intent.putExtra("data", obj);
                context.startActivity(intent);
            }
        });

//        holder.tv_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(context)
//                        .setIcon(R.drawable.logo1)
//                        .setTitle(R.string.confirmation)
//                        .setMessage("क्या आप डाटा अपलोड करना चाहते है?")
//                        .setCancelable(false)
//                        .setPositiveButton("हाँ", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                proceedUpdate(mData.get(position), position);
//                            }
//                        })
//                        .setNegativeButton("नहीं", null)
//                        .show();
//            }
//        });
    }

    public void setDataToView(InspectionDetailsModel info, ViewHolder holder, int position){
            holder.tv_panchayat.setText(info.getPanchayatName());
            holder.tv_village.setText(info.getVillageName());
            holder.tv_command_area.setText(info.getApproxCommandArea());
            holder.tv_approx_amount.setText(info.getSchemeApproxAmt());

    }

//    public void deleteSaveData(int position, String type){
//        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
//        String id = String.valueOf(mData.get(position).getId());
//        long c = dataBaseHelper.resetNursuryBuildingUpdatedData(id);
//
//        if(c>0)
//        {
//            Toast.makeText(context, type+" Successfully",Toast.LENGTH_SHORT).show();
//            mData.remove(position);
//            notifyDataSetChanged();
//        }
//        else
//        {
//            Toast.makeText(context, "Failed to delete",Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void proceedUpdate(InspectionDetailsModel entity, int position){
//        if (Utiilties.isOnline(context)) {
//            new UploadDetail(entity, position).execute();
//        }
//        else {
//            Utiilties.showAlet(context);
//        }
//    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //TextView tv_name,tv_panchayat,tv_village,tv_ward,tv_area,tv_tree,tv_nursury_id;
        TextView tv_remove,tv_edit,tv_upload;
        TextView tv_panchayat,tv_village,tv_command_area,tv_approx_amount;

        ViewHolder(View itemView) {
            super(itemView);
            tv_panchayat = itemView.findViewById(R.id.tv_panchayat);
            tv_village = itemView.findViewById(R.id.tv_village);
            tv_command_area = itemView.findViewById(R.id.tv_command_area);
            tv_approx_amount = itemView.findViewById(R.id.tv_approx_amount);

            tv_remove = itemView.findViewById(R.id.tv_remove);
            tv_edit = itemView.findViewById(R.id.tv_edit);
            tv_upload = itemView.findViewById(R.id.tv_upload);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    InspectionDetailsModel getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


//    private class UploadDetail extends AsyncTask<String, Void, String> {
//        InspectionDetailsModel data;
//        int position;
//
//        private final ProgressDialog dialog = new ProgressDialog(context);
//
//        UploadDetail(InspectionDetailsModel data, int position) {
//            this.data = data;
//            this.position = position;
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            this.dialog.setCanceledOnTouchOutside(false);
//            this.dialog.setMessage("अपलोड हो राहा है...");
//            this.dialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... param) {
//
//            return WebServiceHelper.uploadNursuryDate(this.data);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (this.dialog.isShowing()) {
//                this.dialog.dismiss();
//            }
//
//            onGotResponse(result, position);
//        }
//    }

//    public void onGotResponse(String result, int position){
//        Log.d("Responsevalue",""+result);
//
//        if (result != null) {
//            if (result.equalsIgnoreCase("1")) {
//                deleteSaveData(position, "Uploaded");
//
//                chk_msg("डेटा अपलोड हो गया", "डेटा अपलोड हो गया");
//            }
//            else  if (result.equalsIgnoreCase("0")) {
//                Toast.makeText(context, "अपलोड विफल, कृपया बाद में पुनः प्रयास करें!", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        else {
//            Toast.makeText(context, "null record", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void chk_msg(String title, String msg) {
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setCancelable(false);
        ab.setIcon(R.drawable.logo1);
        ab.setTitle(title);
        Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        ab.setPositiveButton("ओके", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });

        ab.show();
    }

}
