package com.mapotempo.fleet.core.exception;

import com.couchbase.lite.CouchbaseLiteException;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * CoreException.
 */
public class CoreException extends Exception{

    public CoreException(NoSuchMethodException e) {
        super(e);
    }

    public CoreException(MalformedURLException e) {
        super(e);
    }

    public CoreException(CouchbaseLiteException e) {
        super(e);
    }

    public CoreException(IOException e) {
        super(e);
    }

    public CoreException(String string) {
        super(string);
    }
}
