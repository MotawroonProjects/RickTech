package com.app.ricktech.uis.general_module.activity_checkout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.ricktech.R;
import com.app.ricktech.databinding.ActivityCheckoutBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.ManageCartModel;
import com.app.ricktech.models.ProductModel;
import com.app.ricktech.models.SelectedLocation;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.UserModel;
import com.app.ricktech.preferences.Preferences;
import com.app.ricktech.remote.Api;
import com.app.ricktech.share.Common;
import com.app.ricktech.tags.Tags;
import com.app.ricktech.uis.general_module.activity_map.MapActivity;
import com.app.ricktech.uis.saved_build_module.activity_saved_buldings.SavedBuildingsActivity;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private ActivityCheckoutBinding binding;
    private String lang;
    private ActivityResultLauncher<Intent> launcher;
    private SelectedLocation selectedLocation;
    private ManageCartModel manageCartModel;
    private Preferences preferences;
    private UserModel userModel;



    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_checkout);
        initView();
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        manageCartModel = ManageCartModel.getInstance();
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
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

        binding.setTotal(manageCartModel.getTotal(this) + "");



    }

    private void sendOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String locationDetails = binding.edtLocationDetails.getText().toString();
        CartModel cartModel = manageCartModel.getCartModel(this);
        cartModel.setAddress(selectedLocation.getAddress());
        cartModel.setLatitude(selectedLocation.getLat());
        cartModel.setLongitude(selectedLocation.getLng());
        cartModel.setLocation_details(locationDetails);

        List<CartModel.BuildProduct> pc_buidings = cartModel.getPc_buidings();
        Log.e("build_size", pc_buidings.size()+"__");
        for (CartModel.BuildProduct buildProduct:pc_buidings){
            Log.e("name", buildProduct.getTitle()+"__"+buildProduct.getPrice()+"__"+buildProduct.getAmount());

            List<CartModel.Component> components = buildProduct.getComponents();
            for (CartModel.Component component :components){
                Log.e("title", component.getCategory_id()+"__"+component.getCategory_name());
                List<Integer> product_ids = component.getProduct_ids();
                for (Integer integer :product_ids){
                    Log.e("id", integer+"__");
                }

                for (CartModel.SingleProduct model :component.getComponentItemList()){
                    Log.e("id", model.getId()+"__"+model.getName());
                }

            }
        }

        Api.getService(Tags.base_url)
                .makeOrder(lang, "Bearer " + userModel.getData().getToken(), cartModel)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(CheckoutActivity.this, R.string.suc, Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                dialog.dismiss();
                            }

                        } else {
                            dialog.dismiss();

                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });


    }


}