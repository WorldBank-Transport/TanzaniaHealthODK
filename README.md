This is an Android app derived from the [ODK Collect](https://opendatakit.org/use/collect/), dedicated for mapping Tanzania's Health Facilities


Build Instructions
-------------------

This project uses the Gradle build system wich is a default build system in [Android Studio](http://developer.android.com/sdk/installing/studio.html).


To create a signed apk for release build you can put


    ODK_RELEASE_STORE_FILE=/path/to/keystore/file
    ODK_RELEASE_STORE_PASSWORD=*********
    ODK_RELEASE_KEY_ALIAS=yourkeyallias
    ODK_RELEASE_KEY_PASSWORD=*********


into `~/.gradle/gradle.properties` file. That will provide signing configurations for the project. 
Then you can run `gradle assembleRelease` OR `gradle build` from the project root via terminal.

