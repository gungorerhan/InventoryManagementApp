package com.example.inventorymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.inventorymanagement.DBManager.card_col;
import static com.example.inventorymanagement.DBManager.product_log_table;

public class ProductView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        TextView product_name_txt, card_no_txt, model_no_txt, price_buy_txt, price_sell_txt, place_txt, description_txt, quantity_txt;
        Button update_button, delete_button, add_unit_button;

        product_name_txt = findViewById(R.id.product_name);
        card_no_txt = findViewById(R.id.card_no);
        model_no_txt = findViewById(R.id.model_no);
        price_buy_txt = findViewById(R.id.price_buy);
        price_sell_txt = findViewById(R.id.price_sell);
        place_txt = findViewById(R.id.place);
        description_txt = findViewById(R.id.description);
        quantity_txt = findViewById(R.id.quantity);

        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        add_unit_button = findViewById(R.id.add_remove_unit_button);

        Intent intent = getIntent();
        String card_no = intent.getStringExtra("clicked_item");

        // get all details of product
        final Product p = new Product();
        dbManager.getProduct(p,card_no);

        // set texts of textviews
        product_name_txt.setText(p.getProduct_name());
        card_no_txt.setText(p.getCard_no());
        model_no_txt.setText(p.getModel_no());
        price_buy_txt.setText(String.valueOf(p.getPrice_buy()));
        price_sell_txt.setText(String.valueOf(p.getPrice_sell()));
        place_txt.setText(p.getPlace());
        description_txt.setText(p.getDescription());
        quantity_txt.setText(String.valueOf(p.getQuantitiy()));


        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setTitle("Ürün Silme");
        builder.setMessage(p.getProduct_name()+ " 'i silmek istediğinize emin misiniz?");

        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int check = dbManager.deleteProduct(p.getCard_no());
                if(check == 1){
                    Toast toast = Toast.makeText(ProductView.this, "Ürün silindi!", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#008000"), PorterDuff.Mode.SRC_IN);
                    toast.show();
                    Intent intent = new Intent(ProductView.this, ListProducts.class);
                    startActivity(intent);
                    finish();
                }else if(check == 0){
                    Toast toast = Toast.makeText(ProductView.this, "Ürün silinemedi!", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);
                    toast.show();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(ProductView.this, "Ürün silinemedi!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.getBackground().setColorFilter(Color.parseColor("#ff2b2b"), PorterDuff.Mode.SRC_IN);
                toast.show();
                dialog.dismiss();
            }
        });


        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductView.this,ProductUpdate.class);
                intent.putExtra("p_name",p.getProduct_name());
                intent.putExtra("card_no",p.getCard_no());
                intent.putExtra("model_no",p.getModel_no());
                intent.putExtra("price_buy",p.getPrice_buy());
                intent.putExtra("price_sell",p.getPrice_sell());
                intent.putExtra("place",p.getPlace());
                intent.putExtra("description",p.getDescription());
                intent.putExtra("quantity",p.getQuantitiy());
                startActivity(intent);
                finish();
            }
        });

        add_unit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductView.this,AddRemoveUnit.class);
                intent.putExtra("p_name",p.getProduct_name());
                intent.putExtra("card_no",p.getCard_no());
                intent.putExtra("model_no",p.getModel_no());
                intent.putExtra("price_buy",p.getPrice_buy());
                intent.putExtra("price_sell",p.getPrice_sell());
                intent.putExtra("place",p.getPlace());
                intent.putExtra("description",p.getDescription());
                intent.putExtra("quantity",p.getQuantitiy());
                startActivity(intent);
                finish();
            }
        });

        fillLogTable(card_no_txt.getText().toString());


    }

    private void fillLogTable(String cn) {

        SQLiteDatabase db = dbManager.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + product_log_table + " WHERE " +card_col+ " ='" +cn+ "';";
        final Cursor c = db.rawQuery(selectQuery, null);

        LinearLayout tl = findViewById(R.id.log_table_layout);
        View tablerow = null;
        TextView customer_txt, date_txt, qnt_change_txt, customer_label, date_label, qnt_change_label, label;

        int i = 0;
        if (c.moveToFirst()) {
            do {
                i++;
                tablerow = (TableRow) View.inflate(this, R.layout.log_list_row, null);

                customer_txt = tablerow.findViewById(R.id.customer);
                date_txt = tablerow.findViewById(R.id.date);
                qnt_change_txt = tablerow.findViewById(R.id.quantity);
                customer_label = tablerow.findViewById(R.id.customer_label);
                date_label = tablerow.findViewById(R.id.date_label);
                qnt_change_label = tablerow.findViewById(R.id.quantity_label);
                label = tablerow.findViewById(R.id.label);

                String customer = c.getString(1);
                String date = c.getString(2);
                int qnt_change = c.getInt(3);   // qnt_change is null for some reason fix!!!!
                String sob = c.getString(4);

                customer_txt.setText(customer);
                date_txt.setText(date);
                qnt_change_txt.setText(String.valueOf(qnt_change));

                if(sob.equals("s")){
                    customer_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    date_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    qnt_change_label.setBackgroundColor(Color.parseColor("#ff2b2b"));
                    label.setText("Stok Çıkışı");
                }
                else{
                    customer_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    date_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    qnt_change_label.setBackgroundColor(Color.parseColor("#32CD32"));
                    label.setText("Stok Girişi");
                }

                tablerow.setTag(i);
                //add TableRows to Layout
                tl.addView(tablerow);

                c.moveToNext();
            } while (c.moveToNext());
        }
        c.close();
        db.close();

    }


    // show product list when back button pressed
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ProductView.this,ListProducts.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
