# First Test Cases (2 Apr) #
  * [Test Cases](FirstTestCase#Test_Cases.md)
  * [Results](FirstTestCase#Results.md)

---


## Test Cases ##
  1. In welcome page, can user just leave without entering username or password? (Yes/No)
> > Yes. Just press back button on the device. Then the application will exit.
> > In Sign up page, a user wanting to register needs to provide his/her ITSC. After registration, an email will be sent out for confirmation purpose. The user need to click the link in the email to activate its account.
  1. If an ITSC has been used for registration and the account has been activated, can the ITSC be used again for registration? (Yes/No)
> > No. “You have already signed up” will be displayed.
  1. If an ITSC has been used for registration without activated, can the ITSC be used again for registration? (Yes/No)
> > Yes. The information of previous registration will be deleted.
  1. If an ITSC has been used for registration without activation and the ITSC is used again for registration with another confirmation email sending out, is the previous confirmation email still effective? (Yes/No)
> > No.
  1. Can a registered but not activated account log in? (Yes/No)
> > No. The information of this kind of users’ will be put in a “Temporary Users” list in the database.
  1. Will the application keep logging in, if the connection failed? (Yes/No)
> > No. Error message will be displayed, and users should to try to log in again manually.
  1. After logging in successfully, is the application functional if connection failed?
> > No. Whenever there is an interaction between the device and the server, a connection timeout and a socket timeout will be set on the device (client) side. Error message will be displayed when timeout occurs.
  1. In the main page, what if the user press the back button on the device?
> > It will log out the system and go back to the welcome page. Pressing a dish on the list, a detailed page will be shown.
  1. If a user presses the back button, is the view the same as the original view before detail page shows up? (Yes/No)
> > Yes.
  1. In the setting page, if a user doesn’t press the submit button after making some change, will the change be saved? (Yes/No)
> > No.
  1. In the setting page, can a user change its nickname with a new one containing space? (Yes/No)
> > No.
  1. In the Settings page, if a user both changes the nickname and the password while one of them doesn’t follow the requirement, will other changes be submitted? (Yes/No)
> > No. Everything should be in correct form, and be submitted together.
  1. In the Settings page, if a user does some modification during the application submitting the change, will the application submit the newest change? (Yes/No)
> > No.
  1. In the Settings page, is there any notification if a user leaves the page without saving the changes? (Yes/No)
> > Yes.
  1. In the Settings page, will the notification show up if a user leaves the page with neither making change and submitting the change? (Yes/No)
> > No. An alert dialog will pop up ONLY if the user has modified something.
  1. In the Settings page, a user has submitted some change. Then the user makes another change and leaves without submitting. Will the notification show up?(Yes/No)
> > Yes.
  1. The test cases for My Taste page and Registration page are similar with the one of the Settings page.
> > As is stated above.
  1. A user goes into SHAKE page (called page A) from page B. When the user presses the back button, will the application go back to page B? (Yes/No)
> > Yes. Actually, the OnClickListener calls finish() if the back button is pressed.
  1. A user goes into SHAKE page(called page A) from a detail page, page B. The user can press the image shown in page A to enter the detail page(page C) for the result. If the user shakes its device, a SHAKE page(page D) will show up. If the user press the result, another detail page, page E, will show up. Can user go back to page C with the back button? (Yes/No)
> > No. In page C, the effect for shaking the device is the same as pressing the back button and then pressing the reshake button, which means page D is the same as page A but with a new result. And page E will take place with the page C.
  1. In the SHAKE page, if a user reshake when a result is loading, will the new shake start intermediately? (Yes/No)
> > No.

## Results ##
Bugs found:
  * On the Welcome page, Registration page and Settings page, if a user enters single quotation marks (') in his/her user name, password and/or nickname, errors may occur. For example, if a new user set "aaa'aaa" as his/her password when registering, the app will display error message "You have already signed up", even if the user name has never been used.
    * <b><u>Reasons</u></b>: A single quotation mark is a special character for PHP. The server will return error messages if the received contents contain special characters.
    * <b><u>Solutions</u></b>: Forbid users to use any single quotation marks in his/her user name, password and/or nickname.
    * <b><u>Potential Problems</u></b>: In the comment function, we cannot forbid users to use single quotation marks. Therefore, we need to come up with a better idea to cope with this problems. The provisional solution is to add a reverse slash mark (\) in front of each single quotation mark (actually, each special character).