# AVFaceDetection using MVVM & DataBinding & FaceDetector
**Amazing Face Detection app for Android**

*Some of the techniques:*
* **MVVM**
* **RecyclerView & glide** - For smoother scrolling while the images loading
* **Permissions mechanism** - (READ/WRITE external storage) supports Android O
* **Local Notifications** - supports Android O

*Improvement ideas:* 
* Change AsyncTaskFaceDetection to IntentService (or maybe RxAndroid ?) so it won't be affected when configuration changes for example (when rotating the screen while downloading images)
* Use **Dagger** - Fully static, compile-time dependency injection framework - for injecting view model and images fetcher service
* I wanted to use online service for random images, like [loremflickr](http://loremflickr.com), save the image on SD and bind Glide to the file path.
but... I didn't manage to make it work (I guess because of the redirect, the stream doesn't contains the image) so currently the images are fixed and taken from [pixabay](https://pixabay.com/)

## 3rd party libraries
* **Glide** - An image loading and caching library for Android focused on smooth scrolling
* **play-services-vision** - for FaceDetector
* **viewmodel** - for ViewModelProvider.Factory

## Screenshots
All images
![All images](https://github.com/gadipaz/AVFaceDetection/blob/master/Screenshot_1.png?raw=true)
Images with face detected
![Images with face detected](https://github.com/gadipaz/AVFaceDetection/blob/master/Screenshot_2.png?raw=true)