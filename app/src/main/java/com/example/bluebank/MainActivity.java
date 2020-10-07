package com.example.bluebank;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.example.Adapter.RvAdapter;
import com.example.ModeClass.DataModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import guy4444.smartrate.SmartRate;
import id.fando.GDPRChecker;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EndlessRecyclerViewScrollListener scrollListener;


    private RecyclerView recyclerView;
    private RvAdapter mAdapter;
    public static int klik=0;
    ArrayAdapter<String> dataAdapter;

    List<DataModel> listdata= new ArrayList<>();
    public static String DOWNLOAD_DIRECTORY="/Download";
    public static String GAMEDIR="/games/com.mojang/minecraftpe";
    private DrawerLayout drawer;
    private ImageView navigationView;
    ImageView imageView;
    FloatingActionButton floatingActionButton;
    EditText search;
    TextView nf;
    boolean hasload=false;
    KProgressHUD hud;
    private static  String[] CATEGORY = new String[]{"null"};
    Spinner category;
    List<String> listcat = new ArrayList<String>();

    LinearLayout llOne,llSecond,llThird,llFourth,llFifth,llSixth,banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new GDPRChecker()
                .withContext(getApplicationContext())
                .withActivity(MainActivity.this)
                .withAppId(Tools.appid)
                .withDebug()
                .check();




        if (Tools.pops.equals("y")){
            showPopAds();
        }

        category = (Spinner) findViewById(R.id.sp_city);
        dataAdapter= new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.spinner_text,listcat);
        category.setAdapter(dataAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    getdata("home","null",1,false);

                }
               else{
                    getdata("cat",listcat.get(position),1,false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        nf=findViewById(R.id.nf);
        banner=findViewById(R.id.banner_container);

        Display display = getWindowManager().getDefaultDisplay();

        Ads ads = new Ads();
        ads.ShowBannerAds(MainActivity.this,banner,Tools.fanbanner,Tools.admobbanner,display);

//
//        llOne = findViewById(R.id.llOne);
//        llSecond = findViewById(R.id.llSecond);
//        llThird = findViewById(R.id.llThird);
//        llFourth = findViewById(R.id.llFourth);
//        llFifth = findViewById(R.id.llFifth);
//        llSixth = findViewById(R.id.llSixth);
//        floatingActionButton=findViewById(R.id.floating_action_button);
        search=findViewById(R.id.search);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode==KEYCODE_ENTER){
                    getdata("search",search.getText().toString(),1,false);
                }
                return false;
            }
        });
//
//        llOne.setOnClickListener(this);
//        llSecond.setOnClickListener(this);
//        llThird.setOnClickListener(this);
//        llFourth.setOnClickListener(this);
//        llFifth.setOnClickListener(this);
//        llSixth.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }


