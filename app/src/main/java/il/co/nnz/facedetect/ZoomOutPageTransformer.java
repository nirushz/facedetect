package il.co.nnz.facedetect;

import android.support.v4.view.ViewPager;
import android.view.View;



public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

    // the smallest size the image can have
    private static final float MIN_SCALE = 0.5f;
    // the minimum alpha (invisibility) the image can have
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View view, float position) {

        //positon = the position of the fragment relative to the center of the pager

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        // scaleFactor = the current size of the image (will shrink up to 0.5 from the middle of the view)
        float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
        float verticalMargin = pageHeight * (1 - scaleFactor) / 2;
        float horzontalMargin = pageWidth * (1 - scaleFactor) / 2;

        // if we move the page to the left or the right,move the fragment
        if (position < 0) {
            view.setTranslationX(horzontalMargin - verticalMargin / 2);
        } else {
            view.setTranslationX(-horzontalMargin + verticalMargin / 2);
        }

        // Scale the page down (between MIN_SCALE and 1)
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);

        // Fade the page relative to its size.
        view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
    }
}
