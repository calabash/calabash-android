package sh.calaba.instrumentationbackend.utils;

import android.graphics.Matrix;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ViewWrapper {
    private static final Class viewRootClass;
    private static final Class viewRootImplClass;
    private static final Field mCurScrollYField;

    static {
        try {
            Class viewRootClassTmp = null;

            try {
                viewRootClassTmp = Class.forName("android.view.ViewRoot");
            } catch (ClassNotFoundException e) {
            }

            viewRootClass = viewRootClassTmp;

            Class viewRootImplClassTmp = null;

            try {
                viewRootImplClassTmp = Class.forName("android.view.ViewRootImpl");
            } catch (ClassNotFoundException e) {
            }

            viewRootImplClass = viewRootImplClassTmp;

            if (viewRootImplClass != null) {
                mCurScrollYField = viewRootImplClass.getDeclaredField("mCurScrollY");
                mCurScrollYField.setAccessible(true);
            } else {
                mCurScrollYField = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Field mLeftField;
    private static final Field mTopField;
    private static final Field mScrollXField;
    private static final Field mScrollYField;
    private static final Field mParentField;
    private static final Field mAttachInfoField;
    private static final Method hasIdentityMatrixMethod;

    static {
        try {
            mLeftField = android.view.View.class.getDeclaredField("mLeft");
            mLeftField.setAccessible(true);
            mTopField = android.view.View.class.getDeclaredField("mTop");
            mTopField.setAccessible(true);
            mScrollXField = android.view.View.class.getDeclaredField("mScrollX");
            mScrollXField.setAccessible(true);
            mScrollYField = android.view.View.class.getDeclaredField("mScrollY");
            mScrollYField.setAccessible(true);
            mParentField = android.view.View.class.getDeclaredField("mParent");
            mParentField.setAccessible(true);
            mAttachInfoField = android.view.View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);

            Method hasIdentityMatrixMethodTmp = null;

            try {
                hasIdentityMatrixMethodTmp = android.view.View.class.getDeclaredMethod("hasIdentityMatrix");
            } catch (NoSuchMethodException e) {
            }

            hasIdentityMatrixMethod = hasIdentityMatrixMethodTmp;

            if (hasIdentityMatrixMethod != null) {
                hasIdentityMatrixMethod.setAccessible(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Class attachInfoClass;
    private static final Field mWindowLeftField;
    private static final Field mWindowTopField;

    static {
        try {
            attachInfoClass = Class.forName("android.view.View$AttachInfo");
            mWindowLeftField = attachInfoClass.getDeclaredField("mWindowLeft");
            mWindowLeftField.setAccessible(true);
            mWindowTopField = attachInfoClass.getDeclaredField("mWindowTop");
            mWindowTopField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private View view;

    public ViewWrapper(android.view.View androidView) {
        this.view = new View();

        try {
            view.initializeFromAndroidView(androidView);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int[] getLocationOnScreen() {
        int[] location = new int[2];

        if (Build.VERSION.SDK_INT >= 15) {
            view.getLocationOnScreen15Plus(location);
        } else {
            view.androidView.getLocationOnScreen(location);
        }

        return location;
    }

    private interface ViewParent {

    }

    private class ViewRootImpl implements ViewParent {
        /*
            Copied from android.view.View
         */
        int mCurScrollY;

        protected void initializeFromAndroidViewRootImpl(Object androidViewRootImpl) throws IllegalAccessException {
            mCurScrollY = ((Integer)mCurScrollYField.get(androidViewRootImpl)).intValue();
        }
    }

    private class View implements ViewParent {
        private android.view.View androidView;

        /*
            Copied from android.view.View
         */
        protected int mLeft;
        protected int mTop;
        protected int mScrollX;
        protected int mScrollY;
        protected ViewParent mParent;
        AttachInfo mAttachInfo;

        protected void initializeFromAndroidView(android.view.View androidView) throws IllegalAccessException {
            this.androidView = androidView;

            mLeft = ((Integer)mLeftField.get(androidView)).intValue();
            mTop = ((Integer)mTopField.get(androidView)).intValue();
            mScrollX = ((Integer)mScrollXField.get(androidView)).intValue();
            mScrollY = ((Integer)mScrollYField.get(androidView)).intValue();

            android.view.ViewParent androidViewParent = (android.view.ViewParent) mParentField.get(androidView);

            if (androidViewParent instanceof android.view.View) {
                View parentView = new View();
                parentView.initializeFromAndroidView((android.view.View) androidViewParent);
                mParent = (ViewParent) parentView;
            } else if (viewRootImplClass != null && viewRootImplClass.isInstance(androidViewParent)) {
                ViewRootImpl parentView = new ViewRootImpl();
                parentView.initializeFromAndroidViewRootImpl(androidViewParent);
                mParent = (ViewParent) parentView;
            } else if (viewRootClass != null && viewRootClass.isInstance(androidViewParent)) {
                // TODO: Add impl for older devices
            } else {
                throw new RuntimeException("Unknown class '" + androidViewParent.getClass().getName() + " as a parent");
            }

            mAttachInfo = new AttachInfo();
            mAttachInfo.initializeFromAndroidAttachInfo(mAttachInfoField.get(androidView));
        }

        private boolean hasIdentityMatrix() {
            try {
                return ((Boolean) hasIdentityMatrixMethod.invoke(androidView)).booleanValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Matrix getMatrix() {
            return androidView.getMatrix();
        }

        public void getLocationInWindow15Plus(int[] location) {
            if (location == null || location.length < 2) {
                throw new IllegalArgumentException("location must be an array of two integers");
            }

            if (mAttachInfo == null) {
                // When the view is not attached to a window, this method does not make sense
                location[0] = location[1] = 0;
                return;
            }

            //float[] position = mAttachInfo.mTmpTransformLocation;
            float[] position = new float[2];
            position[0] = position[1] = 0.0f;

            if (!hasIdentityMatrix()) {
                getMatrix().mapPoints(position);
            }

            position[0] += mLeft;
            position[1] += mTop;

            ViewParent viewParent = mParent;
            while (viewParent instanceof View) {
                final View view = (View) viewParent;

                position[0] -= view.mScrollX;
                position[1] -= view.mScrollY;

                if (!view.hasIdentityMatrix()) {
                    view.getMatrix().mapPoints(position);
                }

                position[0] += view.mLeft;
                position[1] += view.mTop;

                viewParent = view.mParent;
            }

            if (viewParent instanceof ViewRootImpl) {
                // *cough*
                final ViewRootImpl vr = (ViewRootImpl) viewParent;
                position[1] -= vr.mCurScrollY;
            }

            location[0] = (int) (position[0] + 0.5f);
            location[1] = (int) (position[1] + 0.5f);
        }

        public void getLocationOnScreen15Plus(int[] location) {
            getLocationInWindow15Plus(location);

            final AttachInfo info = mAttachInfo;
            if (info != null) {
                location[0] += info.mWindowLeft;
                location[1] += info.mWindowTop;
            }
        }

        final class AttachInfo {
            protected void initializeFromAndroidAttachInfo(Object androidAttachInfo) throws IllegalAccessException {
                mWindowLeft = ((Integer) mWindowLeftField.get(androidAttachInfo)).intValue();
                mWindowTop = ((Integer) mWindowTopField.get(androidAttachInfo)).intValue();
            }

            /*
                Copied from android.view.View$AttachInfo
            */

            int mWindowLeft;
            int mWindowTop;
        }
    }
}
