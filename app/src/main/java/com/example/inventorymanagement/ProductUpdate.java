package com.example.inventorymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProductUpdate extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_update);

        final EditText product_name, model_no, price_buy, price_sell, place, description,quantity;
        final Button update_button;
        final TextView card_no;

        product_name = findViewById(R.id.product_name);
        card_no = findViewById(R.id.card_no);
        model_no = findViewById(R.id.model_no);
        price_buy = findViewById(R.id.price_buy);
        price_sell = findViewById(R.id.price_sell);
        place = findViewById(R.id.place);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);

        update_button = findViewById(R.id.update_button);

        Bundle extras = getIntent().getExtras();
        String p_name,c_no="",m_no,plc,desc;
        double p_buy,p_sell;
        int qnt;

        if(extras != null){
            p_name = extras.getString("p_name");
            c_no = extras.getString("card_no");
            m_no = extras.getString("model_no");
            p_buy = extras.getDouble("price_buy");
            p_sell = extras.getDouble("price_sell");
            plc = extras.getString("place");
            desc = extras.getString("description");
            qnt = extras.getInt("quantity");

            product_name.setText(p_name);
            card_no.setText(c_no);
            model_no.setText(m_no);
            price_buy.setText(String.valueOf(p_buy));
            price_sell.setText(String.valueOf(p_sell));
            place.setText(plc);
            description.setText(desc);
            quantity.setText(String.valueOf(qnt));
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p_n = product_name.getText().toString();
                String c_n = card_no.getText().toString();
                String m_n = model_no.getText().toString();
                String p_b = price_buy.getText().toString();
                String p_s = price_sell.getText().toString();
                String pl = place.getText().toString();
                String dsc = description.getText().toString();
                String qn = quantity.getText().toString();

                // force enter product name, card no, model no, buy&sell price, place
                int error = checkEditTextInputs(product_name,model_no,price_buy,price_sell,place,quantity);
                if(error > 0){
                    Toast toast = Toast.makeText(ProductUpdate.this, "Ürün Güncellenemedi!", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);

                    toast.show();
                }
                else{
                    double p_by = Double.parseDouble(p_b);
                    double p_sl = Double.parseDouble(p_s);
                    int q = Integer.parseInt(qn);
                    int check = dbManager.updateProduct(m_n,c_n,p_by,p_sl,pl,dsc,p_n,q); //update a single entry in product table
                    if(check == 1){     //update success
                        Toast toast = Toast.makeText(ProductUpdate.this, "Ürün güncellendi!!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#008000"),PorterDuff.Mode.SRC_IN);
                        toast.show();

                        Intent intent = new Intent(ProductUpdate.this, ProductView.class);
                        intent.putExtra("clicked_item",c_n);
                        startActivity(intent);
                        finish();
                    }else if(check == 0){   //update failed
                        Toast toast = Toast.makeText(ProductUpdate.this, "Ürün güncellenemedi!!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"),PorterDuff.Mode.SRC_IN);

                        toast.show();
                    }
                }
            }
        });

    }

    // change background colours if edittext's are empty
    private int checkEditTextInputs(EditText product_name, EditText model_no, EditText price_buy, EditText price_sell, EditText place, EditText quantity) {
        int error = 0;
        if (product_name.getText().toString().equals("")){
            product_name.setBackgroundResource(R.drawable.red_background);
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
            Intent intent = new Intent(ProductUpdate.this,ProductView.class);
            TextView card_no = findViewById(R.id.card_no);
            intent.putExtra("clicked_item",card_no.getText().toString());
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
