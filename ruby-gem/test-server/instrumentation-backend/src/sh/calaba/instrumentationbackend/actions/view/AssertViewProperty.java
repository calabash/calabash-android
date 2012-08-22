package sh.calaba.instrumentationbackend.actions.view;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import sh.calaba.instrumentationbackend.Result;
import sh.calaba.instrumentationbackend.TestHelpers;
import sh.calaba.instrumentationbackend.actions.Action;


/**
 * eg: performAction( 'assert_view_property', 'my_view', 'visibility', 'visible' )  // or invisible/gone
 * eg: performAction( 'assert_view_property', 'my_view', 'drawable', 'expected_id' )
 * eg: performAction( 'assert_view_property', 'my_view', 'compoundDrawables', 'left', 'expected_id' )
 * 
 * @author Nicholas Albion
 */
public class AssertViewProperty extends GetViewProperty implements Action {
	private static final String TAG = "assert_view_property";

	@Override
	protected Result getPropertyValue( String propertyName, View view, String[] args ) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if( "compoundDrawables".equals(propertyName) ) {
			if( view instanceof TextView ) {
				// performAction( 'get_view_property', 'my_view', 'compoundDrawables', 'left', 'expected_id'
				String expected_id = args[3];
				int pos;
				if( "top".equals(args[2]) ) {
					pos = 1;
				} else if( "right".equals(args[2]) ) {
					pos = 2;
				} else if( "bottom".equals(args[2]) ) {
					pos = 3;
				} else {
					pos = 0;
				}
				Drawable[] drawables = ((TextView)view).getCompoundDrawables();		// [left, top, right, bottom]
				Drawable actualDrawable = drawables[pos];
				Drawable expectedDrawable = TestHelpers.getDrawableById(expected_id);
				return assertSameDrawables( propertyName + ", " + args[2], expectedDrawable, actualDrawable );
			} else {
				throw new IllegalArgumentException("compoundDrawables is only supported for subclasses of TextView, not " + view.getClass().getName());
			}
		} else {
			return super.getPropertyValue(propertyName, view, args);
		}
	}
	
	/**
	 * @param propertyName
	 * @param propertyValue
	 * @param args
	 * @return successResult if <code>propertyValue</code> is equal to <code>args[2]</code>
	 * 			otherwise provides a descriptive message and the expected and actual values in the <code>bonusInformation</code> 
	 */
	@Override
	protected Result processProperty( String propertyName, Object propertyValue, String[] args ) {
		String expected = args[2];
		String value;
		if( propertyValue == null ) {
			if( expected == null || "null".equals(expected) ) {
				return Result.successResult();
			}
			value = "null";
		} else if( "drawable".equals(propertyName) ) {
			Drawable actualDrawable = (Drawable)propertyValue; //  ((ImageButton)view).getDrawable();
			if( actualDrawable instanceof DrawableContainer ) {
				actualDrawable = ((DrawableContainer)actualDrawable).getCurrent();
			}
			Drawable expectedDrawable = TestHelpers.getDrawableById(expected);
			return assertSameDrawables( propertyName, expectedDrawable, actualDrawable );
		} else {
			value = propertyValue.toString();
			if( value.equals(expected) ) {
				return Result.successResult();
			}
		}
		Result result = new Result(false, "For " + propertyName + " expected " + expected + " but was actually " + value);
		result.addBonusInformation( expected );
		result.addBonusInformation( value );
		return result;
	}
    
	private Result assertSameDrawables( String propertyName, Drawable expectedDrawable, Drawable actualDrawable ) {
		if( sameDrawables( expectedDrawable, actualDrawable) ) {
			return Result.successResult();
		} else {
			Result result = new Result(false, "For " + propertyName + " expected " + expectedDrawable + " but was actually " + actualDrawable);
			result.addBonusInformation( expectedDrawable == null ? "null" : expectedDrawable.toString() );
			result.addBonusInformation( actualDrawable == null ? "null" : actualDrawable.toString() );
			return result;
		}
	}
	
	private boolean sameDrawables( Drawable expected, Drawable actual ) {
		if( expected.equals(actual) ) {
			return true;
		}
		if( expected instanceof DrawableContainer ) {
			expected = ((DrawableContainer)expected).getCurrent();
		}
		if( actual instanceof DrawableContainer ) {
			actual = ((DrawableContainer)actual).getCurrent();
		}
		if( (expected instanceof BitmapDrawable) && (actual instanceof BitmapDrawable) ) {
			Bitmap expectedBitmap = ((BitmapDrawable)expected).getBitmap();
			Bitmap actualBitmap = ((BitmapDrawable)actual).getBitmap();
			try {
				// As pointed out by kbielenberg, Bitmap.sameAs() was only added in level 12/Android 3.1/Honeycomb MR1
				Method sameAs = Bitmap.class.getMethod("sameAs", Bitmap.class);
				return (Boolean)sameAs.invoke( expectedBitmap, actualBitmap );
			} catch (Exception e) {
				if( expectedBitmap.getWidth() != actualBitmap.getWidth() ) { return false; }
				if( expectedBitmap.getHeight() != actualBitmap.getHeight() ) { return false; }
				if( expectedBitmap.getConfig() != actualBitmap.getConfig() ) { return false; }
				boolean bitmapsEqual = expectedBitmap.equals(actualBitmap);
				if( !bitmapsEqual ) {
					Log.i(TAG, "Bitmaps are not equal");
				}
				boolean drawablesEqual = expected.equals(actual);
				if( !drawablesEqual ) {
					Log.i(TAG, "Drawables are not equal");
				}
				return bitmapsEqual && drawablesEqual;
			}
		}
		return false;
	}

	@Override
	public String key() {
		return "assert_view_property";
	}
}
