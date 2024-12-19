package com.example.jurnal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.jurnal.data.Jurnal;
import com.example.jurnal.database.JurnalService;
import com.example.jurnal.network.AsyncTaskRunner;
import com.example.jurnal.network.Callback;
import com.example.jurnal.network.HttpManager;
import com.example.jurnal.network.JSONParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFERENCES = "shared_preferences";
    public static final String SPINNER_KEY = "spinner_key";
    public static final String ET_KEY = "et_key";
    private final String url = "https://api.npoint.io/efcea1d46c36fa555d04";
    private FloatingActionButton fab;
    private TextView tv;
    private Spinner spinner;
    private EditText et;
    private Button bttn;
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private List<Jurnal> jurnale = new ArrayList<>();
    private JurnalService jurnalService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();

        fab.setOnClickListener(click -> {
            asyncTaskRunner.executeAsync(new HttpManager(url), onMainThreadOperation());
        });
        
        bttn.setOnClickListener(click -> {
            if (et.getText() == null || et.getText().toString().isBlank()) {
                Toast.makeText(getApplicationContext(), R.string.empty_expense_value, Toast.LENGTH_SHORT).show();
                return;
            }

            String comp = spinner.getSelectedItem().toString();
            int value = Integer.parseInt(et.getText().toString());

            List<Jurnal> deleted;
            if (comp.equals("are valoarea mai mare decat")) {
                deleted = jurnale.stream().filter(j -> j.getExpense() > value).collect(Collectors.toList());
            } else if (comp.equals("are valoarea mai mica decat")) {
                deleted = jurnale.stream().filter(j -> j.getExpense() < value).collect(Collectors.toList());
            } else {
                deleted = jurnale.stream().filter(j -> j.getExpense() == value).collect(Collectors.toList());
            }

            if (!deleted.isEmpty()) {
                jurnalService.delete(deleted, callbackDelete(value, comp));
            }
        });
    }

    private Callback<List<Jurnal>> callbackDelete(int value, String comp) {
        return result -> {
            if (result != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SPINNER_KEY, comp);
                editor.putInt(ET_KEY, value);
                editor.apply();
                jurnale.removeAll(result);
                Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private Callback<String> onMainThreadOperation() {
        return result -> {
            List<Jurnal> parsed = JSONParser.getFromJson(result);
            List<Jurnal> insert = parsed.stream().filter(j -> !jurnale.contains(j)).collect(Collectors.toList());
            
            if (!insert.isEmpty()) {
                jurnalService.insertAll(insert, callbackInsertAll());
            }
        };
    }

    private Callback<List<Jurnal>> callbackInsertAll() {
        return result -> {
            if (result != null) {
                Toast.makeText(getApplicationContext(), R.string.inserted, Toast.LENGTH_SHORT).show();
                jurnale.addAll(result);
            }
        };
    }

    private void initComponents() {
        fab = findViewById(R.id.surugiu_george_alexandru_fab);
        tv = findViewById(R.id.surugiu_george_alexandru_tv);
        spinner = findViewById(R.id.surugiu_george_alexandru_spinner);
        et = findViewById(R.id.surugiu_george_alexandru_et);
        bttn = findViewById(R.id.surugiu_george_alexandru_bttn);
        
        jurnalService = new JurnalService(getApplicationContext());
        sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        
        String comp = sharedPreferences.getString(SPINNER_KEY, "");
        int value = sharedPreferences.getInt(ET_KEY, 0);
        
        if (comp.equals("are valoarea mai mare decat")) {
            spinner.setSelection(0);
        } else if (comp.equals("are valaorea mai mica decat")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }
        
        if (value != 0) {
            et.setText(String.valueOf(value));
        }
        
        jurnalService.getAll(callbackGetAll());
    }

    private Callback<List<Jurnal>> callbackGetAll() {
        return result -> {
            if (result != null) {
                jurnale.clear();
                jurnale.addAll(result);
                Toast.makeText(getApplicationContext(), R.string.loaded, Toast.LENGTH_SHORT).show();
            }
        };
    }
}