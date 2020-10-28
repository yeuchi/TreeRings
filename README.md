# TreeRings
Challenge: count tree rings from a photo. \
Device: Android mobile (no server backend processing)

### Application Workflow
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
<img width="220" src="https://user-images.githubusercontent.com/1282659/95004159-2600cd80-05ad-11eb-88b6-4fb78ca83a3e.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380830-c44be980-00fc-11eb-95bb-651b952458ce.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380835-c746da00-00fc-11eb-9b6d-80f484c82b3f.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380820-b9915480-00fc-11eb-9c36-3a12f57a1fc9.jpg">

### User draw line - identify rings cross-section 
Find the best cross-section to count your tree rings. \
(Try to draw a path that is 'normal' tangent across all rings) 
1. Touch point #1 : center of tree rings. 
2. Touch point #2 : most outer ring position.
3. Touch any point : update/correct #1 or #2 (line)
4. Next : navigate to detail screen. \
<img width="220" src="https://user-images.githubusercontent.com/1282659/95004158-25683700-05ad-11eb-9175-5c1588c8830d.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/95004160-28632780-05ad-11eb-9649-85264a4e21bd.jpg">

### UserLine Intersects Ring Derivative 
Ok, there is some correction to be done in scaling user line coordinates in raster space. \
Black line is a rendering in pixels; black should line up with blue line. \
<img width="220" src="https://user-images.githubusercontent.com/1282659/97117073-6f859980-16cf-11eb-9e71-9adbe0a501ea.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97117074-701e3000-16cf-11eb-881c-27427484c9a9.jpg">


### Highlight Intersection Found 
Current algorithm is simply a 1st derivative to detect the transition of ring color. \
Some noise is removed by accepting only low->high transition (no high->high, high->low). \
<img width="220" src="https://user-images.githubusercontent.com/1282659/97119646-a19ef780-16df-11eb-87f2-0db8832eca3f.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97119647-a5327e80-16df-11eb-8625-cfd78e0a0265.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97119648-a5cb1500-16df-11eb-8a22-2665cc2ba7ca.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97119649-a663ab80-16df-11eb-8c35-eab4f2b0fcf1.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97119650-a663ab80-16df-11eb-9802-0b7faa2cefd1.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97119655-a95e9c00-16df-11eb-8f44-5fe5b61c0ec6.jpg">

## Work in Progress

1. Scale image to fit screen.
2. Higher resolution image.
3. Image Processing, identify rings from pixels.
4. Highlight configuration / colors.
5. About page.
6. Progress bar 

## Configuration Details

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
### Version
<img width="400" alt="Screen Shot 2020-10-25 at 2 56 56 PM" src="https://user-images.githubusercontent.com/1282659/97117537-65b16580-16d2-11eb-824e-f0cec35d49aa.png">


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

