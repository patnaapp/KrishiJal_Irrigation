package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import bih.in.krishijal_irrigation.R;
import bih.in.krishijal_irrigation.database.DataBaseHelper;
import bih.in.krishijal_irrigation.entity.InspectionDetailsModel;
import bih.in.krishijal_irrigation.entity.PanchayatData;
import bih.in.krishijal_irrigation.entity.VillageListEntity;
import bih.in.krishijal_irrigation.utility.GlobalVariables;
import bih.in.krishijal_irrigation.utility.Utiilties;

public class Aahar_Sinchaai_YojyaActivity extends Activity {
    Spinner sp_panchayat,sp_village,spn_water_available;
    EditText edt_pipe_length,edt_distribution_pipe_inch,edt_distribution_pipe_lambai,edt_command_area,edt_yojna_lagat;
    Button btn_aahar_location;
    LocationManager mlocManager = null;
    static Location LastLocation = null;
    private final int UPDATE_LATLNG = 2;
    private final int UPDATE_ADDRESS = 1;
    private ProgressDialog dialog;
    ArrayList<VillageListEntity> VillageList=new ArrayList<>();
    ArrayList<PanchayatData>PanchayatList=new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    InspectionDetailsModel inspectionDetailsModel;
    String panchayat_Id="",Vill_Id="",Dist_Id="",BlockId="";
    String _edt_pipe_length="",_edt_distribution_pipe_inch="",_edt_distribution_pipe_lambai="",_edt_command_area="",_edt_yojna_lagat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aahar__sinchaai__yojya);
        dataBaseHelper= new DataBaseHelper(Aahar_Sinchaai_YojyaActivity.this);

        initialization();
    }
    public  void initialization(){
        sp_panchayat=(Spinner)findViewById(R.id.sp_panchayat);
        sp_village=(Spinner)findViewById(R.id.sp_village);
        spn_water_available=(Spinner)findViewById(R.id.spn_water_available);

        edt_pipe_length=(EditText)findViewById(R.id.edt_pipe_length);
        edt_distribution_pipe_inch=(EditText)findViewById(R.id.edt_distribution_pipe_inch);
        edt_distribution_pipe_lambai=(EditText)findViewById(R.id.edt_distribution_pipe_lambai);
        edt_command_area=(EditText)findViewById(R.id.edt_command_area);
        edt_yojna_lagat=(EditText)findViewById(R.id.edt_yojna_lagat);

        btn_aahar_location=(Button) findViewById(R.id.btn_aahar_location);

        btn_aahar_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //locationpoint="1";
                //locationManager();
                //getLocation();
            }
        });
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

        id = new DataBaseHelper(Aahar_Sinchaai_YojyaActivity.this).InsertInspectionDetail(inspectionDetailsModel);

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
        _edt_pipe_length=edt_pipe_length.getText().toString();
        _edt_distribution_pipe_inch=edt_distribution_pipe_inch.getText().toString();
        _edt_distribution_pipe_lambai=edt_distribution_pipe_lambai.getText().toString();
        _edt_command_area=edt_command_area.getText().toString();
        _edt_yojna_lagat=edt_yojna_lagat.getText().toString();
    }

}
