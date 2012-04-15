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

package com.ezmeal.main;

import android.app.Application;

/**
 * UserApp keeps the basic information (e.g. username, password, personal taste,
 * etc.) of a logged-in user.
 * 
 * @author Neal Zheng
 */

public class UserApp extends Application {
	private String username, password, nickname;
	private String tmpUsername;   //used before login
	private boolean isVege, isSpicy, isMeat, isShake;
	
	/**
	 * Get functions
	 */
	public String getUserName() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getNickName() {
		return nickname;
	}
	public String getTmpUserName() {
		return tmpUsername;
	}
	public boolean[] getTaste() {
		boolean[] taste = {isSpicy, isMeat, isVege};
		return taste;
	}
	public boolean isShake() {
		return isShake;
	}
	
	/**
	 * Set functions
	 */
	public void setUserName(String uname) {
		username = uname;
	}
	public void setPassword(String pwd) {
		password = pwd;
	}
	public void setNickName(String nname) {
		nickname = nname;
	}
	public void setTmpUserName(String tmpuname) {
		tmpUsername = tmpuname;
	}
	public void setTaste(boolean[] taste) {
		isSpicy = taste[0];
		isMeat  = taste[1];
		isVege  = taste[2];
	}
	public void setShake(boolean _isShake) {
		isShake = _isShake;
	}
}
