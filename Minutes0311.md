# Minutes of 11th Mar #
  * Test the interaction between Android devices and the server.
    * Note: The work on the server side was much tougher than what we had expected, so we did not undertake the testing.
  * Task assignment due on this Wednesday (14th Mar).
  * Discuss the domain and use-case model.

---


# Review of Work #
  1. Login
    * Client side: done (Zheng)
    * Server side: the communication between the server and Android devises (emulator) has bugs (Ma)
  1. Main page
    * The transition between the main page and the detailed dish page: done (Lu)
  1. Registration
    * Email authentication via PHP page: code done, but not tested on the server (Tu)

# Task Assignment #
  1. Zheng: the UI of registration page; refine the UI of detailed dish pages.
  1. Lu: start programming SHAKE function.
  1. Ma: debug the communication between the server and Android devises (emulator).
  1. Tu: uploading pictures issues; test PHP auto-reply on the server.

# Domain and User-Case Model #
This task is due on 16th Mar (next Friday).

## Domain Model ##
  * Classes (attributes following the class name)
    * User: userName, password, nickName, history (multi-valued), blacklist (multi-valued)
    * PersonalTaste (composition with User): doesPreferSpicy, doesPreferMeat, isVege
    * Dish: name, photos, overallRating (multi-valued)
      * Note: The overallRating is a 5-element array. The first element stores the number of users that rate 1 star for this dish; The second one stores the number of users that rate 2 stars; so on so forth.
    * Canteen: dish (multi-valued)
    * Comment: content

  * Associations
    * User Modify PersonalTaste
    * User Rate Dish
      * Rate (association class): numOfStars
    * User CommentOn Dish
    * User UploadPhotosTo Dish
    * Canteen Classify Dish
    * User Write Comment

## Use-Case Model ##
  * Actor: User
  * Use-case: Register, Login, Search, SHAKE, Comment, Rate, UploadPhotos, ModifyPersonalTaste

# Next Meeting #
  * Scheduled on 7pm 14th Mar (Wednesday)
  * Venue: outside LT-J near Lift 27-28
  * Agenda
    * Review the work that has been done.
    * Assign tasks.
    * Finish the domain and use-case model together.