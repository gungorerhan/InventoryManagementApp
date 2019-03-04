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

public class AddRemoveUnit extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_unit);

        final TextView product_name_txt, card_no_txt, model_no_txt, price_buy_txt, price_sell_txt, place_txt, description_txt, quantity_txt;
        final EditText quantity_add_remove, customer_in;
        Button add_unit_button, remove_unit_button;

        product_name_txt = findViewById(R.id.product_name);
        card_no_txt = findViewById(R.id.card_no);
        model_no_txt = findViewById(R.id.model_no);
        price_buy_txt = findViewById(R.id.price_buy);
        price_sell_txt = findViewById(R.id.price_sell);
        place_txt = findViewById(R.id.place);
        description_txt = findViewById(R.id.description);
        quantity_txt = findViewById(R.id.quantity);

        quantity_add_remove = findViewById(R.id.unit);
        customer_in = findViewById(R.id.customer);

        add_unit_button = findViewById(R.id.add_unit_button);
        remove_unit_button = findViewById(R.id.remove_unit_button);

        Bundle extras = getIntent().getExtras();
        String p_name,c_no,m_no,plc,desc;
        double p_buy,p_sell;
        int quantity;

        if(extras != null){
            p_name = extras.getString("p_name");
            c_no = extras.getString("card_no");
            m_no = extras.getString("model_no");
            p_buy = extras.getDouble("price_buy");
            p_sell = extras.getDouble("price_sell");
            plc = extras.getString("place");
            desc = extras.getString("description");
            quantity = extras.getInt("quantity");

            product_name_txt.setText(p_name);
            card_no_txt.setText(c_no);
            model_no_txt.setText(m_no);
            price_buy_txt.setText(String.valueOf(p_buy));
            price_sell_txt.setText(String.valueOf(p_sell));
            place_txt.setText(plc);
            description_txt.setText(desc);
            quantity_txt.setText(String.valueOf(quantity));
        }


        add_unit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // force enter product name and quantity
                int error = checkEditTextInputs(customer_in,quantity_add_remove);
                if(error > 0){
                    Toast toast = Toast.makeText(AddRemoveUnit.this, "Gerekli alanları doldurmadınız!", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"),PorterDuff.Mode.SRC_IN);

                    toast.show();
                }
                else{
                    int q = Integer.parseInt(quantity_add_remove.getText().toString());
                    String c_no = card_no_txt.getText().toString();
                    String customer = customer_in.getText().toString();
                    String sob = "b";
                    int check = dbManager.sellorbuyProduct(c_no,customer,sob,q);
                    if(check == 1){     //update success
                        Toast toast = Toast.makeText(AddRemoveUnit.this, "Ürün eklendi!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#008000"),PorterDuff.Mode.SRC_IN);
                        toast.show();

                        Intent intent = new Intent(AddRemoveUnit.this,ProductView.class);
                        intent.putExtra("clicked_item",c_no);
                        startActivity(intent);
                        finish();
                    }else if(check == 0){   //update failed
                        Toast toast = Toast.makeText(AddRemoveUnit.this, "Gerekli alanları doldurmadınız!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);

                        toast.show();
                    }
                }

            }
        });

        remove_unit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // force enter product name and quantity
                int error = checkEditTextInputs(customer_in,quantity_add_remove);
                if(error > 0){
                    Toast toast = Toast.makeText(AddRemoveUnit.this, "Gerekli alanları doldurmadınız!", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"),PorterDuff.Mode.SRC_IN);

                    toast.show();
                }
                else{
                    int q = Integer.parseInt(quantity_add_remove.getText().toString());
                    String c_no = card_no_txt.getText().toString();
                    String customer = customer_in.getText().toString();
                    String sob = "s";
                    int check = dbManager.sellorbuyProduct(c_no,customer,sob,q);
                    if(check == 1){     //update success
                        Toast toast = Toast.makeText(AddRemoveUnit.this, "Ürün çıkarıldı!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#008000"),PorterDuff.Mode.SRC_IN);
                        toast.show();
                        Intent intent = new Intent(AddRemoveUnit.this,ProductView.class);
                        intent.putExtra("clicked_item",c_no);
                        startActivity(intent);
                        finish();
                    }else if(check == 0){   //update failed
                        Toast toast = Toast.makeText(AddRemoveUnit.this, "Gerekli alanları doldurmadınız!", Toast.LENGTH_SHORT);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);

                        toast.show();
                    }
                }
            }
        });
    }

    private int checkEditTextInputs(EditText customer, EditText quantity) {
        int error = 0;
        if (customer.getText().toString().equals("")){
            customer.setBackgroundResource(R.drawable.red_background);
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
            Intent intent = new Intent(AddRemoveUnit.this,ProductView.class);
            TextView card_no = findViewById(R.id.card_no);
            intent.putExtra("clicked_item",card_no.getText().toString());
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
