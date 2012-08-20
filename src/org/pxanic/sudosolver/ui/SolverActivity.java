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
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;

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
                  solv.doSolve();
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
   
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      final SolverView solv = (SolverView)findViewById(R.id.input);
      
      switch (item.getItemId()) {
          case MENU_SOLVE:
             solv.doSolve();
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