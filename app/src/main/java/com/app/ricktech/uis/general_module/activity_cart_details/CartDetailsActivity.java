package com.app.ricktech.uis.general_module.activity_cart_details;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ricktech.R;
import com.app.ricktech.adapters.CartBuildAdapter;
import com.app.ricktech.adapters.CartSingleProductsAdapter;
import com.app.ricktech.databinding.ActivityCartDetailsBinding;
import com.app.ricktech.databinding.ActivityCheckoutBinding;
import com.app.ricktech.language.Language;
import com.app.ricktech.models.CartModel;
import com.app.ricktech.models.ManageCartModel;
import com.app.ricktech.models.SelectedLocation;
import com.app.ricktech.uis.general_module.activity_checkout.CheckoutActivity;
import com.app.ricktech.uis.general_module.activity_map.MapActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CartDetailsActivity extends AppCompatActivity {
    private ActivityCartDetailsBinding binding;
    private String lang;
    private ManageCartModel manageCartModel;
    private List<CartModel.SingleProduct> singleProductList;
    private List<CartModel.BuildProduct> buildProductList;
    private CartSingleProductsAdapter singleProductsAdapter;
    private CartBuildAdapter cartBuildAdapter;
    private boolean isItemChanged = false;
    private ActivityResultLauncher<Intent> launcher;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "de")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart_details);
        initView();
    }

    private void initView() {
        manageCartModel = ManageCartModel.getInstance();
        singleProductList = new ArrayList<>();
        buildProductList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", "de");
        binding.setLang(lang);
        binding.llBack.setOnClickListener(v -> back());
        binding.btnCancel.setOnClickListener(v -> back());
        binding.btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            launcher.launch(intent);
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode()==RESULT_OK){
                manageCartModel.clearCart(this);
                isItemChanged = true;
                back();
            }


        });
        getData();

    }



    private void getData() {
        singleProductList.clear();
        buildProductList.clear();
        singleProductList.addAll(manageCartModel.getSingleProductList(this));
        buildProductList.addAll(manageCartModel.getBuildProductList(this));
        if (singleProductList.size() > 0) {
            binding.recViewProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            singleProductsAdapter = new CartSingleProductsAdapter(this, singleProductList);
            binding.recViewProducts.setAdapter(singleProductsAdapter);
            binding.llProducts.setVisibility(View.VISIBLE);
            binding.tvCount.setText(singleProductList.size() + "");
        } else {

            binding.llProducts.setVisibility(View.GONE);

        }

        if (buildProductList.size() > 0) {
            binding.recViewBuild.setLayoutManager(new LinearLayoutManager(this));
            cartBuildAdapter = new CartBuildAdapter(this, buildProductList);
            binding.recViewBuild.setAdapter(cartBuildAdapter);
            binding.recViewBuild.setVisibility(View.VISIBLE);
        } else {

            binding.recViewBuild.setVisibility(View.GONE);
        }
        if (singleProductsAdapter!=null){
            singleProductsAdapter.notifyDataSetChanged();

        }

        if (cartBuildAdapter!=null){
            cartBuildAdapter.notifyDataSetChanged();

        }

        binding.setTotal(manageCartModel.getTotal(this) + "");


    }

    public void updateSingleProduct(CartModel.SingleProduct singleProduct) {
        manageCartModel.addSingleProduct(this, singleProduct);
        binding.setTotal(manageCartModel.getTotal(this) + "");
        isItemChanged = true;

    }


    public void updateBuildProduct(CartModel.SingleProduct singleProduct, int parent_pos, int parent_pos2, int child_pos) {
        manageCartModel.updateItemBuild(this, parent_pos, parent_pos2, child_pos, singleProduct);
        binding.setTotal(manageCartModel.getTotal(this) + "");
        isItemChanged = true;

    }

    public void removeSingleProduct(int adapter_pos, CartModel.SingleProduct singleProduct) {
        if (manageCartModel.getBuildProductList(this).size() > 0) {
            isItemChanged = true;
            manageCartModel.removeSingleProduct(this, singleProduct);
            singleProductList.remove(adapter_pos);
            singleProductsAdapter.notifyItemRemoved(adapter_pos);
            if (singleProductList.size() == 0) {
                binding.llProducts.setVisibility(View.GONE);

            }
        } else {
            if (manageCartModel.getSingleProductList(this).size() > 1) {
                isItemChanged = true;
                manageCartModel.removeSingleProduct(this, singleProduct);
            }
        }

        binding.setTotal(manageCartModel.getTotal(this) + "");

    }


    public void removeBuildProduct(int parent_pos, int parent_pos2, int child_pos) {


        if (manageCartModel.getSingleProductList(this).size() > 0) {

            isItemChanged = true;
            manageCartModel.removeItemBuild(this, parent_pos,parent_pos2,child_pos);

            getData();

            if (buildProductList.size() == 0) {
                binding.recViewBuild.setVisibility(View.GONE);

            }
        } else {
            if (manageCartModel.getBuildProductList(this).size() > 1) {
                isItemChanged = true;
                manageCartModel.removeItemBuild(this, parent_pos,parent_pos2,child_pos);
                getData();

            }else {

                if (manageCartModel.getBuildProductList(this).size()==1){

                    if (manageCartModel.getBuildProductList(this).get(0).getComponents().size()==1){

                        if (manageCartModel.getBuildProductList(this).get(0).getComponents().get(0).getComponentItemList().size()>1){
                            isItemChanged = true;
                            manageCartModel.removeItemBuild(this, parent_pos,parent_pos2,child_pos);
                            getData();
                        }

                    }else if (manageCartModel.getBuildProductList(this).get(0).getComponents().size()>1){
                        isItemChanged = true;
                        manageCartModel.removeItemBuild(this, parent_pos,parent_pos2,child_pos);
                        getData();
                    }
                }
            }
        }



    }

    @Override
    public void onBackPressed() {
        back();
    }
    private void back() {
        if (isItemChanged) {
            setResult(RESULT_OK);
        }
        finish();
    }

}