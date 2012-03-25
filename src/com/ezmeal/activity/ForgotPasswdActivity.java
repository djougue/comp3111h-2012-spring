 /*
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package com.ezmeal.activity;

import com.ezmeal.main.R;
import com.ezmeal.main.UserApp;
import com.ezmeal.main.WelcomeActivity;
import com.ezmeal.server.Communication_API;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ForgotPasswdActivity extends Activity implements OnClickListener {
	private Button backBtn;
	private TextView headerTitle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_passwd);
        
        backBtn = (Button) findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(this);
        headerTitle = (TextView) findViewById(R.id.labelHeader);
        headerTitle.setText("Forgot Password");
    }
    
    public void onClick(View view) {
    	if (view == backBtn) {
    		//Finish the activity, and go back to the welcome page.
    		finish();
    	}
    }
}
