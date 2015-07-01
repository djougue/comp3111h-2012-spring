# Second Test Cases (16 Apr) #
  * [Test Cases](SecondTestCase#Test_Cases.md)
    * [Forgot Password Page](SecondTestCase#Forgot_Password_Page.md)
    * [Search Page](SecondTestCase#Search_Page.md)
  * [Results](SecondTestCase#Results.md)

---


## Test Cases ##
#### <u>Forgot Password Page</u> ####
1. Can a user use a used confirmation code?
> No.
2. Can a user login after a confirmation code has been sent to him/her, and before he/she resets the password?
> Yes. He/She can login with his/her original password.
3. Can a user ask for another confirmation code, if the previous one is unused?
> Yes. And the last confirmation code should become invalid.
4. If a user asks for another confirmation code, can the previous one be used even though it is never used before?
> No. Only the latest confirmation code is valid.
5. Can a user exit this page after he/she modified the user name text box?
> Yes. He/She can directly press the back button, and he/she is able to return to the Welcome page.
6. Can a user exit this page after he/she applied for a confirmation code?
> Yes. However, when he/she presses the back button, a warning dialog should pop out.
7. Can a user modify the user name after he/she applied for a confirmation code?
> No. The user name text box will be disabled.
8. Can a user exit this page after he/she reset the password?
> Yes. When the server returns a successful message, the app will stay on this page for 2 seconds, and automatically return to the Welcome Page.
9. Can a user press the submit button after he/she reset the password?
> No. The submit button will be disabled right after he/she pressed it.
10. A user has applied a confirmation code, and then he/she exits the app. What if he/she re-enter the app, and go to Forgot Password page?
> He/She should re-apply for a new confirmation code, and the old one should become invalid.
11. What if the network connection fails after a user pressed the submit button?
> The app will detect the connection timeout, and display an error message.
12. When a user asks for a confirmation code, what if he/she enters an invalid user name?
> The app will display an error message, and ask the user re-enter a correct user name.
13. When a user resets the password, what if the length of the password is not correct (a correct length of password should be no less than 6 characters and no more than 20 characters), or the confirmed password is not consistent with the password?
> The app will return an error message, and ask the user to re-enter the password. In fact, the app will check the correctness of the format of the password before submitting it to the server.
14. When a user resets the password, what if the confirmation code is invalid?
> The server will return an error code to the app, and the app will display an error message.
15. What if the user shakes the devices when he/she is in this page, if the SHAKE function is on?
> Automatically go to the SHAKE page.

#### <u>Search Page</u> ####
16. What if nothing is entered or chosen when pressing the submit button?
> The app will return all the dishes.
17. What if the network connection fails when pressing the submit button?
> The app will detect the connection timeout, and display an error message.
18. What if no dishes satisfy the search criteria?
> A text with ‘no result’ will show up.
19. Can a user exit this page after he/she modified anything?
> Yes.
20. When a user re-enters this page, what will the page display? (e.g. the default values, or the values that the user searched last time)
> Yes. The value that the user searched last time.
21. What if the user shakes the devices when he/she is in this page, if the SHAKE function is on?
> Automatically go to the SHAKE page.
22. What if the user checks “Spicy” only in the taste preference?
> All returned dishes must be spicy.
23. What if the user checks “Spicy” and “Meat” only in the taste preference?
> All returned dishes must be either spicy or meat.
24. What if the user checks everything in the taste preference?
> Dishes satisfy all the requirements will be returned.
25. What if the user checks nothing in the taste preference?
> All dishes can be returned.
26. What if the user enters nothing in the dish name field?
> It means that the user doesn’t specify the dishes name. The returned dishes will be determined by other constraints.
27. When the user enters “a b” in the dish name field, will the dish with “a c b” as substring be returned?
> Yes. Dishes with “a” and “b” as substrings will be returned.
28. When the user enters “a” in the dish name field, will the dish with “ac” as substring be returned?
> Yes.
29. When the user enters “a b” in the dish name field, will the dish with “ab” as substring be returned?
> No. "a b" is treated as a sub-string, and "ab" does not contain "a b".
30. Is there any difference between “a” and “a a” in the name field?
> Yes. "a a" is treated as a string with a space character between two 'a's, and therefore "a a" is different from a single "a"

## Results ##
Bugs found:
  * Test case 27 did not pass.
    * <u><b>Reasons</b></u>: Error in server's PHP file. The server treats "a b" as a whole string so that only dish with "a b" as a substring will be returned.
    * <u><b>Status</b></u>: Solved.