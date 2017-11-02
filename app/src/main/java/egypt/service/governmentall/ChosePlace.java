package egypt.service.governmentall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ChosePlace extends AppCompatActivity {
    Spinner spinner_gavernorate  ,spinner_place;
    ArrayList<String> categories_gavernorate  ,categories_places,catogersArea ,numberOfChoices;
    private DatabaseReference mDatabase;
    String typeService ,Tyep_OfTypeService;
    Bundle bundle;
    ArrayList<String>CairoPlaces;
    ArrayList<String>gizaPlaces;
    ArrayList<String>garbiaPlaces;
    ArrayList<String>sharqialaces;
    ArrayList<String>mansoraPlaces;
    String item  ,item_place  ,supAreaNmae;
    Button serch ;
    Spinner spinner_supAreas ;
    String value ;
    String nameCity ;
    String governorate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_place);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        serch=(Button)findViewById(R.id.serch);
        bundle=getIntent().getExtras();

        if (bundle!=null){

            typeService= bundle.getString("serviceName");
            Tyep_OfTypeService=bundle.getString("ChoseTypeService");
        }
        Toast.makeText(getApplicationContext(),typeService,Toast.LENGTH_LONG).show();
        spinner_gavernorate =(Spinner)findViewById(R.id.spinner_gavernorate);
        spinner_place =(Spinner)findViewById(R.id.spinner_place);
        spinner_supAreas=(Spinner)findViewById(R.id.spinner_supAreas);
        categories_gavernorate = new ArrayList<String>();
        categories_places=new ArrayList<>();
        CairoPlaces=new ArrayList<>();
        gizaPlaces=new ArrayList<>();
        garbiaPlaces=new ArrayList<>();
        catogersArea= new ArrayList<>();
        numberOfChoices= new ArrayList<>();
        categories_gavernorate.add("أختار  المحافظة");
        categories_places.add("إختار المدينة");
        catogersArea.add("إختار الحي");


        mDatabase.child("users").child("Governorate").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                        Governorate value = dataSnapshot1.getValue(Governorate.class);
                        String servisename =value.getGovernorateName();

                        categories_gavernorate.add(servisename);


                    }
                }else{
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




//        *************************************
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_gavernorate);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_gavernorate.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();



        spinner_gavernorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item


                  String name =  parent.getItemAtPosition(position).toString();
                if(name=="أختار  المحافظة"){
                    governorate=null;
                    Toast.makeText(getApplicationContext(), "لم يتم إختيار المحافظه" + item, Toast.LENGTH_LONG).show();


                }else{
                    governorate = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(getApplicationContext(),governorate,Toast.LENGTH_LONG).show();
                    categories_places.clear();
                    categories_places.add("إختار منطقة");
                    mDatabase.child("users").child("Governorate").child(name).child("citys").child("cityNameHash").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                 categories_places.clear();
                                categories_places.add("إختار المدينة");
                                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()) {
                                    StringBuilder spinnerBuffer = new StringBuilder();
                                    String servisename = dataSnapshot1.getKey();
                                    categories_places.add(servisename);
                                    if(categories_places.contains("cityNameHash"))
                                    {
                                        categories_places.remove("cityNameHash");
                                    }


                                }




                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                 }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
//                Toast.makeText(getApplicationContext(), "أختار محافظة", Toast.LENGTH_LONG).show();

                // TODO Auto-generated method stub
            }
        });


//        *************************
        ArrayAdapter<String> dataAdapter_two = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_places);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner_place.setAdapter(dataAdapter_two);
        dataAdapter_two.notifyDataSetChanged();



        spinner_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                  nameCity =  parent.getItemAtPosition(position).toString();
                if(nameCity=="إختار المدينة"){
                    item_place=null;
                    Toast.makeText(getApplicationContext(), "لم يتم إختيار منطقة" , Toast.LENGTH_LONG).show();


                } else{
                    item_place = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(getApplicationContext(),item_place,Toast.LENGTH_LONG).show();

                    numberOfChoices.add(nameCity);
                    catogersArea.clear();
                    catogersArea.add("إختار الحي");
                     mDatabase.child("users").child("Governorate").child(governorate).child("citys").child("cityNameHash").child(nameCity).child("areas").child("areasHs").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String servisename = dataSnapshot1.getKey();
                                    catogersArea.add(servisename);
                                    if (catogersArea.contains("areasHs")){
                                        catogersArea.remove("areasHs");
                                    }


                                }


                            }



                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                  }



                // Showing selected spinner item
//                Toast.makeText(parent.getContext(),  item_place, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
//                Toast.makeText(getApplicationContext(), "أختار منطقة"  , Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
            }
        });

        ArrayAdapter<String> dataAdapter_Three = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catogersArea);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner_supAreas.setAdapter(dataAdapter_Three);
        dataAdapter_Three.notifyDataSetChanged();



        spinner_supAreas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item

                String name  =  parent.getItemAtPosition(position).toString();
                if(name=="إختار الحي"){

                    supAreaNmae=null;
                                    Toast.makeText(parent.getContext(), "من فضلك قوم بإختيار الحي ", Toast.LENGTH_LONG).show();


                }else {
                    supAreaNmae = parent.getItemAtPosition(position).toString();
//                    Toast.makeText(parent.getContext(),supAreaNmae  , Toast.LENGTH_LONG).show();

                }


                // Showing selected spinner item
//                Toast.makeText(parent.getContext(),  item_place, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
//                Toast.makeText(getApplicationContext(), "أختار "  , Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
            }
        });


        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_place == null) {
                    Toast.makeText(getApplicationContext(), "أختار المدينة"  , Toast.LENGTH_LONG).show();

                } else if (governorate == null) {
                    Toast.makeText(getApplicationContext(), "أختار محافظة", Toast.LENGTH_LONG).show();


                }else{
//                    Toast.makeText(getApplicationContext(), governorate  , Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), item_place  , Toast.LENGTH_LONG).show();
//                    Toast.makeText(getApplicationContext(), supAreaNmae  , Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChosePlace.this,LocationActivity.class);
                                intent.putExtra("ChoseTypeService",Tyep_OfTypeService);
                                intent.putExtra("serviceName",typeService);
                                intent.putExtra("gavernorate",governorate);
                                intent.putExtra("area",item_place);
                                intent.putExtra("supArea",supAreaNmae);
                                startActivity(intent);
                                finish();
                }


            }
        });




    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(ChosePlace.this , WhatDoyouWantToKnow.class);
        setIntent.putExtra("ChoseTypeService",Tyep_OfTypeService);
        setIntent.putExtra("serviceName",typeService);

        startActivity(setIntent);
    }
}
