package org.pxanic.sudosolver.ui;

import org.pxanic.sudosolver.dlx.Solver;
import org.pxanic.sudosolver.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Paint.FontMetrics;
import android.content.res.Resources;
//import android.graphics.Rect;

public class SolverView extends View {
   private final Paint thick, thin, bg, num, sel;
   private final int k, n;
   
   private int selX = 0, selY = 0;
   private final int[][] vals;
   
   public SolverView(Context context) {
      this(context, null);
   }
   
   public SolverView(Context context, AttributeSet attrs) {
      super(context, attrs);
      
      final Resources res = getResources();
      
      thick = new Paint(Paint.ANTI_ALIAS_FLAG);
      thin = new Paint(Paint.ANTI_ALIAS_FLAG);
      bg = new Paint(Paint.ANTI_ALIAS_FLAG);
      num = new Paint(Paint.ANTI_ALIAS_FLAG);
      sel = new Paint(Paint.ANTI_ALIAS_FLAG);
      
      thick.setStrokeWidth(res.getInteger(R.integer.major_line_width));
      thin.setStrokeWidth(res.getInteger(R.integer.minor_line_width));
      num.setStyle(Style.FILL);
      num.setTextAlign(Paint.Align.CENTER);
      
      k = res.getInteger(R.integer.const_k);
      n = k * k;
      vals = new int[n][n];
      
      thick.setColor(res.getColor(R.color.major_line));
      thin.setColor(res.getColor(R.color.minor_line));
      bg.setColor(res.getColor(R.color.bg));
      num.setColor(res.getColor(R.color.number));
      sel.setColor(res.getColor(R.color.selected));
   }

   private void setVals(int[][] inVals) {
      for (int i = 0; i < n; i++)
         for (int j = 0; j < n; j++)
            setCell(i, j, inVals[i][j]);
      invalidate();
   }
   
   private void setCell(int i, int j, int v) {
      if (v > 0 && v <= n)
         vals[i][j] = v;
      else
         vals[i][j] = 0;
   }
   
   private void askVal(int x, int y) {
      final int cellX = x, cellY = y;
      final Context cont = getContext();
      final NumberPickerView npv = new NumberPickerView(cont);
      npv.setVal(vals[x][y]);
      
      new AlertDialog.Builder(cont)
         .setTitle(R.string.select_text)
         .setView(npv)
         .setPositiveButton(R.string.ok_text,
               new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                     setCell(cellX, cellY, npv.getVal());
                     invalidate();
                  }
               }
         )
         .setNeutralButton(R.string.clear_text,
               new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                     setCell(cellX, cellY, 0);
                     invalidate();
                  }
               }
         )
         .setNegativeButton(R.string.cancel_text,
               new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) { }
               }
         )
         .show();
   }
   
   @Override
   public boolean onTouchEvent(MotionEvent event) {
      final int x = (int)((n * event.getX()) / getWidth());
      final int y = (int)((n * event.getY()) / getHeight());
      selX = (x < 0) ? 0 : (x >= n) ? n - 1 : x;
      selY = (y < 0) ? 0 : (y >= n) ? n - 1 : y;
      invalidate();
      if (event.getAction() == MotionEvent.ACTION_UP) {
         askVal(selX, selY);
      }
      return true;
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
      
      final float boxHeight = (float)height / (float)n;
      final float boxWidth = (float)width / (float)n;
      
      c.drawRect(0, 0, width, height, bg);
      
      {
         final float x = selX * boxWidth, y = selY * boxHeight;
         c.drawRect(x, y, x + boxWidth, y+boxHeight, sel);
      }
      
      for (int i = 1; i < n; i++) {
         if (i % k != 0) {
            final float y = i * boxHeight;
            final float x = i * boxWidth;
            c.drawLine(0, y, getWidth(), y, thin);
            c.drawLine(x, 0, x, getHeight(), thin);
         }
      }
      
      for (int i = 1; i < k; i++) {
         final float y = i * k * boxHeight;
         final float x = i * k * boxWidth;
         c.drawLine(0, y, getWidth(), y, thick);
         c.drawLine(x, 0, x, getHeight(), thick);
      }
      
      num.setTextSize(0.75f * boxHeight);
      num.setTextScaleX(boxWidth / boxHeight);
      
      final FontMetrics fm = num.getFontMetrics();
      
      final float xOff = boxWidth / 2;
      final float yOff = boxHeight / 2 - (fm.ascent + fm.descent) / 2;
      
      for (int i = 0; i < n; i++) {
         for (int j = 0; j < n; j++) {
            final int v = vals[i][j];
            if (v > 0 && v <= n) {
               final float x = i * boxWidth + xOff;
               final float y = j * boxHeight + yOff;
               c.drawText(Integer.toString(v), x, y, num);
            }
         }
      }
   }
   
   public boolean doSolve() {
      final Solver s = new Solver(k, vals);
      
      if (s.solved()) {
         setVals(s.getVals());
         return true;
      }
      
      return false;
   }
   
   public void doClear() {
      for (int i = 0; i < n; i++)
         for (int j = 0; j < n; j++)
            setCell(i, j, 0);
      invalidate();
   }
}