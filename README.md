# TreeRings
Challenge: count tree rings from a photo. \
Device: Android mobile (no server backend processing)

### Workflow
Camera/Gallery -> Photo -> Deriviative -> Select Path -> Count -> Render Path-normals overlay

#### Navigation Graph
<img width="508" alt="Screen Shot 2020-09-20 at 7 51 47 PM" src="https://user-images.githubusercontent.com/1282659/93726432-95bd9400-fb7b-11ea-9a39-c4d7127aa634.png">

### Select photo - use camera or gallery 
Select and preview your tree ring image. 
1. Camera : take a photo.
2. Gallery : pick a photo from gallery.
3. Next : navigate to next screen. \
<img width="300" alt="starter" src="https://user-images.githubusercontent.com/1282659/93726809-a5d67300-fb7d-11ea-842c-7065f897f563.png"> <img width="220" src="https://user-images.githubusercontent.com/1282659/93726815-abcc5400-fb7d-11ea-8415-56c07d7b719d.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/93726816-af5fdb00-fb7d-11ea-9c59-629c2936bcf7.jpg"> 

### Image processing 
User should select the best threshold value from slider for best edge detection. \
Default value = 30 in range 0 - 255. 

The image process steps are convolution -> threshold -> merge (source + highlighted edges). \
Convolution with 1st derivative sobel operators on x, y, 45 degree axis. \
Kernel is 3x3 where x-sobel values = 0, 1, -1 \
<img width="220" src="https://user-images.githubusercontent.com/1282659/94371964-e115fc00-00bf-11eb-832d-b29e2da63642.jpg">

### User draw line - identify rings cross-section 
Find the best cross-section to count your tree rings. \
(Try to draw a path that is 'normal' tangent across all rings) 
1. Touch point #1 : center of tree rings. 
2. Touch point #2 : most outer ring position.
3. Touch any point : update/correct #1 or #2 (line)
4. Next : navigate to detail screen. 
<img width="220" src="https://user-images.githubusercontent.com/1282659/93726820-b25acb80-fb7d-11ea-8e2b-3124bab7d353.jpg">

### Count rings - calculation ring xings on line
Perform image process to find/count rings crossing from user-drawn-line. \
Render highlight on each ring crossing for inspection. \
Render count result
1. Adjust sensitivity.


### JNI Configuration 
Below addition is <bold>CRITICAL</bold> to JNI referencing to C++ classes. \
Please consider the file,  ../src/CMakeList.txt \

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
```diff
+       Convolution.cpp
```
        native-lib.cpp)

target_link_libraries( # Specifies the target library.
```diff
        native-lib
+       -ljnigraphics
```

# References
1. Android10CameraBasics - C.T. Yeung \
https://github.com/yeuchi/Android10CameraBasic

2. NDKExercise - C.T. Yeung \
https://github.com/yeuchi/NDKExercise

3. Get started with the Navigation component \
https://developer.android.com/guide/navigation/navigation-getting-started
Android mobile for counting tree rings.

4. Codepath: Accessing the Camera and Stored Media \
https://guides.codepath.com/android/Accessing-the-Camera-and-Stored-Media

5. Custom View in Android with Kotlin by Antonio Leiva  \
https://antonioleiva.com/custom-views-android-kotlin/

