package com.example.john117.bottomtabtest;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment extends Fragment {
    ListView listView = null;
    private static final String TAG = MainActivity.class.getName();
    private Button btnRequest;
    private Button btnbar;
    //private TextView textView;
    private SearchView search;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String query;
    private String url = "https://api.nutritionix.com/v1_1/search";
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_search, container, false);
        AlertDialog.Builder alertdialog;
        ArrayList<String> arrayList ;
        arrayList = new ArrayList<String>();
        super.onCreateView(inflater,container,savedInstanceState);

        search = (SearchView)v.findViewById(R.id.search);
        btnRequest = (Button)v.findViewById(R.id.button3);
        btnbar = (Button)v.findViewById(R.id.button4);
        btnRequest.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              query = search.getQuery().toString();
                                              //dialog.setContentView(R.layout.dialog);
                                              //dialog.setTitle("Please wait");
                                              //dialog.show();
                                              sendAndRequestResponse();

                                          }
                                      }

        );
        btnbar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getActivity(),BarcodeSearchActivity.class);
                startActivity(i);
            }
        });
        return v;
    }
    public void onBarcodeClick(View view){
        Intent i = new Intent(getActivity(),BarcodeSearchActivity.class);
        startActivity(i);

    }
    private void sendAndRequestResponse() {
        try {
            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.listview,R.id.txt,items);
            final Dialog list = new Dialog(getActivity());
            list.setContentView(R.layout.listview);
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog);
            dialog.setTitle("Please wait");
            dialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JSONObject jsonBody = new JSONObject();
            final JSONArray JsonArray = new JSONArray();
            JsonArray.put("item_name");
            JsonArray.put("brand_name");
            JsonArray.put("nf_calories");
            JsonArray.put("upc");
            jsonBody.put("appId", "5e0b80fc");
            jsonBody.put("appKey", "2331f8e1b5b8e2e388c17fa9fa631c1f");
            jsonBody.put("query",query);
            final String fields = "[\"item_name\",\"brand_name\",\"upc\"]";
            jsonBody.put("fields",JsonArray);
            final String mRequestBody = jsonBody.toString();
            //textView.setText(query);
            final ArrayList<HashMap<String, String>> contactList;
            contactList = new ArrayList<>();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    listView = new ListView(getActivity());
                    int length;
                    dialog.dismiss();
                    String[] item_list = new String[10000];
                    int length_item;
                    try{
                        JSONObject JsonResponse = new JSONObject(response.toString());
                        JSONArray list = JsonResponse.getJSONArray("hits");
                        length = list.length();
                        for(int i=0;i<length ; i++)
                        {
                            JSONObject c = list.getJSONObject(i);
                            JSONObject product_info = c.getJSONObject("fields");
                            String product_name = product_info.getString("item_name");
                            String calories = product_info.getString("nf_calories");
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("Product_name", product_name);
                            contact.put("Calories", calories);
                            contactList.add(contact);
                        }
                        String[] items = new String[length];
                        for(int i=0;i<length;i++)
                        {
                            items[i] =" "  + contactList.get(i).get("Product_name") + "  " + contactList.get(i).get("Calories");
                        }
                        item_list = items;
                        length_item = length;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.listview,R.id.txt,items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                sharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                                String cal = sharedPreferences.getString("Calories","0");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                float calo = Float.parseFloat(cal);
                                float currcal = Float.parseFloat(contactList.get(position).get("Calories")) + calo;
                                editor.putString("Calories", "" + currcal);
                                editor.commit();
                                ViewGroup vg = (ViewGroup)view;
                                TextView txt = (TextView)vg.findViewById(R.id.txt);
                                Toast.makeText(getActivity(), "Logged in \n" + currcal,Toast.LENGTH_LONG).show();

                            }
                        });
                        String titleText = "Choose your meal";
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
                        ssBuilder.setSpan(
                                foregroundColorSpan,
                                0,
                                titleText.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        );

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setCancelable(true);
                        builder.setTitle(ssBuilder);
                        builder.setPositiveButton("Okay",null);
                        builder.setView(listView);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    //textView.setText("Inside response wtf");
                    //
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    //Toast.makeText(getApplicationContext(), error.toString(),
                            //Toast.LENGTH_LONG).show();
                    Log.e("LOG_VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    //textView.setText("Inside Content type");
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        //textView.setText("inside getBody");
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        //textView.setText("getBody Error");
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }


            };
            requestQueue.add(stringRequest);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

}