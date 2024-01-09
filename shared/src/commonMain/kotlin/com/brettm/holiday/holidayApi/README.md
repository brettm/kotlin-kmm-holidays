# Holiday API

The Holiday API was generated from the swagger.yml definition at: 
(https://date.nager.at/swagger/v3/swagger.yml) 

Uses the OpenAPI generator CLI to create the model and client code from the swagger spec.
```console
openapi-generator generate -i ~/Desktop/swagger.yml -g kotlin --additional-properties=dateLibrary=kotlinx-dateTime,library=multiplatform,serializationLibrary=kotlinx_serialization -o ~/Desktop/Test/
```

The out-of-the-box definitions needed some manual tweaks to get the client sdk working
- Added new constructors as Obj-C does not support default argument parameters
- Added the Throwable annotations to propagate errors to Swift to handle in try/catch blocks
- Corrected some return values and type casting from the api classes that were causing JSON parse errors

## Summary

- No direct support for Swift yet. Currently Kotlin compiles to ObjC, which means:
-- Default arguments for functions are not propagated (as not supported in ObjC) so defaults must be provided
-- No support for ObservedObject. Users need to manually setup publishers/subscribers to receive updates
-- Sharing view model code is difficult. Maybe sharing view models is not the correct architecture anyway as SwiftUI shouldn't be bound to a MVVM architecture.
- Extension conversion is not yet fully supported. For example extending an array or string to provide additional methods in common main will only be available on android.
  (https://stackoverflow.com/questions/60050795/not-able-to-use-kotlin-extension-function-written-in-common-in-ios-as-swift-exte)