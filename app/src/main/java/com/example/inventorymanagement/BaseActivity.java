package com.example.inventorymanagement;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends Activity {

    protected  DBManager dbManager = new DBManager(BaseActivity.this);

}
