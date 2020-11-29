package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;

public class Udvah_Sinchaai_YojnaActivity extends Activity {
    Spinner sp_panchayat,sp_village,spn_jalshrot,spn_jalshrot_available,spn_motarpump_sanchalan;
    EditText edt_sincht_totArea_dec,edt_motor_power,edt_distribution_length,edt_distribution_pipe_inch,edt_distribution_pipe_meter,edt_apporx_command_area_hec,edt_yojna_lagat;
    ArrayList<VillageListEntity> VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    InspectionDetailsModel inspectionDetailsModel;
    String panchayat_Id="",Vill_Id="",Dist_Id="",BlockId="";
    String _edt_sincht_totArea_dec="",_edt_motor_power="",_edt_distribution_length="",_edt_distribution_pipe_inch="",_edt_distribution_pipe_meter="",_edt_apporx_command_area_hec="",_edt_yojna_lagat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udvah__sinchaai__yojna);
        dataBaseHelper= new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this);

        initialization();
    }
    public void initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_jalshrot=(Spinner)findViewById(R.id.spn_jalshrot);
        spn_jalshrot_available=(Spinner)findViewById(R.id.spn_jalshrot_available);
        spn_motarpump_sanchalan=(Spinner)findViewById(R.id.spn_motarpump_sanchalan);

        edt_sincht_totArea_dec=(EditText) findViewById(R.id.edt_sincht_totArea_dec);
        edt_motor_power=(EditText) findViewById(R.id.edt_motor_power);
        edt_distribution_length=(EditText) findViewById(R.id.edt_distribution_length);
        edt_distribution_pipe_inch=(EditText) findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_meter=(EditText) findViewById(R.id.edt_distribution_pipe_meter);
        edt_apporx_command_area_hec=(EditText) findViewById(R.id.edt_apporx_command_area_hec);
        edt_yojna_lagat=(EditText) findViewById(R.id.edt_yojna_lagat);
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

        id = new DataBaseHelper(Udvah_Sinchaai_YojnaActivity.this).InsertInspectionDetail(inspectionDetailsModel);

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
        _edt_sincht_totArea_dec=edt_sincht_totArea_dec.getText().toString();
        _edt_motor_power=edt_motor_power.getText().toString();
        _edt_distribution_length=edt_distribution_length.getText().toString();
        _edt_distribution_pipe_inch=edt_distribution_pipe_inch.getText().toString();
        _edt_distribution_pipe_meter=edt_distribution_pipe_meter.getText().toString();
        _edt_apporx_command_area_hec=edt_apporx_command_area_hec.getText().toString();
        _edt_yojna_lagat=edt_yojna_lagat.getText().toString();
    }
}
