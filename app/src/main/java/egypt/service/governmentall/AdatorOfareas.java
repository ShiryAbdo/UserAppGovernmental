package egypt.service.governmentall;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by falcon on 04/10/2017.
 */

public class AdatorOfareas    extends RecyclerView.Adapter<AdatorOfareas.ViewHolder> {
    private ArrayList<DataLocation> androidList;
    private Context context;
    private int lastPosition=-1;
       LatLng location;
    ArrayList<String> distance_nm ;

    public AdatorOfareas(ArrayList<DataLocation> android,Context c ,LatLng latLng ,ArrayList<String> distance) {
        this.androidList = android;
        this.context=c;
        this.location=latLng;
        this.distance_nm=distance;
    }

    @Override
    public AdatorOfareas.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout. row_paper, viewGroup, false);


        return new AdatorOfareas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdatorOfareas.ViewHolder viewHolder, final int i) {


        viewHolder.cintery_name.setText(androidList.get(i).getNameOfLocation());
                     viewHolder.position.setText("أعرض المسافه");

//        if(!distance_nm.isEmpty()){
//            viewHolder.position.setText(distance_nm.get(i));
//        }


        viewHolder.cintery_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri gmmIntentUri = Uri.parse("google.navigation:q="+androidList.get(i).getLatlangLocatio().latitude+","+androidList.get(i).getLatlangLocatio().longitude);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, gmmIntentUri);
                context.startActivity(intent);

            }
        });

        viewHolder.position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     Toast.makeText(context,distance_nm.get(i),Toast.LENGTH_LONG).show();
                viewHolder.position.setText(distance_nm.get(i));



//                Intent intent = new Intent(context,Test.class);
////                Toast.makeText(context,""+androidList.get(i).getLatlangLocatio().latitude,Toast.LENGTH_LONG).show();
//
//                intent.putExtra("LATITUDE_ID",androidList.get(i).getLatlangLocatio().latitude);
//                intent.putExtra("LONGITUDE_ID",androidList.get(i).getLatlangLocatio().longitude);
//                intent.putExtra("LATITUDE_ID_crountLocation",location.latitude);
////                intent.putExtra("LONGITUDE_ID_crountLocation",location.longitude);
//                context.startActivity(intent);

            }
        });
        setAnimation(viewHolder.card, i);

    }



    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return androidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView position,cintery_name,email_factory;
        private LinearLayout card;
        public ViewHolder(View view) {
            super(view);
            card=(LinearLayout)view.findViewById(R.id.cardView);
            cintery_name = (TextView)view.findViewById(R.id.title);
            position = (TextView)view.findViewById(R.id.position);

        }
    }

}