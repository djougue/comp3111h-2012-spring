# System Requirements #
  * [Customer Registration](SystemRequirements#Customer_Registration.md)
  * [Browsing and Searching of Items](SystemRequirements#Browsing_and_Searching_of_Items.md)
  * [Comments and Picture Uploading System](SystemRequirements#Comments_and_Picture_Uploading_System.md)
  * [Rating](SystemRequirements#Rating.md)
  * [Personal Taste](SystemRequirements#Personal_Taste.md)
  * [SHAKING](SystemRequirements#SHAKING.md)
  * [System Management](SystemRequirements#System_Management.md)

---


# Customer Registration #
The user could register an account to store the personal data, such as favors, etc. Registration involves entering the following personal information: username, password and nickname.
  * The student ITSC is used as username and also serves as email address. Upon registration, a confirm email with a link would be sent to the ITSC email address automatically. The account will be activated only after clicking the link.
  * Nickname is what the user wants to be called and what appears in the comments window of the dishes. The nickname should be between 6 and 10 characters and contain only letters and numbers. The check should be done during the registration process.
  * The customer should create a password with English letters and numbers during registration, which should be at least 8 characters, at most 15 characters and contain at least one number and one letter.
After registration, the users should be able to change any of the above information except for their username since this is how the system identifies a user. Login is done by using the matched account (aka ITSC) and password. In case that the user forget the password, an email for resetting the password would be sent to the ITSC email address automatically upon “forget password” request. The user receives a code with 4 numbers by email. Using the code, the user can reset the password.

# Browsing and Searching of Items #
Each item should have its own web page that shows the item’s name, picture, favor, canteen, price, available time, rating and comments.
  * The name may be identical for several dishes but belong to different canteen.
  * Favor is the style of the dish, such as spicy, vegetarian and so on.
  * Available time is the period during which the dish is available. For example, some dishes are only offered as breakfast while some are exclusive only for dinner.
  * Rating is the average of all rating of the dish.
  * Comments come from the uses who have taken the dishes before. The nicknames are displayed for comments.
  * There should be a button for user to upload the pictures.
The user is able to browse and filter the dishes. For easier browsing, the dishes are grouped into categories. The available categories are canteen and available time.
In addition to browsing, it should be possible to filter the dishes based on some criteria. The allowed filters include dish name, favor, canteen and available time. By adding filters, the users could restrict the result to meet specific requirements. If more than one criterion is specified, then the filtering result should match all specified restrictions.
The results of browsing or filtering should be displayed in a tabular format showing the dish thumbnail, if any, name, price, canteen and available time. From this display of dishes it should be possible to go directly to a dish’s web page.

# Comments and Picture Uploading System #
On the page of each dish, a picture of the dish and users’ comments to it are shown. When ezMeal displays the menu, dish icons will be shown to the left of the title of the dish. Users are able to comment on dishes and upload related pictures, by pressing the corresponding buttons at the bottom of each dish page. This requires users to register on ezMeal, using their ITSC account and creating a password. The followings are some specification.

  * Comments
    * English comments are supported.
    * The maximum length of a comment is 140 words.
  * Picture uploading
    * If the picture for a dish is not available,which means no one uploads images for the dish before, the user can upload a image for it.
    * JPEG format (.jpg or .jpeg),Bitmap format (.bmp) and PNG format are supported.
    * The dimension of a picture is limited to 144x144. Any images that cannot fulfill the dimension requirement will be cropped and scaled automatically.
    * The size of a picture is not limited.
    * If a dish has no uploaded pictures, a default picture will be used.

# Rating #
Users are welcome to rate on dishes. Each dish also provides the users’ latest overall rating on it. This requires customers to register on ezMeal, using their ITSC account and creating a password. The followings are some specifications.

  * For each user, the unit of rating is star. The maximum is 5 stars (“I couldn’t like it anymore!”), while the minimum is 0 stars (“I really dislike it!”). When a user gives 0 stars to a dish, the dish will be added into the user’s black list.
  * For the overall rating score, the unit is 0.1 points. The maximum is 10 points, the minimum is 0 points.
  * The overall rating is calculated from users’ rating. The number of users who have commented on a specific dish is _n_; the number of stars given by these users is _m_. Since each star is worth 2 points, the overall rating score should be 2 _m_ / _n_ . And the server will automatically correct the final result to one decimal place. The overall rating will displays in the dish page in both star way and numeral way. The decimal part of the total rating is discarded for the star way and each point is worth half star.
  * The overall rating can be updated immediately after a user rates a dish.
  * A user is able to modify his/her rating at any time. However, the overall rating should not double-count the user’s previous rating and his/her new rating. In other words, only his/her new rating contributes to the overall rating.

# Personal Taste #
Each registered customer has a personal profile to record his/her taste preference. A person’s taste preference should have its own page view that shows favor and black list. The favor should include vegetable dish, meat dish and spicy dish. A no preference option should be offered between the vegetable dish and meat dish and it is the default option.  Customer can modify their black list, which contains the dishes they dislike and would never be chosen. Basic information of the dishes should be shown in the list. When customers click the dishes in the list, they can browse its details and cancels the dislike option in the page.

# SHAKING #
It is a function offered to all the customers including the unregistered one. To use this function, customers should shake their phones three times and an individual page appears. After specifying a canteen, a dish available in current time slot would be chosen randomly from the menu and its name and picture appear in the page. The result should be related to user's taste preference.
  * If customers have vegetable preference, dishes contain meat would not be in the candidates.
  * If customers choose meat dishes preference, the dishes with much meat should have more possibility to be chosen.
  * If customers prefer a spicy dish, the spicy dishes will have higher possibility.
  * All the dishes in the black list will be filtered.
If customers want another choice, they click the re-pick button to do so.

# System Management #
It is vital for administrators of ezMeal to maintain the information in the database and to obtain reports generated automatically by the system on a daily basis. Therefore, a website with access only by the administrators should be established so that they can remotely log on to the system, without affecting the service provided to users.
The website requires user-id and password to log on. Administrators who log on to the website are subject to:
  * Change password of the administrator account. It is required by the system to enter the correct password before any modifications to it. Besides, the new password has to be repeated twice to prevent any typing errors.
  * Have access to all information inside the database. More specifically, the administrator is allowed to view all the information related to users, dishes, comments and so on through this website.
  * Add or delete entries of dishes, as well as create or delete an account in the table of ezMeal users.

Beyond that, a simple and straight-forward user-interface, including list of selected outputs, and buttons for editing, adding or deleting entries will be constructed. The list should be capable of producing the following reports:
  * A list of dish details. It can be identified by the dish id, dish name, or the combinations both. The list should include the dish name, price, canteen and its total rating
  * A list of user details. Similar to the dish list, we can search for a particular user with its user ITSC or user id, and the report should contain the user name, user password, and user tastes.