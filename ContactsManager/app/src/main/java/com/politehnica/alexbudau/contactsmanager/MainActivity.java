package com.politehnica.alexbudau.contactsmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button showButton;
    private Button saveButton;
    private Button cancelButton;
    private EditText nameEditText;
    private EditText numberEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;
    private LinearLayout container;

    private Intent intentFromParent;
    private Intent intentToParent;

    private boolean isContainerVisible;

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.showButton :
                    Button b = (Button) v;
                    if (!isContainerVisible) {
                        b.setText(R.string.hideFieldsButton);
                        container.setVisibility(View.VISIBLE);
                        isContainerVisible = true;
                    }
                    else {
                        b.setText(R.string.showFieldsButton);
                        container.setVisibility(View.GONE);
                        isContainerVisible = false;
                    }
                    break;
                case R.id.saveButton :
                    Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                    intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                    String name = nameEditText.getText().toString();
                    String phoneNumber = numberEditText.getText().toString();
                    String email = emailEditText.getText().toString();
                    String address = addressEditText.getText().toString();
                    String jobTitle = jobEditText.getText().toString();
                    String company = companyEditText.getText().toString();
                    String website = websiteEditText.getText().toString();
                    String im = imEditText.getText().toString();

                    if (name != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                    }
                    if (phoneNumber != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber);
                    }
                    if (email != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                    }
                    if (address != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
                    }
                    if (jobTitle != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
                    }
                    if (company != null) {
                        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
                    }

                    ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                    if (website != null) {
                        ContentValues websiteRow = new ContentValues();
                        websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                        websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                        contactData.add(websiteRow);
                    }
                    if (im != null) {
                        ContentValues imRow = new ContentValues();
                        imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                        imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                        contactData.add(imRow);
                    }
                    intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
                    startActivityForResult(intent, Constants.CONTACTS_CONTRACT_REQ_CODE);
                    break;
                case R.id.cancelButton :
                    if (intentFromParent != null) {
                        setResult(RESULT_CANCELED, new Intent());
                        finish();
                    }
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        showButton = (Button) findViewById(R.id.showButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        numberEditText = (EditText) findViewById(R.id.numberEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        jobEditText = (EditText) findViewById(R.id.jobEditText);
        companyEditText = (EditText) findViewById(R.id.companyEditText);
        websiteEditText = (EditText) findViewById(R.id.websiteEditText);
        imEditText = (EditText) findViewById(R.id.imEditText);
        container = (LinearLayout) findViewById(R.id.container);

        View.OnClickListener clickListener = new ButtonClickListener();

        showButton.setOnClickListener(clickListener);
        saveButton.setOnClickListener(clickListener);
        cancelButton.setOnClickListener(clickListener);

        intentFromParent = getIntent();

        if (intentFromParent != null) {
            String phoneNumber = intentFromParent.getStringExtra(Constants.PHONE_NUMBER_EXTRA);
            intentToParent = new Intent();
            numberEditText.setText(phoneNumber);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Constants.CONTACTS_CONTRACT_REQ_CODE == requestCode) {
            setResult(resultCode, intentToParent);
            finish();
        }
    }
}
