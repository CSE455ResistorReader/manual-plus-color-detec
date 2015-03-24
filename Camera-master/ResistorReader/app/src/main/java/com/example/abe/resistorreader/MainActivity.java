package com.example.abe.resistorreader;
/*
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;


public class MainActivity extends Activity {

    private static String logtag = "CameraApp";
    private static int TAKE_PICTURE = 1;
    final int PIC_CROP = 1;
    private Uri picUri;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraButton = (Button) findViewById(R.id.button_camera);
        Button resultsButton = (Button) findViewById(R.id.results_btn);
        cameraButton.setOnClickListener(cameraListener);
        resultsButton.setOnClickListener(resultsListener);

    }

    private View.OnClickListener resultsListener;

    {
        resultsListener = new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_results);

            }

        };
    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        public void onClick(View v) {
            performCrop();
            takePhoto(v);

        }
    };

    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        picUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, requestCode, intent);

        if (requestCode == Activity.RESULT_OK) {
            Uri selectedImage = picUri;
            getContentResolver().notifyChange(selectedImage, null);

            ImageView imageView = (ImageView) findViewById(R.id.image_camera);
            ContentResolver cr = getContentResolver();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                imageView.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e(logtag, e.toString());
            }
        }
    }


    private void performCrop(Uri picUri){
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        //respond to users whose devices do not support the crop action
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
*/

//ADD PACKAGE HERE

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.Arrays;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_ELLIPSE;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.getStructuringElement;

