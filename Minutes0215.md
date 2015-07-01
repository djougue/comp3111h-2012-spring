# Minutes of 15th Feb #
  * Discuss graphical user interface.
  * Discuss technical implementation methods.

---


# User Interface #
  * Theme color: orange + white
  * Main page: welcome logo; button(offline, online); login
  * 4 button (setting, shake, personal taste, quit), bottom tab (menu, search, rank)
  * Menu: left-top => time; right-top => canteen; middle (can slide) => picture + name + taste + price.
  * Search: (name, canteen, taste), + clean, search => view move up, show the search results (pull down to return to search view)
  * Auto detect to decide the offline/online mode
  * Main page, on the top: random show a sentence(by time, may have a link)
  * Main page: logo + sign in + offline mode. (logo move up a little to show sign-in window)
  * What local what online? put everything online first, download on demand.
  * Detail of a dish: photo, review, ranking(star+number)
  * Mascot (maybe a pig)
  * Q&A
    * Q: Why local database? A: quicker, lighter for server.
    * Q: Language? A: English (maybe Chinese).
    * Q: Search result order? A: Database default order.

# Database #
## Dish ##
  * Attributes: ID (key), name, canteen, picture, price, time, star, flavor (hot, vegetable, meat)
  * Use ID as the name of folder for picture
  * Store who rank what for which dish (can)

## User Account ##
  * Attributes: ID (maybe ITSC), password, personal taste
  * Personal taste can be recommended by the program.
    * Q: Less and less recommended dishes?
    * A:
      1. Improve algorithm, define a minimum number of "un-preferred" dishes to be recommended.
      1. Manually set what I dislike, what I prefer.
      1. Record the preference of users, for later use.
  * Blacklist
    * When recommending a dish, user can choose "like", "dislike", and "blacklist"
    * User can edit their blacklist in Settings.

## Relationship between Dishes and Users ##
  * Attributes: score, review
  * Reviews should show time, user name. No link needed for user name.