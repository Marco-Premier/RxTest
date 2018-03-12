My goal was to write code that is easily maintainable, testable and reusable.
I have used a single-activity app pattern + MVP architecture and reactive programming.
In the app there are views that simply transform users interaction into a stream of UiEvents. Views don't know anything about the logic of the app.
Presenters take this stream of events and transform them into Actions.
The model takes this stream of Actions, do whatever operation is needed and then return a stream of Results.
Now, to maintain consistency across screens, there is a shared object (DownloadImageState) that maintain the state of the flow itself.
How can we map the stream of Results into the state?
I created Binders for this purpose.
The class BindDownloadImageState is a wrapper around the state and is responsible to change it according to the stream of Results coming from the model.
So different presenters can reuse the same binder if they want to share the state (that is provided through a Singleton).

The reason I don't submit UiEVents straight into the model is because I wouldn't have the same reusability. The same UiEvent in different screens may
produce different effects.

To store the history of searched urls, I used Realm.

The image is downloaded in a background thread. So in those 20 seconds the UI thread is completely free.
Finally, when downloading the image from the server, I decode it once using the class BitmapFactory with the option inJustDecodeBounds set to true,
in order to get the size of the image before download it and consume memory.
If the image is bigger then the screen size, the image is scaled down to consume as less memory as possible.

