package me.tiredy.sleepy.callback;

@SuppressWarnings("unused")
public interface ResultCallback<T> {
    void onSuccess(T result);

    void onFailure(Throwable throwable);
}
