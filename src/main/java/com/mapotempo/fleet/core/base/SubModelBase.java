package com.mapotempo.fleet.core.base;

import com.mapotempo.fleet.core.DatabaseHandler;

import java.util.Map;

/** The abstract submodel base.*/
public abstract class SubModelBase {

    public SubModelBase() {
        return;
    }

    public SubModelBase(Map map, DatabaseHandler databaseHandler) {
        fromMap(map);
        return;
    }

    abstract protected void fromMap(Map map);
}
