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
	private String username;
	private String password;
	
	/**
	 * Get functions
	 */
	public String getUserName() {
		return username;
	}
	public String getPassword() {
		return password;
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
}