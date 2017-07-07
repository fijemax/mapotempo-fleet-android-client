package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

@DocumentBase(type = "mission")
public class Mission extends ModelBase {

    public Mission() {
    }

    /**
     * Constructor.
     * @param name Mission name
     * @param location Mission Location
     */
    public Mission(String name, Location location, Device device) {
        this.mName = name;
        this.mLocation = location;
        this.mDevice = device;
    }

    @FieldBase(name = "name")
    public String mName;

    @FieldBase(name = "location")
    public Location mLocation;

    @FieldBase(name = "device", foreign = true)
    public Device mDevice;
}