//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCategory();
//            }
//        });


        navigationView =  findViewById(R.id.navigation_menu);
        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,navigationView);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Log.e("item",item.toString());

                        if (item.getTitle().equals("Privacy")){
                            showTermServicesDialog();
                        }else  if (item.getTitle().equals("GDPR")){
                            showGDPR();
                        }else  if (item.getTitle().equals("Share")){
                            try {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.setType("text/plain");
                                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                                String shareMessage= "\nLet me recommend you this application\n\n";
                                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                startActivity(Intent.createChooser(shareIntent, "choose one"));
                            } catch(Exception e) {
                                //e.toString();
                            }
                        }

                        return true;
                    }
                });
                popupMenu.inflate( R.menu.menu);
                popupMenu.show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        recyclerView = (RecyclerView)findViewById(R.id.rvTransaction);


        mAdapter = new RvAdapter(MainActivity.this,listdata);

        GridLayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this,3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                getdata("home","null",page,true);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        getdata("home","null",1,false);
        getlistcat();
    }


    public void openDrawer() {
        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawer(navigationView);
        } else {
            drawer.openDrawer(navigationView);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.llOne:


                llOne.setBackgroundColor(Color.parseColor("#fafafa"));
                llSecond.setBackgroundColor(Color.parseColor("#ffffff"));
                llThird.setBackgroundColor(Color.parseColor("#ffffff"));
                llFourth.setBackgroundColor(Color.parseColor("#ffffff"));
                llFifth.setBackgroundColor(Color.parseColor("#ffffff"));
                llSixth.setBackgroundColor(Color.parseColor("#ffffff"));

                break;


            case R.id.llSecond:

                llOne.setBackgroundColor(Color.parseColor("#ffffff"));
                llSecond.setBackgroundColor(Color.parseColor("#fafafa"));
                llThird.setBackgroundColor(Color.parseColor("#ffffff"));
                llFourth.setBackgroundColor(Color.parseColor("#ffffff"));
                llFifth.setBackgroundColor(Color.parseColor("#ffffff"));
                llSixth.setBackgroundColor(Color.parseColor("#ffffff"));


                break;

            case R.id.llThird:


                llOne.setBackgroundColor(Color.parseColor("#ffffff"));
                llSecond.setBackgroundColor(Color.parseColor("#ffffff"));
                llThird.setBackgroundColor(Color.parseColor("#fafafa"));
                llFourth.setBackgroundColor(Color.parseColor("#ffffff"));
                llFifth.setBackgroundColor(Color.parseColor("#ffffff"));
                llSixth.setBackgroundColor(Color.parseColor("#ffffff"));


                break;


            case R.id.llFourth:

                llOne.setBackgroundColor(Color.parseColor("#ffffff"));
                llSecond.setBackgroundColor(Color.parseColor("#ffffff"));
                llThird.setBackgroundColor(Color.parseColor("#ffffff"));
                llFourth.setBackgroundColor(Color.parseColor("#fafafa"));
                llFifth.setBackgroundColor(Color.parseColor("#ffffff"));
                llSixth.setBackgroundColor(Color.parseColor("#ffffff"));


                break;


            case R.id.llFifth:


                llOne.setBackgroundColor(Color.parseColor("#ffffff"));
                llSecond.setBackgroundColor(Color.parseColor("#ffffff"));
                llThird.setBackgroundColor(Color.parseColor("#ffffff"));
                llFourth.setBackgroundColor(Color.parseColor("#ffffff"));
                llFifth.setBackgroundColor(Color.parseColor("#fafafa"));
                llSixth.setBackgroundColor(Color.parseColor("#ffffff"));

                break;


            case R.id.llSixth:


                llOne.setBackgroundColor(Color.parseColor("#ffffff"));
                llSecond.setBackgroundColor(Color.parseColor("#ffffff"));
                llThird.setBackgroundColor(Color.parseColor("#ffffff"));
                llFourth.setBackgroundColor(Color.parseColor("#ffffff"));
                llFifth.setBackgroundColor(Color.parseColor("#ffffff"));
                llSixth.setBackgroundColor(Color.parseColor("#fafafa"));

                break;
        }
    }

        public void getdata(String type,String cat,int page,boolean next){
            if (next){

            }

            else {
                listdata.clear();
                recyclerView.removeAllViews();
            }


            String url;
            if (type.equals("home")){
                url=Tools.api+"home.php?page="+page+"&limit=100";
            }
            else if (type.equals("cat")){
                url =Tools.api+"det_category.php?category="+cat+"&limit=20";
            }

            else {
                url=Tools.api+"search.php?keyword="+cat+"&limit=100";

            }

            Log.e("url",url);


            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray1=response.getJSONArray("skin");


                        if (jsonArray1.length()>0){

                            nf.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0;i<jsonArray1.length();i++){
                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                            DataModel dataModel = new DataModel();
                            dataModel.setId(jsonObject.getInt("id"));
                            dataModel.setCategory(jsonObject.getString("category"));
                            dataModel.setTitle(jsonObject.getString("title"));
                            dataModel.setGambar(jsonObject.getString("gambar"));
                            dataModel.setPreview_3d(jsonObject.getString("preview_3d"));
                            dataModel.setUrl_download(jsonObject.getString("url_download"));
                            dataModel.setUrl_apply(jsonObject.getString("url_apply"));
                            dataModel.setVote(jsonObject.getInt("vote"));
                            dataModel.setFavorite(jsonObject.getInt("favorite"));
                            listdata.add(dataModel);




                        }
                        }

                        else {

                            if (next){

                            }

                            else {
                                nf.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("err11", error.getMessage());

                }
            });

            Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);


    }
    public void getlistcat(){

        String url=Tools.api+"category.php";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray1=response.getJSONArray("skin");
                    listcat.add("Home");
                    for (int i = 0;i<jsonArray1.length();i++){
                        JSONObject jsonObject = jsonArray1.getJSONObject(i);
                        String cat = jsonObject.getString("category");
                        listcat.add(cat);





                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                dataAdapter.notifyDataSetChanged();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("errcat", error.getMessage());

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(jsonObjectRequest);


    }

    private WebResourceResponse getTextWebResource(InputStream data) {
        return new WebResourceResponse("text/plain", "UTF-8", data);
    }
    @SuppressLint("SetJavaScriptEnabled")
    public void showdetail(final DataModel dataModel) {

        hasload=false;

        final Dialog dialog = new Dialog(this);
        hud = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setCancellable(true)
                .setDetailsLabel("Please Wait")
                .setMaxProgress(100);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.detail_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView textView=  dialog.findViewById(R.id.titledetail);
        TextView cat=  dialog.findViewById(R.id.category);
        TextView fav=dialog.findViewById(R.id.totalfav);
        TextView vote =dialog.findViewById(R.id.totalvote);

        fav.setText(String.valueOf(dataModel.getFavorite()));
        vote.setText(String.valueOf(dataModel.getVote()));
        cat.setText(dataModel.getCategory());
        final ImageView imageView =dialog.findViewById(R.id.imagedetail);
        Glide.with(this)
                .load(dataModel.getGambar())
                .fitCenter()
                .placeholder(R.drawable.blue_rect)
                .into(imageView);

        final WebView webView;
        webView = dialog.findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
               hasload=true;
                hud.dismiss();
                // do your stuff here
            }
//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view,     String url) {
//                if(url.contains("google")||url.contains("facebook")){
//                    InputStream textStream = new ByteArrayInputStream("".getBytes());
//                    return getTextWebResource(textStream);
//                }
//                return super.shouldInterceptRequest(view, url);
//            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(dataModel.getPreview_3d());



        Log.e("vis", String.valueOf(webView.getVisibility()));


        textView.setText(dataModel.getTitle());
        dialog.findViewById(R.id.bt_dl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Ads ads = new Ads();


                    Log.e("eee","genap");
                    ads.showreward(MainActivity.this,Tools.admobreward);

                    ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                        @Override
                        public void onAdsfinish() {
                            Toast.makeText(MainActivity.this,"Failed To download, You must finish Watch Ads",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onRewardOk() {
                            Tools.downloadfile(MainActivity.this,dataModel.getUrl_download(),DOWNLOAD_DIRECTORY,dataModel.getTitle(),true);
                            Toast.makeText(MainActivity.this,"Download Starting",Toast.LENGTH_LONG).show();

                        }
                    });







            }
        });
        dialog.findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ads ads = new Ads();


                Log.e("eee","genap");
                ads.showreward(MainActivity.this,Tools.admobreward);

                ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                    @Override
                    public void onAdsfinish() {
                        Toast.makeText(MainActivity.this,"Failed To download, You must finish Watch Ads",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onRewardOk() {
                        Tools.downloadfile(MainActivity.this,dataModel.getUrl_download(),GAMEDIR,dataModel.getTitle(),false);
                        Toast.makeText(MainActivity.this,"Download Starting",Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
        dialog.findViewById(R.id.bt_prev).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {




                if (webView.getVisibility()==8){
                    if (!hasload){
                        hud.show();
                    }


                    imageView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }

                else{
                    imageView.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                }



            }
        });

        dialog.show();
    }

    String single_choice_selected = CATEGORY[0];
//    private void showCategory() {
//
//       final View parent_view = findViewById(android.R.id.content);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Category");
//        builder.setSingleChoiceItems(CATEGORY, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                single_choice_selected = CATEGORY[i];
//            }
//        });
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                getdata("cat",single_choice_selected);
//            }
//        });
//        builder.setNegativeButton("CANCEL", null);
//        builder.show();
//    }

        private void showPopAds() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_header_polygon);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        ImageView imageView =dialog.findViewById(R.id.pops);
        TextView title,desc;
        title=dialog.findViewById(R.id.popstitle);
        desc=dialog.findViewById(R.id.popsdesc);
        title.setText(Tools.popstitle);
        desc.setText(Tools.popsdesc);


        Glide
                .with(MainActivity.this)
                .load(Tools.popsimage)
                .centerCrop()
                .placeholder(R.drawable.bg_polygon)
                .into(imageView);

        ((Button) dialog.findViewById(R.id.bt_install)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Install From Playstore");
                pDialog .setContentText("Please Wait, Open Playstore");
                pDialog  .setConfirmText("Go");
                pDialog .show();
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    pDialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(
                            "https://play.google.com/store/apps/details?id="+Tools.apk));
                    intent.setPackage("com.android.vending");
                    startActivity(intent);

//                                Do something after 100ms
                }, 3000);



            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    exitdialog();

    }

    public void  exitdialog(){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle(R.string.quittitle)
                .setMessage(R.string.quitmessage)

                .addButton(getString(R.string.quitbutton), -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        finish();

                    }
                })
                .addButton(getString(R.string.rate), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showrate();

                    }
                });

// Show the alert
        builder.show();
    }


    public void showrate(){

        SmartRate.Rate(MainActivity.this
                , "Rate Us"
                , "Tell others what you think about this app"
                , "Continue"
                , "Please take a moment and rate us on Google Play"
                , "click here"
                , "Cancel"
                , "Thanks for the feedback"
                , Color.parseColor("#2196F3")
                , 4
        );

    }

    private void showTermServicesDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_term_of_services);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "You Accept TOS", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showGDPR() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_gdpr_basic);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
