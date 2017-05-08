package com.example.com.cs478proj5funcenter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import cs478proj5funcommon.PictureAudioInterface;

/**
 * Created by aloysiusparedes on 4/23/17.
 */

public class PictureAudioImplementation extends Service {
    //an array holding the bitmap images
    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    private static final int NOTIFICATION_ID = 1;
    private MediaPlayer player;

    private final PictureAudioInterface.Stub mBinder= new PictureAudioInterface.Stub(){
        public Bitmap getPicture(int id){
            Bitmap b;
            synchronized (bitmapArray){
                b = bitmapArray.get(id);
            }
            return b;
        }//end getPicture(...)

        public void playAudio(int id){
            play(id);
        }//end playAudio(...)

        public void pauseAudio(int id){
            pause();
        }//end pauseAudio(...)
        
        public void resumeAudio(int id){
            resume();
        }//end resumeAudio(...)
        
        public void stopAudio(int id){
            stop();
        }//end stopAudio(...)
        
        public int test(){
            return -999;
        }
    };//end mBinder

    @Override
    public void onCreate(){
        super.onCreate();
        initializeArray();

    }//end onCreate()


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }//end onBind(...)

    public void initializeArray(){
        bitmapArray.add(BitmapFactory.decodeResource(getResources(), R.drawable.h3h3));
        bitmapArray.add(BitmapFactory.decodeResource(getResources(), R.drawable.myhero));
        bitmapArray.add(BitmapFactory.decodeResource(getResources(), R.drawable.theoffice));
    }//end initializeArray()

    public void setPlayer(int i){
        if (i == 1)
            player = MediaPlayer.create(this, R.raw.office);
        else if (i == 2)
            player = MediaPlayer.create(this, R.raw.bob);
        else
            player = MediaPlayer.create(this, R.raw.scooby);
    }//end setPlayer(...)


    @Override
    public void onDestroy() {
        if(player != null) {
            player.stop();
            player.release();
        }
        stopSelf();
    }//end onDestroy()


    private void play(int num) {
        //if(player == null)
        setPlayer(num);

        if(player.isPlaying()){
            System.out.println("Playing already");
        }
        else
            player.start();

        if(player != null) {
            player.setVolume(100, 100);
            player.setLooping(false);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    stop();
                }
            });
        }

        // Create a notification area notification so the user
        // can get back to the MusicServiceClient
        final Intent notificationIntent = new Intent();
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        final Notification notification = new Notification.Builder(
                getApplicationContext())
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setOngoing(true).setContentTitle("Music Playing")
                .setContentText("Click to Access Music Player")
                .setContentIntent(pendingIntent).build();

        // Put this Service in a foreground state, so it won't
        // readily be killed by the system
        startForeground(NOTIFICATION_ID, notification);
    }

    private void pause(){
        if(player.isPlaying())
            player.pause();
        else
            Toast.makeText(this, "No song currently playing", Toast.LENGTH_SHORT).show();
    }

    private void resume(){
        if(player.isPlaying())
            Toast.makeText(this, "Can't resume a song that's already playing", Toast.LENGTH_SHORT).show();
        else
            player.start();
    }

    private void stop(){
        player.seekTo(0);
        player.pause();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {

        // Don't automatically restart this Service if it is killed
        return START_NOT_STICKY;
    }

}//end PictureAudioImplementation class
