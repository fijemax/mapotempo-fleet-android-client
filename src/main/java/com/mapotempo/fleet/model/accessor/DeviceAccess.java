package com.mapotempo.fleet.model.accessor;

import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Device;

/**
 * DeviceAccess.
 */
public class DeviceAccess extends Access<Device> {
    public DeviceAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Device.class, dbHandler);
    }
}
