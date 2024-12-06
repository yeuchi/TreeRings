# TreeRings (dendroecology)
Challenge: count tree rings from a photo. \
Device: Android mobile (no server backend processing) \
Goog Play Store: https://play.google.com/store/apps/details?id=com.ctyeung.treerings

### Application Workflow 
Camera/Gallery -> Photo -> Sobel Edge Detection -> User Selects Path -> Count Intersections -> Render 

#### Navigation Graph
<img width="518" alt="Screen Shot 2020-10-29 at 3 49 02 AM" src="https://user-images.githubusercontent.com/1282659/97545973-c6be8f00-1999-11eb-84e0-962fe3d1fbec.png">


### Camera/Gallery -> Photo
Select and preview your tree ring image. 
1. Camera : take a photo.
2. Gallery : pick a photo from gallery.
3. Next : navigate to next screen. \
<img width="300" alt="starter" src="https://user-images.githubusercontent.com/1282659/93726809-a5d67300-fb7d-11ea-842c-7065f897f563.png"> <img width="220" src="https://user-images.githubusercontent.com/1282659/93726815-abcc5400-fb7d-11ea-8415-56c07d7b719d.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/93726816-af5fdb00-fb7d-11ea-9c59-629c2936bcf7.jpg"> 


### Edge Detection (Sobel Operators)
User should select the best threshold value from slider for best edge detection. \
Default value = 30 in range 0 - 255. 

The image process steps are convolution -> threshold -> merge (source + highlighted edges). \
Convolution with 1st derivative sobel operators on x, y, 45 degree axis. \
Kernel is 3x3 where x-sobel values = 0, 1, -1 \
<img width="220" src="https://user-images.githubusercontent.com/1282659/95004159-2600cd80-05ad-11eb-88b6-4fb78ca83a3e.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380830-c44be980-00fc-11eb-95bb-651b952458ce.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380835-c746da00-00fc-11eb-9b6d-80f484c82b3f.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/94380820-b9915480-00fc-11eb-9c36-3a12f57a1fc9.jpg">


### User Selects Path
Find the best cross-section to count your tree rings. \
(Try to draw a path that is 'normal' tangent across all rings) 
1. Touch point #1 : center of tree rings. 
2. Touch point #2 : most outer ring position.
3. Touch any point : update/correct #1 or #2 (line)
4. Next : navigate to detail screen. \
<img width="220" src="https://user-images.githubusercontent.com/1282659/95004158-25683700-05ad-11eb-9175-5c1588c8830d.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/95004160-28632780-05ad-11eb-9649-85264a4e21bd.jpg">


### UserLine Intersects Rings (Derivative pixels) 
Ok, there is some correction to be done in scaling user line coordinates in raster space. \
Black line is a rendering in pixels; black should line up with blue line. \
<img width="220" src="https://user-images.githubusercontent.com/1282659/97117073-6f859980-16cf-11eb-9e71-9adbe0a501ea.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/97117074-701e3000-16cf-11eb-881c-27427484c9a9.jpg">


### Count Intersections
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

### Android Studio
Android Studio Ladybug | 2024.2.1 Patch 2

### Test devices
Note: Since there is C++ code, it is device and platform specific !
May not run on your device(s).

I tested it on the following.
- Google Pixel 6a
- Samsung S9

### Version 2.0 on Google Play

1. Main Page: add background image and instruction.
2. Count Page: add white background to count value - legibility.

<img height="400" src="https://user-images.githubusercontent.com/1282659/129494098-06dee2e7-9d13-4672-bdb2-23785ff4a65e.jpg"> <img height="400" src="https://user-images.githubusercontent.com/1282659/129494101-a4cd0f4c-0f4c-43f4-a1d7-a248b031215e.jpg">

### Version 3.0 on Google Play

1. Draw Line Page: draw normal lines in tangent to user's specified line (previous horizontal).

<img height="400" src="https://user-images.githubusercontent.com/1282659/130367900-24cacb3b-9724-420e-9e8b-9db41a9d84c4.jpg"> <img height="400" src="https://user-images.githubusercontent.com/1282659/130367983-b9a8753a-979e-4841-94d3-7a73b962080d.jpg">

### Version 4.0 on Google Play 

1. Portrait mode only - so image does not get lost.

### Version 9.9
Update to Android Studio Ladybug | 2024.2.1 Patch 2

## Slope intercept: <sup>[7]</sup> \
<img width="120" src="https://user-images.githubusercontent.com/1282659/130368524-358a666b-2782-4080-b221-223625f5c7f8.png">

<img width="170" src="https://user-images.githubusercontent.com/1282659/130368528-260f5c0e-e274-4c52-a16a-91fe9b2ce63d.png">

Substitude into distance equation: <sup>[7]</sup> \
<img width="300" src="https://user-images.githubusercontent.com/1282659/130368530-45e75e89-fad9-4c7d-8595-f8cf5caf36c7.png">

## Sample Images (dev/test)
A collection of photos are available in sub-directory /photos. \
Many thanks to Earl 'Bud' Reaves, Maryland County Forester, for photos: bud1-4. \
All others are 'borrowed' on the world-wide-web. 

Only low resolution version of 'borrowed' images have been used thus far. \
Look forward to expanding as better algorithm is devised.

bud1-4 \
<img width="100" src="https://user-images.githubusercontent.com/1282659/97547607-ff5f6800-199b-11eb-89f4-2e12ad584115.jpg"> <img width="100" src="https://user-images.githubusercontent.com/1282659/97547608-fff7fe80-199b-11eb-920b-516e1e313ecf.jpg"> <img width="100" src="https://user-images.githubusercontent.com/1282659/97547610-fff7fe80-199b-11eb-9ded-f77efcb87ec1.jpg"> <img width="100" src="https://user-images.githubusercontent.com/1282659/97547611-fff7fe80-199b-11eb-979b-511acc8ec716.jpg"> 

borrowed \
<img width="100" src="https://user-images.githubusercontent.com/1282659/97547613-00909500-199c-11eb-9437-542b7be9d985.jpg"> <img width="100" src="https://user-images.githubusercontent.com/1282659/97547614-01292b80-199c-11eb-801d-3ce4f818cb3c.jpg"> <img width="100" src="https://user-images.githubusercontent.com/1282659/97547616-01292b80-199c-11eb-97cf-fa751f6321f7.jpg">


# Inspiration


### Earl 'Bud' Reaves 
After seeing many of his Facebook posts, I was intrigued by his deligence, tracking tree age for studies. \
The prospect of automating the process might be helpful ? \
Even a quick estimate on the field might be useful ?


### Stuart Dahlberg 
Stu was a mentor and colleague in my early career, 1994. \
Stu once showed me a pine tree cross section while pointing out the rings and said,

"However slow or fast, every living thing must grow. \
 The moment growth ceases, it is dead."
 
Stu was 52 when he and spouse perished on a plane crash, 2012. \
https://www.legacy.com/obituaries/sctimes/obituary.aspx?n=stuart-and-ivelisse-dahlberg&pid=156721789&fhid=12808

 
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

6. Sample tree cross section images (bud1-4), by Earl Bud Reaves, County Forester, Anne Arundel County, MD

7. Mathematics: "How do I find a point a given distance from another point along a line?" by John Douma, June 2, 2013 \
https://math.stackexchange.com/questions/409689/how-do-i-find-a-point-a-given-distance-from-another-point-along-a-line/409721

