package com.tsinghua.course.Base.CustomizedClass;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @描述 定位的格式
 */
@Document("Location")
public class Location {
    // 经度
    double longitude;
    // 纬度
    double latitude;
    // 附加信息
    String locationInfo;

    public Location() {
        longitude = latitude = 0.0;
        locationInfo = "";
    }

    public Location(double longitude, double latitude, String locationInfo) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationInfo = locationInfo;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }
}
