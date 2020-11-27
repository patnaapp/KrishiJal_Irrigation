package bih.in.krishijal_irrigation.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bih.in.krishijal_irrigation.R;

public class MainActivity extends Activity implements View.OnClickListener {
    RelativeLayout rl_nalkup_new,rl_nalkup_edit,rl_nalkup_upload,rl_udvah_new,rl_udvah_edit,rl_udvah_upload,rl_aahar_new,rl_aahar_edit,rl_aahar_upload;
    TextView nalkup_edit,nalkup_upload,udvah_edit,udvah_upload,aahar_edit,aahar_upload,tv_username,tv_district,tv_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inilization();
        rl_nalkup_new.setOnClickListener(this);
    }
    public void inilization(){
        rl_nalkup_new=(RelativeLayout)findViewById(R.id.rl_nalkup_new);
        rl_nalkup_edit=(RelativeLayout)findViewById(R.id.rl_nalkup_edit);
        rl_nalkup_upload=(RelativeLayout)findViewById(R.id.rl_nalkup_upload);
        rl_udvah_new=(RelativeLayout)findViewById(R.id.rl_udvah_new);
        rl_udvah_edit=(RelativeLayout)findViewById(R.id.rl_udvah_edit);
        rl_udvah_upload=(RelativeLayout)findViewById(R.id.rl_udvah_upload);
        rl_aahar_new=(RelativeLayout)findViewById(R.id.rl_aahar_new);
        rl_aahar_edit=(RelativeLayout)findViewById(R.id.rl_aahar_edit);
        rl_aahar_upload=(RelativeLayout)findViewById(R.id.rl_aahar_upload);

        nalkup_edit=(TextView)findViewById(R.id.nalkup_edit);
        nalkup_upload=(TextView)findViewById(R.id.nalkup_upload);
        udvah_edit=(TextView)findViewById(R.id.udvah_edit);
        udvah_upload=(TextView)findViewById(R.id.udvah_upload);
        aahar_edit=(TextView)findViewById(R.id.aahar_edit);
        aahar_upload=(TextView)findViewById(R.id.aahar_upload);

        tv_username=(TextView)findViewById(R.id.tv_username);
        tv_district=(TextView)findViewById(R.id.tv_district);
        tv_block=(TextView)findViewById(R.id.tv_block);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.rl_nalkup_new)
        {
            Intent intent=new Intent(getApplicationContext(),Nalkup_Sinchaai_YojyaActivity.class);
            startActivity(intent);
            //handle multiple view click events
        }


    }
}
