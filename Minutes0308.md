# Minutes of 8th Mar #
  * Discuss the division of labour of the coming week.
  * Discuss the interaction issues between the clients (Android devices) and the server.

---


# Task Assignment #
  1. UI welcome page
  1. Login
    * Server generate table of user information.
    * Database API (realized by PHP on the server side)
      * User send request (name & password), server respond
      * Wrong password generate alart
      * Each request has individual PHP page
    * Registration for new users
      * Check if the user has already existed in the database.
      * Use ITSC account; send authentication emails to the new users' ITSC mailbox, so that they can activate their ezMeal account.
      * Add initialized information of new users.
  1. Main page
    * Fetch detail of each dish from server.
      * Fetch pictures stored in the server via FTP; picture name and its directory is stored in the database.
      * Create dish table (set default as LG1 canteen).
      * Get all dish given some constrains.
    * Design icons for spicy food and vegetarian. (Optional)
  1. Detail page
    * Pass the info from main page activity to detail page (30min)
    * Get detail from server
      * Protocol: info format
      * Could return XML file
    * (Gallery, upload picture) one default picture first
    * Rating
  1. SHAKE
    * UI
    * How shake
    * How to generate random dish
  1. Personal taste
    * Optional

# Other Issues #
The extra meeting is scheduled on Sunday 2pm.
  * Test the interaction between Android devices and the server.
  * Discuss the domain and use-case model