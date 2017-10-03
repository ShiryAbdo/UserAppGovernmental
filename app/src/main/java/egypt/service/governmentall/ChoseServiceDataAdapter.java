package egypt.service.governmentall;

import android.content.Context;
import android.content.Intent;
import android.graphics.EmbossMaskFilter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by falcon on 02/10/2017.
 */

public class ChoseServiceDataAdapter extends RecyclerView.Adapter<ChoseServiceDataAdapter.ViewHolder> {
    private ArrayList<String> androidList;
    private Context context;
    private int lastPosition=-1;

    public ChoseServiceDataAdapter(ArrayList<String> android,Context c) {
        this.androidList = android;
        this.context=c;
    }

    @Override
    public ChoseServiceDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout. row_layout, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoseServiceDataAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.cintery_name.setText(androidList.get(i));



        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,ChoseTypeService.class);

                intent.putExtra("serviceName",androidList.get(i));
                 context.startActivity(intent);
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
        private TextView factory_name,cintery_name,email_factory;
        private CardView card;
        public ViewHolder(View view) {
            super(view);
            card=(CardView)view.findViewById(R.id.cardView);
            cintery_name = (TextView)view.findViewById(R.id.title);

        }
    }

}