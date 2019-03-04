package com.example.inventorymanagement;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.inventorymanagement.DBManager.card_col;
import static com.example.inventorymanagement.DBManager.description_col;
import static com.example.inventorymanagement.DBManager.model_col;
import static com.example.inventorymanagement.DBManager.product_name_col;
import static com.example.inventorymanagement.DBManager.product_table;

public class ListProducts extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        FloatingActionButton add_new_product_button;
        add_new_product_button = findViewById(R.id.add_new_product_button);
        add_new_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProducts.this, NewProduct.class);
                startActivity(intent);
                finish();
            }
        });


        String selectQuery = "SELECT * FROM " + product_table;
        fillTable(selectQuery);

        SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selectQuery = "SELECT * FROM " +product_table+ " WHERE(" +product_name_col+ " LIKE '%" +newText+
                "%' OR " +card_col+ " LIKE '%" +newText+ "%' OR " +model_col+ " LIKE '%" +newText+ "%' OR " +description_col+
                " LIKE '%" +newText+ "%')";
                fillTable(selectQuery);
                return false;
            }
        });
    }

    private void fillTable(String selectQuery){
        SQLiteDatabase db = dbManager.getReadableDatabase();
        final Cursor c = db.rawQuery(selectQuery, null);

        LinearLayout tl = findViewById(R.id.all_products_table_layout);
        tl.removeAllViews();
        View tablerow = null;
        TextView product_name, card_no, place, quantity;

        int i = 0;
        if (c.moveToFirst()) {
            do {
                i++;
                tablerow = (TableRow) View.inflate(this, R.layout.product_list_row, null);
                product_name = tablerow.findViewById(R.id.product_name);
                card_no = tablerow.findViewById(R.id.card_no);
                place = tablerow.findViewById(R.id.place);
                quantity = tablerow.findViewById(R.id.quantity);

                String p_name = c.getString(6);
                final String c_no = c.getString(1);
                String plc = c.getString(5);
                int qnt = c.getInt(7);

                product_name.setText(p_name);
                card_no.setText(c_no);
                place.setText(plc);
                quantity.setText(String.valueOf(qnt));

                tablerow.setTag(i);
                tablerow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ListProducts.this,ProductView.class);
                        intent.putExtra("clicked_item",c_no);
                        startActivity(intent);
                        finish();
                    }
                });

                //add TableRows to Layout
                tl.addView(tablerow);

            } while (c.moveToNext());
        }
        c.close();
        db.close();
    }



}
