package egypt.service.governmentall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WhatDoyouWantToKnow extends AppCompatActivity {
    Button paperWanted  ,nearPlace;
    String typeService ,Tyep_OfTypeService;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_doyou_want_to_know);
        bundle=getIntent().getExtras();
        if (bundle!=null){

            typeService= bundle.getString("serviceName");
            Tyep_OfTypeService=bundle.getString("ChoseTypeService");
        }
        Toast.makeText(getApplicationContext(), typeService, Toast.LENGTH_SHORT).show();
        paperWanted =(Button)findViewById(R.id.paperWanted);
        paperWanted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WhatDoyouWantToKnow.this,PapersActivity.class);
                intent.putExtra("ChoseTypeService",Tyep_OfTypeService);
                intent.putExtra("serviceName",typeService);
                startActivity(intent);
            }
        });
        nearPlace=(Button)findViewById(R.id.nearPlace);
        nearPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(WhatDoyouWantToKnow.this,ChosePlace.class);
                intent.putExtra("ChoseTypeService",Tyep_OfTypeService);
                intent.putExtra("serviceName",typeService);
                startActivity(intent);

            }
        });
    }
}
