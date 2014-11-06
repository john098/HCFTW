package com.example.johnpham.hcftw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.content.Context;


public class Login extends Activity {

        private EditText userError;
       private EditText username;
        private EditText password;
        private String[] AccountPass={"foo@example.com:hello"};
        private String a="a@";
        private String b="b";
        private String c="c";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNetworkAvailable()) {

            // userError.setError(null);
            setContentView(R.layout.activity_login);
            final EditText userError;
            userError = (EditText) findViewById(R.id.editText);
            username = (EditText) findViewById(R.id.editText);
            password = (EditText) findViewById(R.id.editText3);

            password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        preCondition();
                    }
                    return false;
                }

            });
            Button log = (Button) findViewById(R.id.SignIn);
            log.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preCondition();
                }
            });
        }
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Error");

            // Setting Dialog Message
            alertDialog.setMessage("No internetion");

            // Setting alert dialog icon
            alertDialog.setIcon(R.drawable.x);



            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    close();
                }
            });


            alertDialog.show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void close()
    {
        super.onDestroy();
        this.finish();
    }
    public void preCondition()
    {

            login();



    }
    public void login()
    {

        if (username.getText().toString().equals((a))) {

            if (password.getText().toString().equals(b)) {

                //do something
                startActivity(new Intent(getApplicationContext(), Home.class));
            }
            else {
                password.setError("invalid password or Username");

                password.requestFocus();
            }

        }else{

            if (password.getText().toString().isEmpty()) {
                password.setError("require field");
                password.setHighlightColor(Color.RED);
                password.requestFocus();
            } if (username.getText().toString().isEmpty()) {
                username.setError("require field");
                username.setHighlightColor(Color.RED);
                username.requestFocus();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);


        return true;
    }
    public boolean isEmail(String email)
    {
        return email.contains("@");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
