package com.booking.security.permissions;

import com.booking.entities.Block;
import com.booking.entities.Booking;
import com.booking.entities.Property;
import com.booking.exceptions.BookingException;
import com.booking.exceptions.error.ExceptionCode;
import com.booking.utils.UserContext;
import org.apache.commons.lang3.Strings;

public class PermissionsService {

    private PermissionsService() {
    }

    public static void canManageProperty(Property property) {
        String userName = UserContext.userName();
        boolean canManage = property.getOwner() != null && userName != null && Strings.CS.equals(userName, property.getOwner().getUsername());
        if (!canManage) {
            throw new BookingException(ExceptionCode.API_FORBIDDEN, "operation.not.allowed");
        }
    }

    public static void canManageBooking(Booking booking) {
        String userName = UserContext.userName();
        boolean canManage = booking.getGuest() != null && userName != null && Strings.CS.equals(userName, booking.getGuest().getUsername());
        if (!canManage) {
            throw new BookingException(ExceptionCode.API_FORBIDDEN, "operation.not.allowed");
        }
    }

    public static void canManageBlock(Block block) {

        String userName = UserContext.userName();
        Property property = block.getProperty();
        boolean canManage = property != null && property.getOwner() != null && userName != null && Strings.CS.equals(userName, property.getOwner().getUsername());
        if (!canManage) {
            throw new BookingException(ExceptionCode.API_FORBIDDEN, "operation.not.allowed");
        }
    }
}
