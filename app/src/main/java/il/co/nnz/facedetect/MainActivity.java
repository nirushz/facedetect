package il.co.nnz.facedetect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

public class MainActivity extends AppCompatActivity {
    /*
    private ImageView imageView;
    private Paint rectPaint;
    private Bitmap defaultBitmap;
    private Bitmap temporaryBitmap;
    private Bitmap eyePatchBitmap;
    private Canvas canvas;
    */

//---------------------------------------------------
    private static final int NUMBER_OF_PAGES = 2;

    private ViewPager viewPager;

    private ImageView dot1;//left
    private ImageView dot2;//middle
    private ImageView dot3;//right

    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_main);
/*
        imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.kids);
*/
        //--------------------------------------------

        // referencing views
        dot1 = (ImageView) findViewById(R.id.pager_intro_indic_left);
        dot2 = (ImageView) findViewById(R.id.pager_intro_indic_center);
        dot3 = (ImageView) findViewById(R.id.pager_intro_indic_right);
        viewPager = (ViewPager) findViewById(R.id.pager_viewPager);

        // Instantiate a PagerAdapter.
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager()); //new fragmant manager instance

        //create pages
        BasicFaceDetectorFragment page1 = new BasicFaceDetectorFragment();
        GalleryFaceDetectorFragment page2 = new GalleryFaceDetectorFragment();

        //add pages to our adapter
        viewPagerAdapter.addFragment(page1);
        viewPagerAdapter.addFragment(page2);

        //set adapter to the view pager
        viewPager.setAdapter(viewPagerAdapter);

        // add listener to know when pages have changed
        ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


                                              @Override
                                              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                              }

                                              @Override
                                              public void onPageSelected(int position) {
                                                  switch (position) {
                                                      case 0:
                                                          dot1.setImageResource(R.drawable.page_dot_selected);
                                                          dot2.setImageResource(R.drawable.page_dot_unselected);
                                                          dot3.setImageResource(R.drawable.page_dot_unselected);
                                                          break;
                                                      case 1:
                                                          dot1.setImageResource(R.drawable.page_dot_unselected);
                                                          dot2.setImageResource(R.drawable.page_dot_selected);
                                                          dot3.setImageResource(R.drawable.page_dot_unselected);
                                                          break;
                                                      case 2:
                                                          dot1.setImageResource(R.drawable.page_dot_unselected);
                                                          dot2.setImageResource(R.drawable.page_dot_unselected);
                                                          dot3.setImageResource(R.drawable.page_dot_selected);
                                                          break;
                                                      default:
                                                          break;
                                                  }
                                              }

                                              }

                                              @Override
                                              public void onPageScrollStateChanged(int state) {

                                              }
                                          });
/*
    public void processImage(View view) {

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inMutable = true;

        initializeBitmap(bitmapOptions);
        createRectanglePaint();

        canvas = new Canvas(temporaryBitmap);
        canvas.drawBitmap(defaultBitmap, 0, 0, null); //draw on defaultBitmap


        FaceDetector faceDetector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false) // disabled because we are using a static image. should be enabled for videos.
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(this)
                    .setMessage("Face Detector could not be set up on your device :(")
                    .show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(defaultBitmap).build();  //create a frame using the default bitmap
            SparseArray<Face> sparseArray = faceDetector.detect(frame);  //call on the face detector to get the face objects

            detectFaces(sparseArray);

            imageView.setImageDrawable(new BitmapDrawable(getResources(), temporaryBitmap));

            faceDetector.release();
        }
    }

    private void initializeBitmap(BitmapFactory.Options bitmapOptions) {
        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kids,
                bitmapOptions);
        temporaryBitmap = Bitmap.createBitmap(defaultBitmap.getWidth(), defaultBitmap
                .getHeight(), Bitmap.Config.RGB_565);
        eyePatchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eye_patch,
                bitmapOptions);
    }

    private void createRectanglePaint() {
        rectPaint = new Paint();
        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStyle(Paint.Style.STROKE);
    }

    private void detectFaces(SparseArray<Face> sparseArray) {

        for (int i = 0; i < sparseArray.size(); i++) {
            Face face = sparseArray.valueAt(i);

            float left = face.getPosition().x;
            float top = face.getPosition().y;
            float right = left + face.getWidth();
            float bottom = right + face.getHeight();
            float cornerRadius = 2.0f;

            RectF rectF = new RectF(left, top, right, bottom); //RectF holds four float coordinates for a rectangle
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, rectPaint);

            //the rectangles were drawn. now we call the method that draws the Landmarks
            detectLandmarks(face);
        }
    }

    private void detectLandmarks(Face face) {
        for (Landmark landmark : face.getLandmarks()) {

            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            canvas.drawCircle(cx, cy, 10, rectPaint);

            drawLandmarkType(landmark.getType(), cx, cy);

            drawEyePatchBitmap(landmark.getType(), cx, cy);
        }
    }

    private void drawLandmarkType(int landmarkType, float cx, float cy) {
        String type = String.valueOf(landmarkType);
        rectPaint.setTextSize(50);
        canvas.drawText(type, cx, cy, rectPaint);
    }

    private void drawEyePatchBitmap(int landmarkType, float cx, float cy) {

        if (landmarkType == 4) {
            // TODO: Optimize so that this calculation is not done for every face
            int scaledWidth = eyePatchBitmap.getScaledWidth(canvas);
            int scaledHeight = eyePatchBitmap.getScaledHeight(canvas);
            canvas.drawBitmap(eyePatchBitmap, cx - (scaledWidth / 2), cy - (scaledHeight / 2), null);
        }
    }
    */
}
