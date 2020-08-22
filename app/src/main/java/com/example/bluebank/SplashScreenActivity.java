package com.example.bluebank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashScreenActivity extends AppCompatActivity {
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        hud = KProgressHUD.create(SplashScreenActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading")
                .setDetailsLabel("Please Wait")
                .setMaxProgress(100)
                .show();

        getstatus();


    }


    public void getstatus(){
        String url=Tools.urlstatus;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {




                try {


                    Tools.redirect = response.getString("redirect");
                    Tools.pops = response.getString("pops");
                    Tools.apk = response.getString("apk");
                    Tools.ads = response.getString("ads");
                    Tools.admobbanner = response.getString("admobbanner");
                    Tools.admobinter = response.getString("admobinter");
                    Tools.admobreward = response.getString("admobreward");
                    Tools.fanbanner = response.getString("fanbanner");
                    Tools.faninter = response.getString("faninter");
                    Tools.popstitle = response.getString("popstitle");
                    Tools.popsdesc = response.getString("popsdesc");
                    Tools.popsimage = response.getString("popsimage");
                    Tools.api = response.getString("api");
                    Button button= findViewById(R.id.button_start);

                    button.setVisibility(View.VISIBLE);
                    hud.dismiss();

                    button.setOnClickListener(v -> {
                        Ads ads = new Ads();
                        if (Tools.ads.equals("admob")){
                            ads.showinter(SplashScreenActivity.this,Tools.admobinter);
                        }
                        else {
                            ads.showinterfb(SplashScreenActivity.this,Tools.faninter);
                        }

                        ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                            @Override
                            public void onAdsfinish() {
                                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

                                startActivity(i);

                                // close this activity

                                finish();
                            }

                            @Override
                            public void onRewardOk() {

                            }
                        });

                    });

                    if (Tools.redirect.equals("y")){

                     showdialogredirect(Tools.apk);
                        button.setVisibility(View.GONE);

                    }





                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err",error.getMessage());

            }
        });

        Volley.newRequestQueue(SplashScreenActivity.this).add(jsonObjectRequest);


    }

    private void  showdialogredirect(String appupdate){
        new SweetAlertDialog(SplashScreenActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("App Was Discontinue")
                .setContentText("Please Install Our New Music App")
                .setConfirmText("Install")

                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Install From Playstore")
                            .setContentText("Please Wait, Open Playstore")
                            .setConfirmText("Go")


                            .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(
                                "https://play.google.com/store/apps/details?id="+appupdate));
                        intent.setPackage("com.android.vending");
                        startActivity(intent);
//                                Do something after 100ms
                    }, 3000);



                })
                .show();
    }





}
