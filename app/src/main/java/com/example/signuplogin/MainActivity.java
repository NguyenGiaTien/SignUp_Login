
package com.example.signuplogin;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;



import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class MainActivity extends AppCompatActivity {

    String path = "/";

    TextView temp,hum,errorDHT,rain,light,textLed;
    DatabaseReference refTemp,refHum,refErrorDHT, refRain,refLed,refLight;
    FirebaseDatabase database;
    ProgressBar tempPro, humPro,rainPro;
    ImageView weatherImage,imageLed1;
    CardView tempCard, humCard, rainCard,lightCard;
    Button btnLed;


    int sLed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        temp = findViewById(R.id.Temperature);
        btnLed =findViewById(R.id.btnLed1);
        hum =  findViewById(R.id.Humanity);
        errorDHT =  findViewById(R.id.textDHTError);
        rain =  findViewById(R.id.Rainfall);
        light =  findViewById(R.id.light);
        textLed = findViewById(R.id.textLed);
        tempPro =  findViewById(R.id.proTem);
        humPro =  findViewById(R.id.proHum);
        rainPro = findViewById(R.id.proRain);
        tempCard = findViewById(R.id.cardTemp);
        humCard = findViewById(R.id.cardHum);
        rainCard = findViewById(R.id.cardRain);
        lightCard = findViewById(R.id.cardLight);
        imageLed1 =  findViewById(R.id.imageLed1);
        weatherImage =  findViewById(R.id.WeatherIcon);
        database = FirebaseDatabase.getInstance();
        refTemp = database.getReference(path + "Temp");
        refHum = database.getReference(path + "Hum");
        refErrorDHT = database.getReference(path + "Error");
        refRain = database.getReference(path + "Rain");
        refLight = database.getReference(path+"Light");
        refLed = database.getReference(path+"led");

        ReadDHT11();
        ReadRain();
        ReadLight();
        ReadLed();

    }
    private void ReadDHT11(){
                    errorDHT.setVisibility(View.INVISIBLE);
                    refTemp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Float getTemp = snapshot.getValue(Float.class);
                            assert getTemp != null;
                            String Temperature = getTemp + "\u2103";
                            temp.setText(Temperature);
                            tempPro.setProgress(Math.round(getTemp));
                            if(getTemp < 20){
                                tempCard.setCardBackgroundColor(Color.parseColor("blue"));
                            }else if(getTemp >= 20 && getTemp <= 35){
                                tempCard.setCardBackgroundColor(Color.parseColor("green"));
                            }else if(getTemp > 35){
                                tempCard.setCardBackgroundColor(Color.parseColor("red"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    refHum.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Float getHum = snapshot.getValue(Float.class);
                            assert getHum != null;
                            String Humanity = getHum + "%";
                            hum.setText(Humanity);
                            humPro.setProgress(Math.round(getHum));
                            if(getHum < 40){
                                humCard.setCardBackgroundColor(Color.parseColor("blue"));
                            }else if(getHum >= 40 && getHum <= 70){
                                humCard.setCardBackgroundColor(Color.parseColor("green"));
                            }else if(getHum > 70){
                                humCard.setCardBackgroundColor(Color.parseColor("red"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

    }
    private void ReadRain(){
        refRain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer getWaterData = snapshot.getValue(Integer.class);
                assert getWaterData != null;
                String Rain = getWaterData + "ml";
                rain.setText(Rain);
                rainPro.setProgress(Math.round(getWaterData));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void ReadLight(){
        refLight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer Light = snapshot.getValue(Integer.class);
                assert Light != null;
                if (Light == 0) {
                    light.setText(R.string.sun);
                    weatherImage.setImageResource(R.drawable.sun);

                }
                else {
                    light.setText(R.string.night);
                    weatherImage.setImageResource(R.drawable.moon);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ReadLed(){
        refLed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer statusLed1 = snapshot.getValue(Integer.class);
                assert statusLed1 != null;
                if (statusLed1 == 1) {
                    textLed.setText("Bật");
                    btnLed.setText("Tắt");
                    imageLed1.setImageResource(R.drawable.lamp_on);
                    sLed = 0;
                } else {
                    textLed.setText("Tắt");
                    btnLed.setText("Bật");
                    imageLed1.setImageResource(R.drawable.lamp_off);
                    sLed = 1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnLed.setOnClickListener(view -> {
            refLed.setValue(sLed);

        });
    }


}