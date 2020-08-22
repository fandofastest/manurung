package com.example.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ModeClass.DataModel;
import com.example.bluebank.Ads;
import com.example.bluebank.MainActivity;
import com.example.bluebank.R;
import com.example.bluebank.Tools;

import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.bluebank.MainActivity.klik;


/*created by khilji 28/11/2019*/


public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder> {
    Context context;


    private List<DataModel> lisdata;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mainitem;
        TextView title,cat;
        ImageView ivMain;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            ivMain = (ImageView) view.findViewById(R.id.image);
            mainitem=view.findViewById(R.id.mainitem);
            cat=view.findViewById(R.id.cat);



        }

    }


    public RvAdapter(Context context, List<DataModel> lisdata) {
        this.lisdata = lisdata;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        final DataModel dataModel = lisdata.get(position);
        holder.title.setText(dataModel.getTitle());
        holder.cat.setText(dataModel.getCategory());
        Glide   .with(context)
                .load(dataModel.getGambar())
                .centerInside()
                .placeholder(R.drawable.blue_rect)
                .into(holder.ivMain);


        holder.mainitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klik=klik+1;
                if ((klik % 2 )==0){

                    Ads ads = new Ads();
                    if (Tools.ads.equals("admob")){
                        ads.showinter(context,Tools.admobinter);
                    }

                    else {
                        ads.showinterfb(context,Tools.faninter);
                    }
                    ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                        @Override
                        public void onAdsfinish() {
                            ((MainActivity)context).showdetail(dataModel);
                        }

                        @Override
                        public void onRewardOk() {

                        }
                    });
                }

                else {
                    ((MainActivity)context).showdetail(dataModel);
                }




            }
        });







    }

    @Override
    public int getItemCount() {
        return lisdata.size();
    }



}


