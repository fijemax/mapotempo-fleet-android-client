package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

@DocumentBase(type = "device")
public class Device extends ModelBase
{

    public Device() {
        mId = "Device_434f4e3e-1039-4270-adc8-0319b0636e81";
    }

    public Device(String name) {
        this.mName = name;
        this.mLocation = new Location(0., 0.);
    }

    /**
     * Constructor.
     * @param name Device name
     * @param location Device Location
     */
    public Device(String name, Location location) {
        this.mName = name;
        this.mLocation = location;
    }

    @FieldBase(name = "date")
    public int date;

    @FieldBase(name = "name")
    public String mName;

    @FieldBase(name = "location")
    public Location mLocation;
}
