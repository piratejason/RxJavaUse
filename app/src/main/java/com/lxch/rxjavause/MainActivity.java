package com.lxch.rxjavause;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.FlowableOperator;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        List<File> folders = new ArrayList<>();
        Flowable.fromIterable(folders)
                .flatMap(new Function<File, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(File file) {
                        return Flowable.fromArray(file.listFiles());
                    }
                })
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Function<File, Bitmap>() {
                    @Override
                    public Bitmap apply(File file) {
                        return toBitmap(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) {
                        Log.e("size", "" + bitmap.getByteCount());
                    }
                });
        Observable.fromIterable(folders)
                .flatMap(new Function<File, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(File file) {
                        return Observable.fromArray(file.listFiles());
                    }
                })
                .filter(new Predicate<File>() {
                    @Override
                    public boolean test(File file) {
                        return file.getName().endsWith(".png");
                    }
                })
                .map(new Function<File, Bitmap>() {
                    @Override
                    public Bitmap apply(File file) {
                        return toBitmap(file);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) {
                        Log.e("size", "" + bitmap.getByteCount());
                    }
                });

        Flowable.fromIterable(folders)
                .flatMap(new Function<File, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(File file) {
                        return Flowable.fromArray(file.listFiles());
                    }
                })
                .lift(new FlowableOperator<SB, File>() {
                    @Override
                    public Subscriber<? super File> apply(final Subscriber<? super SB> observer) {
                        return new FlowableSubscriber<File>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                            }

                            @Override
                            public void onNext(File file) {
                                SB s = new SB();
                                s.setFile(file);
                                s.setName(file.getName());
                                observer.onNext(s);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {
                                observer.onComplete();
                            }
                        };
                    }
                })
                .filter(new Predicate<SB>() {
                    @Override
                    public boolean test(SB sb) {
                        return sb.getName().endsWith(".png");
                    }
                })
                .map(new Function<SB, Bitmap>() {
                    @Override
                    public Bitmap apply(SB sb) {
                        return toBitmap(sb.getFile());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) {
                        Log.e("lift", bitmap.getByteCount() + "");
                    }
                });

    }

    private Bitmap toBitmap(File file) {
        return BitmapFactory.decodeFile(file.getPath());
    }
}