public class MainActivity extends Activity implements OnClickListener {

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //captured picture uri
    private Uri picUri;
    //declares the bitmap to store the pic
    Bitmap thePic;
    //The image we want to display
    Bitmap displayImage;
    ImageView picView;
    //Load OpenCV libraries.
    static {
        if(!OpenCVLoader.initDebug()) {
            Log.d("ERROR", "Unable to load OpenCV");
        } else {
            Log.d("SUCCESS", "OpenCV loaded");
        }
    }
    Button btnManualClick;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button cameraButton = (Button) findViewById(R.id.button_camera);
        Button resultsButton = (Button) findViewById(R.id.results_btn);
        cameraButton.setOnClickListener(this);
        resultsButton.setOnClickListener(this);
        btnManualClick = (Button) findViewById(R.id.button_manual);
        btnManualClick.setOnClickListener(new View.OnClickListener() {
                                              public void onClick(View v) {
                                                  switch (v.getId()) {
                                                      case R.id.button_manual:
                                                          btnManualClick();
                                                          break;
                                                  }
                                              }

                                          }
        );


    }

    public void btnManualClick(){
        startActivity(new Intent("com.example.abe.resistorreader.MainActivity2"));
    }

    /**
     * Click method to handle user pressing button to launch camera
     */
    public void onClick(View v) {
        if (v.getId() == R.id.button_camera) {
            try {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }
            catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    /**
     * Handle user returning from both capturing and cropping the image
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                //get the Uri for the captured image
                picUri = data.getData();
                Log.v("CSUSB", data.getDataString());
                //carry out the crop operation
                performCrop();
                Log.v("CSUSB", data.getDataString());
                //colorDetect();
            }
            //user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                thePic = extras.getParcelable("data");
                //change content view
                setContentView(R.layout.activity_results);
                //display the returned cropped image
                picView =  (ImageView)findViewById(R.id.resultsView);


                double[] color = new double[3];
                String[] pos = new String[3];

                //puts the x position of the color detected
                color[0] = colorDetect(0   ,96  ,74  ,67  ,227 ,211); //blue
                color[1] = colorDetect(100 ,150 ,94  ,133 ,255 ,255);//red
                color[2] = colorDetect(30  ,120 ,117 ,104 ,234 ,177);//green

                //puts the position in a string
                pos[0] = "blue:  " + Double.toString(color[0]);
                pos[1] = "red:   " + Double.toString(color[1]);
                pos[2] = "green: " + Double.toString(color[2]);

                //print positiont to the screen to verify proper functionality
                TextView view = (TextView) findViewById(R.id.textView);
                TextView view2 = (TextView) findViewById(R.id.textView2);
                TextView view3 = (TextView) findViewById(R.id.textView3);
                TextView view4 = (TextView) findViewById(R.id.textView4);
                view.setText(pos[0]);
                view2.setText(pos[1]);
                view3.setText(pos[2]);

                //print the resistance to the screen
                view4.setText(resistance(color));

                picView.setImageBitmap(displayImage);

            }
        }
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop()
    {
        //take care of exceptions
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        //respond to users whose devices do not support the crop action
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //this function finds the positions of the color specified by the hsv values using color blob detection
    private double colorDetect(int lowh, int lows, int lowv, int highh, int highs, int highv) {
        Mat temp = new Mat (thePic.getWidth(), thePic.getHeight(), CvType.CV_8UC1);
        displayImage = Bitmap.createBitmap(temp.cols(), temp.rows(), Bitmap.Config.ARGB_8888);

        Utils.bitmapToMat(thePic, temp);


        double iLastX = -1;
        double iLastY = -1;


        Mat imgHSV = new Mat();

        cvtColor(temp, imgHSV, COLOR_BGR2HSV);

        //Search for colors between these HSV values.
        Core.inRange(imgHSV, new Scalar(lowh, lows, lowv), new Scalar(highh, highs, highv), imgHSV );

        //removes background noise
        erode(imgHSV,imgHSV, getStructuringElement(MORPH_ELLIPSE, new Size(5,5)));
        dilate(imgHSV, imgHSV, getStructuringElement(MORPH_ELLIPSE, new Size(5,5)));

        dilate(imgHSV, imgHSV, getStructuringElement(MORPH_ELLIPSE, new Size(5,5)));
        erode(imgHSV,imgHSV, getStructuringElement(MORPH_ELLIPSE, new Size(5,5)));

        //used to find the center of the color blob
        Moments oMoments = Imgproc.moments(imgHSV);
        double dM01 = oMoments.get_m01();
        double dM10 = oMoments.get_m10();
        double dArea = oMoments.get_m00();


        double posX = dM10 / dArea;
        double posY = dM01 / dArea;

        if(iLastX >= 0 && iLastY >= 0 && posX >= 0 && posY >= 0)
        {
            Core.line(temp,new Point(posX, posY), new Point(iLastX, iLastY), new Scalar(255,255,100), 10);
        }
        iLastX = posX;
        iLastY = posY;


       // puts a dot in the center of the color blob
        Core.line(temp,new Point(posX, posY), new Point(iLastX, iLastY), new Scalar(255,255,100), 10);

        Utils.matToBitmap(temp, displayImage);
        picView.setImageBitmap(displayImage);
        return(posX);

    }
    private String resistance (double [] color)
    {
        //holds the value of the resistance
        String res = new String();
        //creates a copy of the array of the positions for sorting
        double [] color_copy = new double[3];
        System.arraycopy(color,0,color_copy,0,color.length);
        Arrays.sort(color);
        //prints the value of the first color found
        for(int i = 0; i < 3 ; i++)
        {
            if(color_copy[i] == color[0])
            {
                if(i == 0)
                    res = "6";
                if(i==1)
                    res = "2";
                if(i==2)
                    res = "5";
            }
        }
        //prints the value of the second color found
        for(int i = 0; i < 3 ; i++)
        {
            if(color_copy[i] == color[1])
            {
                if(i == 0)
                    res += "6";
                if(i==1)
                    res += "2";
                if(i==2)
                    res += "5";
            }
        }
        //prints the value of the third color found
        for(int i = 0; i < 3 ; i++)
        {
            if(color_copy[i] == color[2])
            {
                if(i == 0)
                    res += "x10^6";
                if(i==1)
                    res += "x10^2";
                if(i==2)
                    res += "x10^5";
            }
        }




        return(res);

    }
}