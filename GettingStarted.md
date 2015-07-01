# Getting Started #

This Android project is implemented using Eclipse, with Android SDK plug-in. Also, we use Subversion (SVN) as the repository. This Wiki article explains how to install Eclipse and configure it.


## Downloading Eclipse ##

Eclipse can be downloaded from http://www.eclipse.org/downloads/. "Eclipse Classic" version is recommended. Otherwise, "Eclipse IDE for Java Developers" version is recommended.

## Downloading Android SDK ##
Android SDK can be downloaded from http://developer.android.com/sdk/index.html.
  * For Windows users, Android SDK can be installed directly by the installer.
  * For Mac OS X and Linux users, please go to "tool" directory, and run "android" binary file to open the SDK Manager. Then install the newest version of Android platform under the instructions.

## Installing ADT Plugin for Eclipse ##
  1. Start Eclipse, then select Help > Install New Software....
  1. Click Add, in the top-right corner.
  1. In the Add Repository dialog that appears, enter "ADT Plugin" for the Name and the following URL for the Location: https://dl-ssl.google.com/android/eclipse/
  1. Click OK.
    * Note: If you have trouble acquiring the plugin, try using "http" in the Location URL, instead of "https" (https is preferred for security reasons).
  1. In the Available Software dialog, select the checkbox next to Developer Tools and click Next.
  1. In the next window, you'll see a list of the tools to be downloaded. Click Next.
  1. Read and accept the license agreements, then click Finish.
    * Note: If you get a security warning saying that the authenticity or validity of the software can't be established, click OK.
  1. When the installation completes, restart Eclipse.
  1. A window will pop out automatically when you restart Eclipse. Choose "Using Existing SDK Location", and browse the directory of Android SDK which you downloaded just now.
For more information, please refer to http://developer.android.com/sdk/eclipse-adt.html.

## Installing SVN Plugin for Eclipse ##
Similar steps with installing ADT plugin. Just one different stuff: enter "Subclipse" for the Name and "http://subclipse.tigris.org/update_1.0.x" for the Location after seleting Help > Install New Software....

## Checkout Project from SVN ##
  1. In Eclipse, select File > Import....
  1. Select Other > Checkout Projects from SVN. Click Next.
  1. Select "Create a new repository location". Click Next.
  1. For the URL, enter https://comp3111h-2012-spring.googlecode.com/svn/
    * Note: For non-members, please use "http" instead of "https", since "https" asks for password to commit changes, while "http" checks out read-only version of the project.
  1. Select all folders. Click Finish.

## Updating Project from SVN ##
In Eclipse, choose the directories or files you want to update. Right-click and select Team > Update.

## Committing Changes to the Repository ##
This part is for project members only.
  1. In Eclipse, choose the directories or files you want to commit. Right-click and select Team > Commit.
  1. Eclipse displays the Commit dialog, which summarizes your changes. Click OK.
  1. A dialog will pop out asking for username and password. Username is your Google Code account. Password can be found at https://code.google.com/hosting/settings.

## Deleting Files or Folders from SVN ##
  1. In Eclipse, select Window > Show View > Other....
  1. Select SVN > SVN Repository, and click OK.
  1. There should be a new window called "SVN Repository" in your Eclipse.
  1. Open the target repository, and all files and folders will be listed.
  1. Select the file or folder you want to delete. Right click on it, select Delete....

## More About Android SDK ##
You are highly recommended to read [this page](http://developer.android.com/resources/tutorials/hello-world.html) ("Hello, Android!") to begin your development using Android SDK. This small testing code is under tmp directory. You should select Run > Run to test if your Android ADT works.