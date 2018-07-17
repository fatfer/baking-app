package com.udacity.bakingapp;

public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(T result);
}
