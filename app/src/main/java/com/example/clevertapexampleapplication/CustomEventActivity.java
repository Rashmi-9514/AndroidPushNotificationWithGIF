package com.example.clevertapexampleapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.example.clevertapexampleapplication.application.MyApplication;

import java.util.HashMap;

public class CustomEventActivity extends AppCompatActivity {

    Spinner spnEvent;
    EditText etEventName;
    Button btnAddProperties, btnPushEvent;
    LinearLayout llProperties;
    public int count = 0;

    public HashMap<Integer, EditText> keys = new HashMap<>();
    public HashMap<Integer, EditText> values = new HashMap<>();
    public HashMap<Integer, Integer> datatype = new HashMap<>();

    HashMap<String, Object> eventProperties = new HashMap<>();

    public CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_event);

        spnEvent = findViewById(R.id.spnEvent);
        etEventName = findViewById(R.id.etEventName);
        btnAddProperties = findViewById(R.id.btnAddProperties);
        btnPushEvent = findViewById(R.id.btnPushEvent);
        llProperties = findViewById(R.id.llProperties);

        cleverTapAPI = ((MyApplication) getApplication()).getCleverTapInstance();

        spnEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("Test", "onItemSelected: "+spnEvent.getSelectedItem().toString());
                switch (i) {
                    case 0:
                        etEventName.setVisibility(View.VISIBLE);
                        btnAddProperties.setVisibility(View.VISIBLE);
                        break;

                    case 1:
                        etEventName.setVisibility(View.GONE);
                        btnAddProperties.setVisibility(View.GONE);
                        llProperties.setVisibility(View.GONE);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout ll = findViewById(R.id.llProperties);

                int currentCount = count;

                ll.setVisibility(View.VISIBLE);

                LinearLayout linearLayout = new LinearLayout(CustomEventActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setWeightSum(6);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2);


                EditText etKey = new EditText(CustomEventActivity.this);
                etKey.setId(count);
                etKey.setHint("Key");
                etKey.setLayoutParams(p);
                keys.put(count, etKey);

                EditText etValue = new EditText(CustomEventActivity.this);
                etValue.setId(count);
                etValue.setHint("Value");
                etValue.setLayoutParams(p);
                values.put(count, etValue);

                Spinner spinner = new Spinner(CustomEventActivity.this, Spinner.MODE_DROPDOWN);
                String[] spinnerArray = getResources().getStringArray(R.array.datatype);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                        (CustomEventActivity.this, android.R.layout.simple_spinner_item,
                                spinnerArray); //selected item will look like a spinner set from XML
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setLayoutParams(p);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        datatype.put(currentCount, i);
                        if (i==0){
                            etValue.setInputType(InputType.TYPE_CLASS_TEXT);
                        }else {
                            etValue.setInputType(InputType.TYPE_CLASS_DATETIME);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                datatype.put(count, spinner.getSelectedItemPosition());

                linearLayout.addView(etKey);
                linearLayout.addView(spinner);
                linearLayout.addView(etValue);
                ll.addView(linearLayout);
                count++;
            }
        });

        btnPushEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String evn = etEventName.getText().toString();
                for (int i = 0; i < count; i++) {

                    //eventProperties.put(ed[i].getText().toString(),ed1[i].getText().toString());
                    if (datatype.get(i) != null && datatype.get(i) == 0) {
                        eventProperties.put(keys.get(i).getText().toString(),values.get(i).getText().toString());
                    } else if (datatype.get(i) != null && datatype.get(i) > 0){
                        eventProperties.put(keys.get(i).getText().toString(),Integer.parseInt(values.get(i).getText().toString()));
                    }
                }
                cleverTapAPI.pushEvent(evn, eventProperties);
                eventProperties.clear();
            }
        });
    }
}