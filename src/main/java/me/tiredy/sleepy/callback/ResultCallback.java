package me.tiredy.sleepy.callback;

@SuppressWarnings("unused")
public interface CallbackReturnable<T> {
    void onSuccess(T result);

    void onFailure(Throwable throwable);
}
