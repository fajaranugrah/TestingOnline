package com.example.fajar.testingonline;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MemoryCache {
    private Map<String, SoftReference<Bitmap>> cache=Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

    public Bitmap get(String judul){
        if(!cache.containsKey(judul))
            return null;
        SoftReference<Bitmap> ref=cache.get(judul);
        return ref.get();
    }

    public void put(String judul, Bitmap bitmap){
        cache.put(judul, new SoftReference<Bitmap>(bitmap));
    }

    public void clear() {
        cache.clear();
    }
}
