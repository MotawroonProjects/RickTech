package com.app.ricktech.uis.general_module.activity_checkout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityCheckoutBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.SelectedLocation;
import com.app.ricktech.uis.general_module.activity_map.MapActivity;

import io.paperdb.Paper;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private String lang;
    private ActivityResultLauncher<Intent> launcher;
    private SelectedLocation selectedLocation;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_checkout);
        initView();
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK&&result.getData()!=null){

                selectedLocation = (SelectedLocation) result.getData().getSerializableExtra("location");
                binding.tvLocation.setText(selectedLocation.getAddress());
                binding.tvLocation.setError(null);
            }
        });
        binding.llBack.setOnClickListener(v -> finish());

        binding.imageMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            launcher.launch(intent);
        });

        binding.btnConfirm.setOnClickListener(v -> {
            if (selectedLocation!=null){
                binding.tvLocation.setError(null);
                sendOrder();
            }else {
                binding.tvLocation.setError(getString(R.string.field_req));
            }
        });


    }

    private void sendOrder() {
        setResult(RESULT_OK);
        finish();
    }


}