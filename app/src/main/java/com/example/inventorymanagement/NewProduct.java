package com.example.inventorymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewProduct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        final EditText product_name, card_no, model_no, price_buy, price_sell, place, description, quantity;
        Button new_product_button;

        product_name = findViewById(R.id.product_name);
        card_no = findViewById(R.id.card_no);
        model_no = findViewById(R.id.model_no);
        price_buy = findViewById(R.id.price_buy);
        price_sell = findViewById(R.id.price_sell);
        place = findViewById(R.id.place);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);

        new_product_button = findViewById(R.id.new_product_button);
        new_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_name, c_no, m_no, p_buy, p_sell, plc, desc, qnt;
                p_name = product_name.getText().toString();
                c_no = card_no.getText().toString();
                m_no = model_no.getText().toString();
                p_buy = price_buy.getText().toString();
                p_sell = price_sell.getText().toString();
                plc = place.getText().toString();
                desc = description.getText().toString();
                qnt = quantity.getText().toString();

                // force enter product name, card no, model no, buy&sell price, place
                int error = checkEditTextInputs(product_name,card_no,model_no,price_buy,price_sell,place,quantity);
                if(error > 0){
                    Toast toast = Toast.makeText(NewProduct.this, "Ürün eklenemedi!", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"),PorterDuff.Mode.SRC_IN);

                    toast.show();
                }
                else {
                    double p_b = Double.parseDouble(p_buy);
                    double p_s = Double.parseDouble(p_sell);
                    int q = Integer.parseInt(qnt);
                    int check = dbManager.addNewProduct(m_no,c_no,p_b,p_s,plc,desc,p_name,q); //add to product table
                    if(check == 1){     //add success
                        // set text of all edittext fields
                        product_name.setText("");
                        card_no.setText("");
                        model_no.setText("");
                        price_buy.setText("");
                        price_sell.setText("");
                        place.setText("");
                        description.setText("");
                        quantity.setText("");
                        Toast toast = Toast.makeText(NewProduct.this, "Ürün eklendi!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#008000"),PorterDuff.Mode.SRC_IN);
                        toast.show();

                    }else if(check == 0){   //add failed
                        Toast toast = Toast.makeText(NewProduct.this, "Ürün eklenemedi!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"),PorterDuff.Mode.SRC_IN);

                        toast.show();
                    }
                }
            }
        });

    }

    // change background colours if edittext's are empty
    private int checkEditTextInputs(EditText product_name, EditText card_no, EditText model_no, EditText price_buy, EditText price_sell, EditText place, EditText quantity) {
        int error = 0;
        if (product_name.getText().toString().equals("")){
            product_name.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (card_no.getText().toString().equals("")){
            card_no.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (model_no.getText().toString().equals("")){
            model_no.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (price_buy.getText().toString().equals("")){
            price_buy.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (price_sell.getText().toString().equals("")){
            price_sell.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (place.getText().toString().equals("")){
            place.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        if (quantity.getText().toString().equals("")){
            quantity.setBackgroundResource(R.drawable.red_background);
            error+=1;
        }
        return error;
    }

    // show listproducts when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(NewProduct.this,ListProducts.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
