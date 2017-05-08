package com.example.com.cs478proj5funclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs478proj5funcommon.PictureAudioInterface;

public class CS478Proj5ServiceUser extends AppCompatActivity {
    protected static final String TAG = "MainActivity";
    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREF";

    private EditText pictureNumText, audioNumText;
    private Button displayPicButton, playButton, pauseButton, resumeButton, stopButton;
    private ImageView picture;
    private ListView lv;
    boolean isBound = false;
    List<String> requests = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;

    private PictureAudioInterface pictureAudioService;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder iservice) {
            pictureAudioService = PictureAudioInterface.Stub.asInterface(iservice);
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName className) {
            pictureAudioService = null;
            isBound = false;
        }
    };//end serviceConnection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs478_proj5_service_user);
        getWidgetReferences();

        //retrieve the list of requests from saved preferences
        requests = getArray();

        //fill the list
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requests );
        lv.setAdapter(arrayAdapter);
    }//end onCreate(...)

    //unbind to the service when closing the program
    @Override
    public void onDestroy(){
        unbindService(this.serviceConnection);
        super.onDestroy();
    }//end onDestroy()

    //save the array list when ending the program
    @Override
    public void onStop() {
        saveArray();
        super.onStop();
    }//end onStop()

    //override the back button to do the same as home button
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }//end onBackPressed()

    //attaches the private members to widgets in the main activity XML
    public void getWidgetReferences(){
        pictureNumText = (EditText) findViewById(R.id.picNumberText);
        audioNumText = (EditText) findViewById(R.id.audioNumberText);
        displayPicButton = (Button) findViewById(R.id.displayPictureButton);
        playButton = (Button) findViewById(R.id.playButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        resumeButton = (Button) findViewById(R.id.resumeButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        picture = (ImageView) findViewById(R.id.picture);
        lv = (ListView) findViewById(R.id.requestsListView);
    }//end getWidgetReferences()

    public void displayPicture(View view) {
        int num = Integer.parseInt(pictureNumText.getText().toString());

        if(num > 0 && num < 4) {
            Toast.makeText(this, "Displaying Picture: " + num, Toast.LENGTH_SHORT).show();

            try{
                if(isBound) {
                    picture.setImageBitmap(pictureAudioService.getPicture(num - 1));
                    requests.add("Displaying Picture: " + num);
                    arrayAdapter.notifyDataSetChanged();
                }
                else {
                    Log.i(TAG, "Service not bound");
                    Toast.makeText(this, "Not Bound", Toast.LENGTH_SHORT).show();
                }
            }
            catch(RemoteException e){
                Log.e(TAG, e.toString());
            }
        }
        else
            Toast.makeText(this, "ERROR: Enter 1, 2, or 3", Toast.LENGTH_SHORT).show();
    }//end displayPicture(...)

    public void playAudio(View view) {
        int num = Integer.parseInt(audioNumText.getText().toString());

        if(num > 0 && num < 4) {
            Toast.makeText(this, "Playing Audio: " + num, Toast.LENGTH_SHORT).show();

            try{
                pictureAudioService.playAudio(num);
                requests.add("Playing Audio: " + num);
                arrayAdapter.notifyDataSetChanged();
            }
            catch(RemoteException e){
                Log.e(TAG, e.toString());
            }
        }
        else
            Toast.makeText(this, "ERROR: Enter 1, 2, or 3", Toast.LENGTH_SHORT).show();

    }//end playAudio(...)

    public void pauseAudio(View view) {
        int num = Integer.parseInt(audioNumText.getText().toString());

        if(num > 0 && num < 4) {
            Toast.makeText(this, "Pausing Audio: " + num, Toast.LENGTH_SHORT).show();

            try{
                pictureAudioService.pauseAudio(num);
                requests.add("Pausing Audio: " + num);
                arrayAdapter.notifyDataSetChanged();
            }
            catch(RemoteException e){
                Log.e(TAG, e.toString());
            }
        }
        else
            Toast.makeText(this, "ERROR: Enter 1, 2, or 3", Toast.LENGTH_SHORT).show();

    }//end pauseAudio(...)

    public void resumeAudio(View view) {
        int num = Integer.parseInt(audioNumText.getText().toString());

        if(num > 0 && num < 4) {
            Toast.makeText(this, "Resuming Audio: " + num, Toast.LENGTH_SHORT).show();

            try{
                pictureAudioService.resumeAudio(num);
                requests.add("Resuming Audio: " + num);
                arrayAdapter.notifyDataSetChanged();
            }
            catch(RemoteException e){
                Log.e(TAG, e.toString());
            }
        }
        else
            Toast.makeText(this, "ERROR: Enter 1, 2, or 3", Toast.LENGTH_SHORT).show();

    }//end resumeAudio(...)

    public void stopAudio(View view) {
        int num = Integer.parseInt(audioNumText.getText().toString());

        if(num > 0 && num < 4) {
            Toast.makeText(this, "Stopping Audio: " + num, Toast.LENGTH_SHORT).show();

            try{
                pictureAudioService.stopAudio(num);
                requests.add("Stopping Audio: " + num);
                arrayAdapter.notifyDataSetChanged();
            }
            catch(RemoteException e){
                Log.e(TAG, e.toString());
            }
        }
        else
            Toast.makeText(this, "ERROR: Enter 1, 2, or 3", Toast.LENGTH_SHORT).show();

    }//end stopAudio(...)

    public void eraseList(View view) {
        Toast.makeText(this, "Erasing", Toast.LENGTH_SHORT).show();
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();
    }//end eraseList(...)

    //unbind from PictureAudioInterface Service
    @Override
    protected void onPause() {
        if(isBound){
            //unbindService(this.serviceConnection);
        }
        super.onPause();
    }//end onPause()

    //bind to PictureAudioInterface Service
    @Override
    protected void onResume() {
        super.onResume();

        if(!isBound){
            boolean b = false;
            Intent i = new Intent(PictureAudioInterface.class.getName());

            ResolveInfo info = getPackageManager().resolveService(i, Context.BIND_AUTO_CREATE);
            i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

            b = bindService(i, this.serviceConnection, Context.BIND_AUTO_CREATE);

            if(b){
                Toast.makeText(this, "Bound", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "bindService() succeeded!");
            }
            else{
                Toast.makeText(this, "Not Bound", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "bindService() failed!");
            }
        }
    }//end onResume()

    //saves the list to shared preferences
    public boolean saveArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor mEdit1 = sp.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(requests);
        mEdit1.putStringSet("list", set);

        return mEdit1.commit();
    }//end saveArray()

    //retrieve list from shared preferences
    public ArrayList<String> getArray() {
        SharedPreferences sp = this.getSharedPreferences(SHARED_PREFS_NAME, Activity.MODE_PRIVATE);

        Set<String> set = sp.getStringSet("list", new HashSet<String>());

        return new ArrayList<String>(set);
    }//end getArray()


}//end CS478Proj5ServiceUser
