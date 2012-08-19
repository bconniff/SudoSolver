package org.pxanic.sudosolver.ui;

import org.pxanic.sudosolver.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class SolverActivity extends Activity {
   private static final int MENU_SOLVE = 0;
   private static final int MENU_CLEAR = 1;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.solver);
      
      final SolverView solv = (SolverView)findViewById(R.id.input);
      
      findViewById(R.id.solve_button).setOnClickListener(
            new OnClickListener() {
               @Override
               public void onClick(View v) {
                  solveOrToast(solv);
               }
            }
      );
      
      findViewById(R.id.clear_button).setOnClickListener(
            new OnClickListener() {
               @Override
               public void onClick(View v) {
                  solv.doClear();
               }
            }
      );
   }
   
   public void solveOrToast(SolverView solv) {
      if (!solv.doSolve())
         Toast.makeText(
               getApplicationContext(),
               R.string.solve_failed,
               Toast.LENGTH_SHORT
         ).show();
   }
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      final SolverView solv = (SolverView)findViewById(R.id.input);
      
      switch (item.getItemId()) {
          case MENU_SOLVE:
             solveOrToast(solv);
             return true;
          case MENU_CLEAR:
             solv.doClear();
             return true;
      }

      return false;
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.solver, menu);
      super.onCreateOptionsMenu(menu);

      menu.add(0, MENU_SOLVE, 0, R.string.solve_text);
      menu.add(0, MENU_CLEAR, 0, R.string.clear_text);

      return true;
   }
}
