# Welcome to ezMeal - Project Proposal #
  * [Introduction](ProjectProposal#Introduction.md)
  * [Project Description](ProjectProposal#Project_Description.md)
    * [Login](ProjectProposal#Login.md)
    * [Searching](ProjectProposal#Searching.md)
    * [Classification](ProjectProposal#Classification.md)
    * [Rankings](ProjectProposal#Rankings.md)
    * [Personal Taste](ProjectProposal#Personal_Taste.md)
    * [SHAKE](ProjectProposal#SHAKE.md)
    * [News (Optional)](ProjectProposal#News_(Optional).md)
    * [Rice Friend (Optional)](ProjectProposal#Rice_Friend_(Optional).md)
  * [User Interface](ProjectProposal#User_Interface.md)
  * [Database](ProjectProposal#Database.md)
  * [Project Management Plan](ProjectProposal#Project_Management_Plan.md)
  * [Group Members & Roles](ProjectProposal#Group_Members_&_Roles.md)
  * [Development Tools](ProjectProposal#Development_Tools.md)
  * [Development Schedule](ProjectProposal#Development_Schedule.md)


---

# Introduction #
“What to Eat in UST?”

This is an eternal question for all students, faculties and staff on our campus. It is common that people stand around the recipe board at LG7 for over 5 minutes without a clue. To help answer it, an Android application **ezMeal** (pronounced “easy meal”) is developed.

**ezMeal** is a dinning guide designed specifically for people studying or working in HKUST. It offers recipes of restaurants on campus, classifies food according to their tastes and prices, generates a popularity ranking, and enables users to choose what to eat based on the dishes review written by other UST students and faculties.

Besides all the functionality mentioned above, a brand new feature “SHAKE” is introduced for those who have a hard time making decision on food. When the user “SHAKE”s the cell phone, **ezMeal** automatically recommends a dish for you based on the popularity of all dishes, the price, dietary restrictions, and even your personal taste. Through this intelligent food recommendation engine, users can also “like” or “dislike” a dish. This would change the rankings of all the food, and helps the engine to make a more personalized recommendation for the user next time.


---

# Project Description #
## Login ##
As this is an interactive application, people share their thoughts and writing reviews inside the app. So an account is needed to enable the customization and prevent irresponsible words. A guest can only explore the menu but not ranking and writing reviews. Students are required to use their ITSC as account name. An activation email would be automatically sent to sqmail containing the URL to activate the account.

## Searching ##
Want to drink a cup of hazelnut latte and don’t know where to find? Don’t worry. With the in-app searching function, you can simply tell your Android phone your wishes, and it will return the canteen you are looking for immediately! On top of that, you can even search your desired food and drink according to its price and your personal taste.

## Classification ##
Classification makes your choice of eating easier and faster. You can simply look up your desired dishes in terms of your personal taste, or either by the classification of canteens. For example, if you want to have spaghetti bolognaise for lunch, you can find it in the classification of pasta. You may also find out the detailed information, such as which canteen serves this dish.

## Rankings ##
Global ranking gives us an overview of what others think. The program calculates the score of the dishes according to the users’ review and rating, and displays it in the decreasing order. The ranks maybe further restricted to particular taste or in a particular canteen or during a particular time period.

## Personal Taste ##
After registration, a profile will be created for the user. Different from the profile of normal social networks, our profile will focus on food. We will ask questions, such as how you like hot food or whether you are a vegetarian. There is a history part to record your meal in the last seven days. What’s more, users can add some food they dislike to a blacklist.

## SHAKE ##
If you have no idea what to eat, SHAKE can make the choice for you! What you need to do is just specifying which canteen you are in and shaking the phone. After the accelerometer receives the signal, the system will load the time and decide whether it is  breakfast, lunch, tea or dinner. Then the system will get the menu. After filtering some food according to users’ profiles, we pick up a dish randomly for you.

## News (Optional) ##
News is a real time system that shows newly written comments and uploaded photos of UST cuisine by other users. It allows users to interact by replying to each other, or to leave a comment on the uploaded photo at the earliest time. Apart from this, News also shows the choices of other online users, which might be a reference for the user to make decisions on their own food.

## Rice Friend (Optional) ##
Rice Friend is another real time system that enables users to invite other users to eat together. Users can invite their friends to eat at the same restaurant, or to pick the same dishes. Of course, the person being invited has the freedom to “reject” the invitation.


---

# User Interface #
We are contributing to implement a very user friendly interface. In the center of the homepage, there are buttons for main modules and functions, i.e. searching, rankings, personal taste, SHAKE, etc. On the top, a log in button is shown. A line of rolling text is located on the bottom of the screen, showing the news and share from other users. By shaking your device, a random dish and its related information will pop out. For pages of each dish, you can easily find out the canteen that serves this dish, its ranking according to different classifications, its photos uploaded by users, as well as comments. You can also upload your own photos and comment on it.

# Database #
We would use actually two databases in this program, one in the user’s device and the other in the server end. The menu, including name, price, picture, location and the style of cooking are stored in the local database in order to support quick access, offline searching and SHAKE. As to the server end, we use the database to store users’ review and ranking to make this application interactive. Moreover, we are going to add the feature to allow user to “add favorite” so the SHAKE can give more satisfying suggestion. The habits of the users are associated with account and stored in the server database, such that changing a login device would not stop the user to customize his account.


---

# Project Management Plan #
We are going to use PHASED software development process but together with some concepts from SCRUM.
We divided the whole application into several modules running simutaneously, such as GUI, Database and Server, and assigned the tasks to specific people, possibly one or two according to the complexity, so we can fulfill the whole system incrementally and iteratively. We also considered the communication interface between the module, so the modularity of the system can be achieved and the complexity is reduced. The functionality of the whole application will be built in parallel according to the modularizing and become richer and richer by adding features to the program one by one.
Within each module, we classify the features we want to achieve into different priority level. Each time we peak the most important one from the product backlog subject to the team capacity and finish the design, coding, testing in one sprint. During the sprint the people in charge of the task would communication regularly to improve the efficiency and correctness, as well as update the sprint backlog and burndown chart  daily by estimating the remaining work. Moreover, between the parallel running modules people meet every other day to report the progress and review the difficulties they met together with the solution.


# Group Members & Roles #
| **Developer** | **ITSC** | **Department** | **Year** | **Roles** |
|:--------------|:---------|:---------------|:---------|:----------|
| MA, Fangchang | fma      | CPEG           | 2        | Server administrator & Database developer |
| LU, Xietong   | xlu      | CPEG           | 2        | UI developer |
| TU, Jia       | jtu      | CPEG           | 2        | Database developer |
| ZHENG, Zichen | zzheng   | CPEG           | 2        | UI developer |

You can also check http://code.google.com/p/comp3111h-2012-spring/people/list.

# Development Tools #
  * Eclipse
  * Android SDK
  * ADT Plug-in for Eclipse
  * XAMPP, which includes:
    * Apache  HTTP Server
    * MySQL
    * PHP
    * Perl
    * [DroidDraw](http://www.droiddraw.org/), a user interface designer

# Development Schedule #
| **Deadline** | **Week** | **Task** |
|:-------------|:---------|:---------|
| 19 Feb       | 4        | Finish the homepage UI design |
| 26 Feb       | 5        | Finish the modification of the homepage UI |
| 26 Feb       | 5        | Build up the server |
| 26 Feb       | 5        | Prepare the database to store canteen and dish information |
| 5 Mar        | 6        | Presentation |
| 5 Mar        | 6        | Finish the first version of UI prototype |
| 5 Mar        | 6        | Prepare the database to store user information |
| 12 Mar       | 7        | Finish the second version of UI prototype |
| 12 Mar       | 7        | Realize Searching, Classification and SHAKE function |
| 12 Mar       | 7        | Pass the offline testing |
| 19 Mar       | 8        | Finish the third version of UI prototype |
| 19 Mar       | 8        | Realize Login function |
| 19 Mar       | 8        | Pass the first online testing |
| 26 Mar       | 9        | Prototype demonstration |
| 2 Apr        | 10       | Inner testing 1, focusing on Searching, Classification and Login function |
| 2 Apr        | 10       | Realize Rankings and Personal taste function |
| 9 Apr        | Break    | Release of the first alpha version (1.0a1) |
| 16 Apr       | 11       | Inner testing 2, on all functions |
| 16 Apr       | 11       | Realize optional functions (news and Rice Friend) |
| 16 Apr       | 11       | Release of the second alpha version (1.0a2) |
| 22 Apr       | 12       | Release of the first beta version (1.0b1) |
| 23 Apr       | 12       | Peer acceptance testing 1 |
| 29 Apr       | 13       | Release of the second beta version (1.0b2) |
| 30 Apr       | 13       | Peer acceptance testing 2 |
| 4 May        | 14       | Release of the stable version (1.0) |

You can also [track our progress](ProgressTracking.md).