package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;

public class Nalkup_Sinchaai_YojyaActivity extends AppCompatActivity {
    LinearLayout ll;
    Spinner sp_panchayat,sp_village,sp_option_power,sp_dist_khatakhesra;
    EditText edt_no_of_nalkup,edt_pole_length,edt_pipe_Perimeter_inch,edt_pipe_lingth_meter,edt_apporx_command_area_hec,edt_yojna_price;
    ArrayList<VillageListEntity>VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    InspectionDetailsModel inspectionDetailsModel;
    String panchayat_Id="",Vill_Id="",Dist_Id="",BlockId="";
    String _edt_no_of_nalkup="",_edt_pole_length="",_edt_pipe_Perimeter_inch="",_edt_pipe_lingth_meter="",_edt_apporx_command_area_hec="",_edt_yojna_price="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nalkup__sinchaai__yojya);
        Initialization();
        dataBaseHelper=new DataBaseHelper(getApplicationContext());
    }


    private void Initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        sp_option_power=(Spinner)findViewById(R.id.sp_option_power);
        sp_dist_khatakhesra=(Spinner)findViewById(R.id.sp_dist_khatakhesra);
        edt_no_of_nalkup=(EditText)findViewById(R.id.edt_no_of_nalkup);
        edt_pole_length=(EditText)findViewById(R.id.edt_pole_length);
        edt_pipe_Perimeter_inch=(EditText)findViewById(R.id.edt_pipe_Perimeter_inch);
        edt_pipe_lingth_meter=(EditText)findViewById(R.id.edt_pipe_lingth_meter);
        edt_apporx_command_area_hec=(EditText)findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_price=(EditText)findViewById(R.id.edt_yojna_price);
    }


    private void InsertData(){
        long id = 0;
        inspectionDetailsModel.setDistCode(Dist_Id);
        inspectionDetailsModel.setBlockCode(BlockId);
        inspectionDetailsModel.setPanchayatCode(panchayat_Id);
        inspectionDetailsModel.setVILLCODE(Vill_Id);

        // inspectionDetailsModel.setDistributionChannelLength(Flag_IsDataWrong);

        String userid= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("USER_ID", "");
       // inspectionDetailsModel.set_EntryBy(userid.toLowerCase());

        id = new DataBaseHelper(Nalkup_Sinchaai_YojyaActivity.this).InsertInspectionDetail(inspectionDetailsModel);

        if (id > 0) {
            Toast.makeText(getApplicationContext(), "डेटा सफलतापूर्वक सहेजा गया", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "डेटा सहेजा नहीं गया", Toast.LENGTH_LONG).show();
        }
    }



    private void setVillage(String blockcode)
    {
        VillageList = dataBaseHelper.getVillageList(blockcode);
        ArrayList<String> VillageListString = new ArrayList<>();
        ArrayList<String> VillageListint = new ArrayList<>();

        if (VillageList.size() > 0)
        {
            VillageListString.add("--Choose--");
            VillageListint.add("--Choose--");
        }

        for (int i = 0; i < VillageList.size(); i++)
        {
            VillageListString.add(VillageList.get(i).getVillName());
            VillageListint.add(VillageList.get(i).getVillCode());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, VillageListString);
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, VillageListint);
        sp_village.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_village.setSelection(((ArrayAdapter<String>)spinnerAdapter1).getPosition(Village_code));

        }

    }
    private void setPanchayat(String blockcode)
    {
        PanchayatList = dataBaseHelper.getPanchayt(blockcode);
        ArrayList<String> PanchayatListString = new ArrayList<>();
        ArrayList<String> PanchayatListint = new ArrayList<>();

        if (PanchayatList.size() > 0)
        {
            PanchayatListString.add("--Choose--");
            PanchayatListint.add("--Choose--");
        }

        for (int i = 0; i < VillageList.size(); i++)
        {
            PanchayatListString.add(VillageList.get(i).getVillName());
            PanchayatListint.add(VillageList.get(i).getVillCode());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PanchayatListString);
        ArrayAdapter<String> spinnerAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, PanchayatListint);
        sp_panchayat.setAdapter(spinnerAdapter);
        String Village_code = "";
        Village_code = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("Spin_Vill_Code", "");

        if (!Village_code.equalsIgnoreCase(""))
        {

            sp_panchayat.setSelection(((ArrayAdapter<String>)spinnerAdapter1).getPosition(Village_code));

        }

    }
    public void setvalue(){
        _edt_no_of_nalkup=edt_no_of_nalkup.getText().toString();
        _edt_pole_length=edt_pole_length.getText().toString();
        _edt_pipe_Perimeter_inch=edt_pipe_Perimeter_inch.getText().toString();
        _edt_pipe_lingth_meter=edt_pipe_lingth_meter.getText().toString();
        _edt_apporx_command_area_hec=edt_apporx_command_area_hec.getText().toString();
        _edt_yojna_price=edt_yojna_price.getText().toString();

    }

}
