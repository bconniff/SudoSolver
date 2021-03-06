/* Copyright (c) 2012, Brendan Conniff
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of Brendan Conniff nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

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