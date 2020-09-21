# TreeRings
Challenge: count tree rings from a photo. \
Device: Android mobile (no server backend processing)

### Workflow
Camera/Gallery -> Photo -> Select Path -> Deriviative -> Count -> Render Path-normals overlay

#### Navigation Graph
<img width="508" alt="Screen Shot 2020-09-20 at 7 51 47 PM" src="https://user-images.githubusercontent.com/1282659/93726432-95bd9400-fb7b-11ea-9a39-c4d7127aa634.png">

### Camera/Gallery - Select photo
Select and preview your tree ring image. 
1. Camera : take a photo.
2. Gallery : pick a photo from gallery.
3. Next : navigate to next screen. \
<img width="220" src="https://user-images.githubusercontent.com/1282659/93717903-460ca780-fb3e-11ea-89b3-40602b661752.jpg"> <img width="220" src="https://user-images.githubusercontent.com/1282659/93717899-43aa4d80-fb3e-11ea-801d-b2d7cfbe4285.jpg">

### Cross-section - User draw line
Find the best cross-section to count your tree rings. \
(Try to draw a path that is 'normal' tangent across all rings) 
1. Touch point #1 : center of tree rings. 
2. Touch point #2 : most outer ring position.

### Calculate Result - count rings Xing
Perform image process to find/count rings crossing from user-drawn-line. \
Render highlight on each ring crossing for inspection. \
Render count result
1. Adjust sensitivity.

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

5. Custom View in Android with Kotlin by Antonio Leiva  
https://antonioleiva.com/custom-views-android-kotlin/
