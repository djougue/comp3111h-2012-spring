# Domain and Use-Case Model #
  * [Introduction](Modeling#Introduction.md)
  * [Domain Model](Modeling#Domain_Model.md)
  * [Use-Case Model](Modeling#Use-Case_Model.md)
    * [Actors](Modeling#Actors.md)
      * [User](Modeling#User.md)
      * [Administrator](Modeling#Administrator.md)
    * [Use Case](Modeling#Use_Case.md)
      * [Register](Modeling#Register.md)
      * [Login](Modeling#Login.md)
      * [Search](Modeling#Search.md)
      * [SHAKE](Modeling#SHAKE.md)
      * [Comment](Modeling#Comment.md)
      * [Rate](Modeling#Rate.md)
      * [Upload Photos](Modeling#Upload_Photos.md)
      * [Modify Personal Taste](Modeling#Modify_Personal_Taste.md)
      * [Manage User](Modeling#Manage_User.md)
      * [Manage Dish](Modeling#Manage_Dish.md)
  * [Download](Modeling#Download.md)

---


# Introduction #
This wiki pages contains a class diagram showing the domain model classes, their attributes and the associations or association classes that relate the classes; and a use-case diagram showing the actors and use cases and how they are related.

# Domain Model #
![https://comp3111h-2012-spring.googlecode.com/svn/images/modeling/domain-model.png](https://comp3111h-2012-spring.googlecode.com/svn/images/modeling/domain-model.png)

# Use-Case Model #
![https://comp3111h-2012-spring.googlecode.com/svn/images/modeling/use-case-model.png](https://comp3111h-2012-spring.googlecode.com/svn/images/modeling/use-case-model.png)

## Actors ##
#### User ####
A User is a registered person of ezMeal, who can login to ezMeal to browse all dishes in terms of their personal taste and the ranking of dishes, rate dishes, comment on dishes, upload related photos, and modify personal information. A User is able to use ezMeal on a mobile device with Android.

#### Administrator ####
An Administrator is a person who maintains the information of the dishes and users. An Administrator uses a admin website to add or delete dishes and export users’ information.

## Use Case ##
#### Register ####
  * Participating actors: Initiated by User.
  * Purpose: Enable users' information to be recorded in ezMeal's database
  * Flow of events:
    1. A user submits ITSC account and ezMeal password.
    1. The system checks the correctness of the user information.
    1. The registration information is sent to the server.
    1. An aunthentication/activation email is sent to the user.
    1. The user clicks on the randomly generated link included in the email to activate his/her account.
    1. The use is successfully registered.
  * Nonfunctional requirements:
    1. No space is allowed in the password.
    1. After the submission of the user's information, the user will be put in to the temporary user table in the database.

#### Login ####
  * Participating actors: Initiated by User.
  * Purpose: Enable the user to comment on dishes, upload photos, and browse dishes according to his/her personal taste.
  * Flow of events:
    1. A user enters his/her ezMeal user name (ITSC account) and password.
    1. The system checks the correctness of the format of the user's input.
    1. The login information is sent to the server.
    1. The server send the login status (successful of failed) to the client).
    1. Go to the main page if login is successful.
  * Nonfunctional requirements:
    1. The system should check the network availability when the user clicks the _Login_ button.
    1. The system should stop listening to the response from the server and display error message if timeout (30 seconds).

#### Search ####
  * Participating actors: Initiated by User.
  * Purpose: The use case enables a user to browse dishes with some customized limitations.
  * Flow of events:
    1. The User activates the “search” page in his/her terminal.
    1. The User inputs some conditions for the dishes he/she wants.
    1. The User receives the search result.
  * Nonfunctional requirements:
    1. The allowed filters include dish name, favor, canteen, price, available time and rating.
    1. The results of browsing or filtering should be displayed in a tabular format showing the dish thumbnail, if any, name, price, canteen and available time.
    1. It should be possible to order the dishes by price and group by canteen or available time.
    1. From this display of dishes it should be possible to go directly to a dish’s web page.

#### SHAKE ####
  * Participating actors: Initiated by User.
  * Purpose: When a user want the system to recommend a dish to him/her, SHAKE will choose a dish randomly for the user.
  * Flow of events:
    1. The User activates the “SHAKE” page in his/her terminal.
    1. The User specifies the a canteen.
    1. The User receives the random-picked-up dish in the menu of the canteen.
  * Nonfunctional requirements:
    1. The chosen dish should be available in current time slot.
    1. The name and a picture of the dish should appears in the page.
    1. For registered user, the result should be related to their taste preference.

#### Comment ####
  * Participating actors: Initiated by User.
  * Purpose: User can write comments on the dishes.
  * Flow of events:
    1. The User writes comments in the dialog.
    1. The User submits comments.
    1. The User receives acknowledge upon successful commenting.
  * Nonfunctional requirements:
    1. The comment dialog shall appear at the bottom of the detailed page
    1. User can comments on the same dish for several times
    1. One user can comment on several different dishes.

#### Rate ####
  * Participating actors: Initiated by User.
  * Purpose: This use case allow users to rate a dish and enable searching by rating.
  * Flow of events:
    1. The User rates the dish.
    1. The rate is sent to the system.
    1. The User is acknowledged once rated.
  * Nonfunctional requirements:
    1. Rating is in the form of stars.
    1. User give 1-5 stars for a dish and the system calculates the total average rating
    1. The rating stars should appear above the comments dialog in the detailed page.
    1. The default stars before use rating shall be the average rating.
    1. After rating there shall be a small word indicating that the user has already rated the dish.
    1. User can only rate a dish once but could modify their rating.

#### Upload Photos ####
  * Participating actors: Initiated by User.
  * Purpose: User can upload their photos of dishes.
  * Flow of events:
    1. The User upload the desired photo.
    1. The photo is submitted to the server system.
    1. The User receive acknowledge upon successful upload.
  * Nonfunctional requirements:
    1. The _Upload Photo_ button is in the Detailed page.
    1. User can upload any numbers of photos of any number of dishes.
    1. Uploaded photos should within some certain size.
    1. Uploaded photos will appear in Gallery page.

#### Modify Personal Taste ####
  * Participating actors: Initiated by User.
  * Purpose: Enable users to edit their personal taste after registering and enable searching by tastes as well as SHAKE function.
  * Flow of events:
    1. The User modifies their personal taste.
    1. he request is sent to the system and the information is updated.
    1. The User receive acknowledgement of successful modification.
  * Nonfunctional requirements:
    1. The personal taste setting is shown in _Personal Taste_ Page.
    1. The User should see the same information on different device.
    1. The User can modify their personal tastes infinite number of times.

#### Manage User ####
  * Participating actors: Initiated by Administrator.
  * Purpose: Enable the Administrator to manage the users account.
  * Flow of events:
    1. The Administrator modify the user account.
    1. The request is sent to the system.
    1. The Administrator receive acknowledgement upon user account update.
  * Nonfunctional requirements:
    1. The Administrator has access to user’s account information.
    1. The Administrator should delete the “zombie fan” or fake users.
    1. The User shall not have the access to alter other user account.

#### Manage Dish ####
  * Participating actors: Initiated by Administrator.
  * Purpose: Administrator takes the responsibility to keep the dish database up to date.
  * Flow of events:
    1. The Administrator send update dishes request.
    1. The request is sent to the system.
    1. The Administrator receive acknowledgement once the system is updated.
  * Nonfunctional requirements:
    1. The Administrator adds new dishes or deletes canceled dishes from time to time to keep dish information up to date.
    1. The User shall not have the direct access to manage the dish information.
    1. The Administrator can modify the default dish photo from the gallery in this process.

# Download #
You can also download the original files of the domain model and the use-case model under the "Download" tab of ezMeal's Google Code site.

Quick links to the download page:
  * [Domain model](http://code.google.com/p/comp3111h-2012-spring/downloads/detail?name=domain-model.emx&can=2&q=)
  * [Use-case model](http://code.google.com/p/comp3111h-2012-spring/downloads/detail?name=use-case-model.emx&can=2&q=)