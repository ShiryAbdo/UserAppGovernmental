package egypt.service.governmentall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    ArrayList<String> categories_gavernorate  ,categories_places;
    private DatabaseReference mDatabase;
    String typeService ,Tyep_OfTypeService;
    Bundle bundle;
    ArrayList<String>CairoPlaces;
    ArrayList<String>gizaPlaces;
    ArrayList<String>garbiaPlaces;
    ArrayList<String>sharqialaces;
    ArrayList<String>mansoraPlaces;
    String item  ,item_place;
    Button serch ;

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
        spinner_gavernorate =(Spinner)findViewById(R.id.spinner_gavernorate);
        spinner_place =(Spinner)findViewById(R.id.spinner_place);
        categories_gavernorate = new ArrayList<String>();
        categories_places=new ArrayList<>();
        CairoPlaces=new ArrayList<>();
        gizaPlaces=new ArrayList<>();
        garbiaPlaces=new ArrayList<>();
        categories_gavernorate.add("أختار  المحافظة");
        categories_gavernorate.add("القاهره");
        categories_gavernorate.add("الجيزه");
        categories_gavernorate.add("الشرقية");
        categories_gavernorate.add("الدقهلية");
        categories_gavernorate.add("المنصورة");
        categories_gavernorate.add("قنا");
        categories_gavernorate.add("سهاج");
        categories_gavernorate.add("دمياط");
        categories_gavernorate.add("الغربية");


//        ***********************************************


         garbiaPlaces.add("طنطا قسم أول ");
        garbiaPlaces.add("طنطا قسم ثاني ");
        garbiaPlaces.add("المحله");
        garbiaPlaces.add("زفتي");
        garbiaPlaces.add("المحله الكبري");
        garbiaPlaces.add("محله روح");


        CairoPlaces.add("مدينة نصر");
        CairoPlaces.add("مصر الجديدة");
        CairoPlaces.add("المعادي");
        CairoPlaces.add("حدائق الزتون");
        categories_places.add("إختار منطقة");

//        *************************************
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_gavernorate);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner_gavernorate.setAdapter(dataAdapter);



        spinner_gavernorate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item = parent.getItemAtPosition(position).toString();

                  String name =  parent.getItemAtPosition(position).toString();
                if(name=="أختار  المحافظة"){
                    Toast.makeText(getApplicationContext(), "لم يتم إختيار المحافظه" + item, Toast.LENGTH_LONG).show();

                }else if(name=="القاهره") {
                    categories_places.clear();
                    categories_places.addAll(CairoPlaces);

                }else if(name=="الغربية") {

                    categories_places.clear();
                    categories_places.addAll(garbiaPlaces);

                }else{
                    categories_places.clear();
                    Toast.makeText(getApplicationContext(), "أختار محافظة", Toast.LENGTH_LONG).show();
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {

                // TODO Auto-generated method stub
            }
        });


//        *************************
        ArrayAdapter<String> dataAdapter_two = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories_places);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        // attaching data adapter to spinner
        spinner_place.setAdapter(dataAdapter_two);



        spinner_place.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                item_place = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(),  item_place, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                Toast.makeText(getApplicationContext(), "أختار منطقة" + item, Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub
            }
        });
        serch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChosePlace.this,ShowNearPLACES.class);
                intent.putExtra("ChoseTypeService",Tyep_OfTypeService);
                intent.putExtra("serviceName",typeService);
                intent.putExtra("gavernorate",item);
                intent.putExtra("area",item_place);
                startActivity(intent);

            }
        });




    }
}
