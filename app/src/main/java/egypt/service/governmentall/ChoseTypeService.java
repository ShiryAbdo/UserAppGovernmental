package egypt.service.governmentall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChoseTypeService extends AppCompatActivity {
    RecyclerView recyclerView ;
    ArrayList<String> data ;
    ChoseServiceTypeDataAdapter adapter ;
    DatabaseReference mDatabase;
    Bundle bundle;
    String typeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_type_service);
        bundle=getIntent().getExtras();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        data= new ArrayList<>();
        recyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);


        if (bundle!=null){

            typeService= bundle.getString("serviceName");
         }
        Toast.makeText(getApplicationContext(), typeService, Toast.LENGTH_SHORT).show();

        mDatabase.child("users").child("Service").child(typeService).child("typeOfSerVICE").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                        String name =dataSnapshot1.getKey();

                        data.add(name);
                        adapter = new ChoseServiceTypeDataAdapter(data,ChoseTypeService.this ,typeService);
                        recyclerView.setAdapter(adapter);


                    }
                }else{
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
