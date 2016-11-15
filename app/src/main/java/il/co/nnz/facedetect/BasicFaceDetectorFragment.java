package il.co.nnz.facedetect;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;


public class BasicFaceDetectorFragment extends Fragment implements View.OnClickListener {

    ImageView imageView;
    private Paint rectPaint;
    private Bitmap defaultBitmap;
    private Bitmap temporaryBitmap;
    private Bitmap eyePatchBitmap;
    private Canvas canvas;
    private Button basicBtn;

    public BasicFaceDetectorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_basic_face_detector, container, false);


        imageView = (ImageView) v.findViewById(R.id.image_view);
        imageView.setImageResource(R.drawable.kids);
        basicBtn = (Button) v.findViewById(R.id.basicBtn);
        basicBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inMutable = true;

        initializeBitmap(bitmapOptions);
        createRectanglePaint();

        canvas = new Canvas(temporaryBitmap);
        canvas.drawBitmap(defaultBitmap, 0, 0, null); //draw on defaultBitmap


        FaceDetector faceDetector = new FaceDetector.Builder(getContext())
                .setTrackingEnabled(false) // disabled because we are using a static image. should be enabled for videos.
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();

        if (!faceDetector.isOperational()) {
            new AlertDialog.Builder(getContext())
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

    /*
    public void processImage(View view) {


    }*/

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


}

