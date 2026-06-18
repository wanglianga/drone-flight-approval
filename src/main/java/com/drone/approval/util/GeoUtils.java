package com.drone.approval.util;

import java.math.BigDecimal;

public class GeoUtils {

    private static final double EARTH_RADIUS_METERS = 6371000.0;

    public static double calculateDistanceMeters(BigDecimal lat1, BigDecimal lon1,
                                                  BigDecimal lat2, BigDecimal lon2) {
        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1.doubleValue()))
                * Math.cos(Math.toRadians(lat2.doubleValue()))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_METERS * c;
    }

    public static boolean isPointInCircle(BigDecimal pointLat, BigDecimal pointLon,
                                           BigDecimal centerLat, BigDecimal centerLon,
                                           double radiusMeters) {
        double distance = calculateDistanceMeters(pointLat, pointLon, centerLat, centerLon);
        return distance <= radiusMeters;
    }

    public static boolean isAltitudeInRange(double altitude, double minAltitude, double maxAltitude) {
        return altitude >= minAltitude && altitude <= maxAltitude;
    }

    public static double calculateMinDistanceToCircle(BigDecimal pointLat, BigDecimal pointLon,
                                                       BigDecimal centerLat, BigDecimal centerLon,
                                                       double radiusMeters) {
        double distance = calculateDistanceMeters(pointLat, pointLon, centerLat, centerLon);
        return Math.max(0, distance - radiusMeters);
    }
}
