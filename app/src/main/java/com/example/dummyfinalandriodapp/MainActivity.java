package com.example.dummyfinalandriodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.SafeIterableMap;

import android.os.Bundle;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {


    //DO NOT MODIFY THE INSTANCE VARIABLE NAMES INCLUDED BELOW
    //YOU MAY ADD YOUR OWN


    EditText editTextFaceValue;
    EditText editTextSellingPrice;
    EditText editTextAnnualInterest;
    EditText editTextDuration;
    Button buttonCalculateYield;
    TextView textViewResult;
    final String sharedPrefFile = "sharedPref";
    SharedPreferences sharedPreferences;
    final String KEY_FACEVALUE = "faceValue";
    final String KEY_SELLINGPRICE = "sellingPrice";
    final String KEY_ANNUALINTEREST = "annualInterest";
    final String KEY_DURATION = "duration";
    final String EMPTY_STRING = "";
    String faceValueString;
    String sellingPriceString;
    String annualInterestString;
    String durationString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editTextFaceValue = findViewById(R.id.editTextFaceValue);
        editTextSellingPrice = findViewById(R.id.editTextSellingPrice);
        editTextAnnualInterest = findViewById(R.id.editTextAnnualInterest);
        editTextDuration = findViewById(R.id.editTextDuration);

        buttonCalculateYield = findViewById(R.id.buttonCalculateYield);

        textViewResult = findViewById(R.id.textViewResult);

        faceValueString = sharedPreferences.getString(KEY_FACEVALUE, EMPTY_STRING);
        sellingPriceString = sharedPreferences.getString(KEY_SELLINGPRICE, EMPTY_STRING );
        annualInterestString = sharedPreferences.getString(KEY_ANNUALINTEREST, EMPTY_STRING);
        durationString = sharedPreferences.getString(KEY_DURATION, EMPTY_STRING);

        editTextFaceValue.setText(faceValueString);
        editTextSellingPrice.setText(sellingPriceString);
        editTextAnnualInterest.setText(annualInterestString);
        editTextDuration.setText(durationString);


        buttonCalculateYield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                faceValueString = editTextFaceValue.getText().toString();
                sellingPriceString= editTextSellingPrice.getText().toString();
                annualInterestString = editTextAnnualInterest.getText().toString();
                durationString = editTextDuration.getText().toString();

                if (faceValueString.isEmpty() || sellingPriceString.isEmpty()||annualInterestString.isEmpty()||durationString.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter a value" + faceValueString + sellingPriceString + annualInterestString + durationString + "**", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        double faceValue = Double.parseDouble(faceValueString);
                        double sellingPrice = Double.parseDouble(sellingPriceString);
                        double annualInterest = Double.parseDouble(annualInterestString);
                        double duration = Double.parseDouble(durationString);

                        Bond bond = new Bond.BondBuilder().setFaceValue(faceValue).setSellingPrice(sellingPrice).setInterestPayment(annualInterest).setDuration(duration).createBond();

                        if (Math.abs(annualInterest) < 1e-10){
                            bond.setYieldCalculation( new ZeroCouponYield());
                        } else{
                            bond.setYieldCalculation( new WithCouponYield());
                        }

                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Handler handler = new Handler(Looper.getMainLooper());
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                double result = bond.calculateYTM();
                                String resultString = String.valueOf(result);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewResult.setText(resultString);
                                    }
                                });
                            }
                        });
                    } catch (IllegalArgumentException ex) {
                        Toast.makeText(MainActivity.this, "Illegal Values", Toast.LENGTH_LONG).show();
                    }


                }

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FACEVALUE, faceValueString);
        editor.putString(KEY_SELLINGPRICE, sellingPriceString);
        editor.putString(KEY_ANNUALINTEREST, annualInterestString);
        editor.putString(KEY_DURATION, durationString);

    }
}
