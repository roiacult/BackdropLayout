# BackdropLayout
##### this library is based on Material design guidance (https://material.io/design/components/backdrop.html)

![alt](https://raw.githubusercontent.com/roiacult/BackdropLayout/master/art/mio-design_assets_1PxBRcPZkTzJFN7vGcNrKDoekzzE7FrZY_backdrop-intro.png)
![alt](https://raw.githubusercontent.com/roiacult/BackdropLayout/master/art/backdrop.gif)
### Requirements
- api 16 or higher
- migrating to  androidx

# Setting up

 Add it in your root build.gradle at the end of repositories:
```Gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency in your app build.gradle file
```Gradle
dependencies {
	...
    implementation 'com.github.roiacult:BackdropLayout:VERSION'
}
```
Note : replace VERSION with latest version from jitpack
[![](https://jitpack.io/v/roiacult/BackdropLayout.svg)](https://jitpack.io/#roiacult/BackdropLayout)

# Usage
you need to include front and back layout inside backdropLayout
and specifie front_layout and back_layout attribute or it will throw an exception 
```xml
    <com.roacult.backdrop.BackdropLayout
            android:id="@+id/container"
            app:front_layout="@id/includedFront"
            app:back_layout="@id/includedBack"
            app:toolbarId="@id/toolbar"
            app:peekHeight="56dp"
            app:disable_when_open="true"
            app:menuDrawable="@drawable/ic_menu"
            app:closeDrawable="@drawable/ic_close"
            app:animationDuration="400"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        <include
                android:id="@+id/includedBack"
                layout="@layout/back_layout"/>
        <include
                android:id="@+id/includedFront"
                layout="@layout/front_layout"/>

    </com.roacult.backdrop.BackdropLayout>
```
`app:front_layout` : id of front layout  (required)

`app:back_layout` : id of back layout (required)

`app:toolbarId` : id of toolbar when this attribute is specified BackdropLayout 
will automatically handle click event on toolbar and change state of backdroplayout 
and navigation icon according to the click

`app:peekHeight` : height of front layout header 
if not specified BackdropLayout will swip front layout out of screen when BackdropLayout
is in open state

`app:menuDrawable` : drawable shown when BackdropLayout is in close State

`app:closeDrawable` : drawable shown when BackdropLayout is in open State

`app:animationDuration` : duration of swiping front layout 

`app:disable_when_open` : disable front layout and adding white (Transparent) view above front layout 

```Kotlin
// you can add listener
backdropLayout.setOnBackdropChangeStateListener{
    when(it){	
       BackdropLayout.State.OPEN -> {
            //TODO 
       }
       BackdropLayout.State.CLOSE -> { 
	    //TODO 
       }
    } 	 
}

// you can access front and back layout
backdropLayout.getFrontLayout()
backdropLayout.getBackLayout()
	
```


for more information see simple app included in this repo
(https://github.com/roiacult/BackdropLayout/tree/master/app)

## contact

[![alt text][1.1]][1]

[1.1]: http://i.imgur.com/P3YfQoD.png (facebook icon)
[1]: https://www.facebook.com/roiacult27
