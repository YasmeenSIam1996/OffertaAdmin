package com.example.offertaadmin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.offertaadmin.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignIn;
    EditText txtEmail, txtPassword;

    ProgressDialog progress;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preferences.edit();

        if (preferences.contains("token")) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }
        findViews();
    }

    private void findViews() {

        btnSignIn = findViewById(R.id.btnSignIn);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnSignIn.setOnClickListener(this);

    }

    public void loginRequest() {
        showProgress();

        final StringRequest request = new StringRequest(Request.Method.POST, Constants.LOGIN,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            JSONObject loginObject = new JSONObject(response);

                            Log.e("response", loginObject.getBoolean("status") + "");
                            Log.e("response", loginObject.getString("message") + "");
                            Log.e("response", loginObject.getJSONObject("data").toString());


                            JSONObject data = loginObject.getJSONObject("data");

                            if (loginObject.getBoolean("status")) {

                                editor.putString("name", data.getString("name")).apply();
                                editor.putString("email", data.getString("email")).apply();
                                editor.putString("phone", data.getString("phone")).apply();
                                editor.putString("token", data.getString("token")).apply();

                                Log.e("response", data.getString("token"));

                                startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                finish();

                            } else {

                                Toast.makeText(SignInActivity.this, loginObject.getString("message"), Toast.LENGTH_LONG).show();
                                Log.e("responseElse", loginObject.getString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgress();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgress();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                params.put("lang", "0");
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    private void showProgress() {
        progress = new ProgressDialog(this);
        progress.setMessage("جار تسجيل الدخول");
        progress.setCancelable(false);
        progress.show();
    }

    private void hideProgress() {
        if (progress.isShowing())
            progress.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:

                if (txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, "الرجاء تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
                } else {

                    if (Utils.isInternetAvailable() && Utils.isNetworkConnected(this)) {
                        Toast.makeText(this, getResources().getString(R.string.checkConnection), Toast.LENGTH_SHORT).show();
                    } else
                        loginRequest();
                }
                break;
        }
    }
}
