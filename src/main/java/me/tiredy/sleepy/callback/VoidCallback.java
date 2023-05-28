package me.tiredy.sleepy.callback;

@SuppressWarnings("unused")
public interface VoidCallback {
    void onSuccess();

    void onFailure(Throwable throwable);
}
