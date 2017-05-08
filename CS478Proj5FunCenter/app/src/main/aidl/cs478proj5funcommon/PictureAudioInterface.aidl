// PictureAudioInterface.aidl
package cs478proj5funcommon;

// Declare any non-default types here with import statements

interface PictureAudioInterface {
    Bitmap getPicture(int id);
    void playAudio(int id);
    void pauseAudio(int id);
    void resumeAudio(int id);
    void stopAudio(int id);
    int test();


        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString);
}
