package org.pxanic.sudosolver.ui;

import org.pxanic.sudosolver.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.FontMetrics;
//import android.graphics.Rect;

public class NumberPickerView extends View {
   private final int k, n;
   private final Paint thin, bg, num, sel;
   
   private int selX = -1, selY = -1;
   
   public NumberPickerView(Context context) {
      this(context, null);
   }

   public NumberPickerView(Context context, AttributeSet attrs) {
      super(context, attrs);
      
      final Resources res = getResources();
      
      k = res.getInteger(R.integer.const_k);
      n = k * k;
      
      thin = new Paint(Paint.ANTI_ALIAS_FLAG);
      bg = new Paint(Paint.ANTI_ALIAS_FLAG);
      num = new Paint(Paint.ANTI_ALIAS_FLAG);
      sel = new Paint(Paint.ANTI_ALIAS_FLAG);
      
      thin.setStrokeWidth(res.getInteger(R.integer.minor_line_width));
      num.setStyle(Style.FILL);
      num.setTextAlign(Paint.Align.CENTER);
      
      thin.setColor(res.getColor(R.color.minor_line));
      bg.setColor(res.getColor(R.color.bg));
      num.setColor(res.getColor(R.color.number));
      sel.setColor(res.getColor(R.color.selected));
   }

   @Override
   public boolean onTouchEvent(MotionEvent event) {
      final int x = (int)((k * event.getX()) / getWidth());
      final int y = (int)((k * event.getY()) / getHeight());
      selX = (x < 0) ? 0 : (x >= k) ? k - 1 : x;
      selY = (y < 0) ? 0 : (y >= k) ? k - 1 : y;
      invalidate();
      return true;
   }
   
   public void clear() {
      setVal(0);
   }
   
   public void setVal(int v) {
      if (v > 0 && v <= n) {
         final int z = v - 1;
         selX = z % k;
         selY = (z - selX) / k;
      } else {
         selX = -1;
         selY = -1;
      }
   }
   
   public int getVal() {
      return 1 + selX + k * selY;
   }
   
   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // Keep the view squared
       final int w = MeasureSpec.getSize(widthMeasureSpec);
       final int h = MeasureSpec.getSize(heightMeasureSpec);
       final int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
       setMeasuredDimension(d, d);
   }
   
   @Override
   protected void onDraw(Canvas c) {
      super.onDraw(c);
      
      final int width = getWidth();
      final int height = getHeight();
      
      final float boxHeight = (float)height / (float)k;
      final float boxWidth = (float)width / (float)k;
      
      c.drawRect(0, 0, width, height, bg);
      
      if (selX >= 0 && selY >= 0) {
         final float x = selX * boxWidth, y = selY * boxHeight;
         c.drawRect(x, y, x + boxWidth, y+boxHeight, sel);
      }
      
      for (int i = 1; i < k; i++) {
         final float y = i * boxHeight;
         final float x = i * boxWidth;
         c.drawLine(0, y, getWidth(), y, thin);
         c.drawLine(x, 0, x, getHeight(), thin);
      }
      
      num.setTextSize(0.75f * boxHeight);
      num.setTextScaleX(boxWidth / boxHeight);
      
      final FontMetrics fm = num.getFontMetrics();
      
      final float xOff = boxWidth / 2;
      final float yOff = boxHeight / 2 - (fm.ascent + fm.descent) / 2;
      
      for (int i = 0; i < k; i++) {
         for (int j = 0; j < k; j++) {
            final int v = 1 + i + k * j;
            final float x = i * boxWidth + xOff;
            final float y = j * boxHeight + yOff;
            c.drawText(Integer.toString(v), x, y, num);
         }
      }
   }
}