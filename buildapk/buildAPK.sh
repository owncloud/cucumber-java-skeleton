#!/bin/bash

# Required variables:

## $PROJECT_ROOT_PATH : root folder of the Android project
## APK_PATH: path where the apk will be stored after being built 
##           typically owncloudApp/build/outputs/apk/original/release
## TARGET_PATH: path to copy the apk after being built
## BUILD_TOOLS: path to the existing build_tools
## KEYSTORE_PATH: path to the keystore to sign
## KEYSTORE_PASS: pass for the keystore (string)

# Move to the correct place
cd $PROJECT_ROOT_PATH

# Disable welcome wizard and release notes
gsed -i 's/<bool name="wizard_enabled">true<\/bool>/<bool name="wizard_enabled">false<\/bool>/i' owncloudApp/src/main/res/values/setup.xml
gsed -i 's/<bool name="release_notes_enabled">true<\/bool>/<bool name="release_notes_enabled">false<\/bool>/i' owncloudApp/src/main/res/values/setup.xml
grep wizard owncloudApp/src/main/res/values/setup.xml
grep release_notes owncloudApp/src/main/res/values/setup.xml

# Set basic auth as default
gsed -i '375,376d' owncloudApp/src/main/java/com/owncloud/android/presentation/authentication/LoginActivity.kt
gsed -i '380,414d' owncloudApp/src/main/java/com/owncloud/android/presentation/authentication/LoginActivity.kt

# Build the app in release mode
./gradlew assembleOriginalRelease

# Allign artifact 
ls $APK_PATH | grep owncloud | xargs -I{} $BUILD_TOOLS_PATH/zipalign -v -p 4 $APK_PATH/{} $APK_PATH/owncloudunsigned.apk

# Sign apk with keystore and pass
$BUILD_TOOLS_PATH/apksigner sign --ks $KEYSTORE_PATH --ks-pass pass:$KEYSTORE_PASS --ks-key-alias oc-release --out $APK_PATH/owncloud.apk $APK_PATH/owncloudunsigned.apk

# Move to the tarhet path
cp ./$APK_PATH/owncloud.apk $TARGET_PATH

# Remove the path of generated artifacts
rm -rf $APK_PATH
