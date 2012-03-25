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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ezmeal.main.R;
import com.ezmeal.server.Communication_API;

public class RegisterActivity extends Activity implements OnClickListener {
	private Button submitBtn, cancelBtn;
	private TextView resultText;
	
	//messages for result
	private String EMPTY_USERNAME      = "Your ITSC account is required.";
	private String EMPTY_PASSWD        = "Password is required.";
	private String EMPTY_NICKNAME      = "Nick name is required.";
	private String INVALID_ITSC        = "The ITSC is invalid.";
	private String SHORT_PASSWD        = "The length of password should be no less than 6 characters.";
	private String LONG_PASSWD         = "The length of password should be no more than 20 characters.";
	private String SPACE_IN_PASSWD     = "No space is allowed in password.";
	private String INCONSISTENT_PASSWD = "The confirmed password is inconsistent.";
	private String EXISTED_ITSC        = "You have already signed up.";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        submitBtn = (Button) findViewById(R.id.buttonSubmitRegForm);
        submitBtn.setOnClickListener(this);
        cancelBtn = (Button) findViewById(R.id.buttonCancelRegForm);
        cancelBtn.setOnClickListener(this);
        resultText = (TextView) findViewById(R.id.labelRegResult);
    }
    
    /**
     * Check if the user input is valid.
     */
    private boolean checkInput(String uname, String passwd,String confPasswd,
    		String nname) {
    	//check if the username field is empty
    	if (uname.length() == 0) {
    		resultText.setText(EMPTY_USERNAME);
    		return false;
    	}
    	//check if the password field is empty
    	else if (passwd.length() == 0) {
    		resultText.setText(EMPTY_PASSWD);
    		return false;
    	}
    	//check if the nick name field is empty
    	else if (nname.length() == 0) {
    		resultText.setText(EMPTY_NICKNAME);
    		return false;
    	}
    	//check if the username contains any space(s)
    	else if (uname.indexOf(" ") >= 0) {
    		resultText.setText(INVALID_ITSC);
    		return false;
    	}
    	//check if the password is too short
    	else if (passwd.length() < 6) {
    		resultText.setText(SHORT_PASSWD);
    		return false;
    	}
    	//check if the password is too long
    	else if (passwd.length() > 20) {
    		resultText.setText(LONG_PASSWD);
    		return false;
    	}
    	//check if the password contains any space(s)
    	else if (passwd.indexOf(" ") >= 0) {
    		resultText.setText(SPACE_IN_PASSWD);
    		return false;
    	}
    	//check if confirmed password is consistent
    	else if (!passwd.equals(confPasswd)) {
    		resultText.setText(INCONSISTENT_PASSWD);
    		return false;
    	}
    	return true;
    }
    
    /**
     * Post the registration form to the server.
     */
    private void postRegData() {
    	//get input
    	EditText uname = (EditText) findViewById(R.id.editTextRegUserName);
    	String username = uname.getText().toString();
    	
    	EditText pwd = (EditText) findViewById(R.id.editTextRegPassword);
    	String password = pwd.getText().toString();
    	
    	EditText cPwd = (EditText) findViewById(R.id.editTextRegConfirmPassword);
    	String confirmedPassword = cPwd.getText().toString();
    	
    	EditText nname = (EditText) findViewById(R.id.editTextRegNickName);
    	String nickname = nname.getText().toString();
    	
    	if (checkInput(username, password, confirmedPassword, nickname)) {
    		//TODO: Post data to the server
    		//Finish the activity, and go back to the welcome page.
    		Communication_API.register(username, password, nickname);
    		finish();
    	}
    	
    }
    
    public void onClick(View view) {
    	if (view == submitBtn) {
    		postRegData();
    	}
    	else if (view == cancelBtn) {
    		//Finish the activity, and go back to the welcome page.
    		finish();
    	}
    }
}
