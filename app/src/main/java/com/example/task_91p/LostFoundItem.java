package com.example.task_91p;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a lost/found post
 */
public class LostFoundItem implements Parcelable {
    private int id;
    private String type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private double lat;
    private double lon;

//    constructors

    public LostFoundItem() {}

    public LostFoundItem(String type, String name, String phone, String description, String date, String location, Double lat, Double lon) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.lat = lat;
        this.lon = lon;
    }

//    required for Parcelable
    protected LostFoundItem(Parcel in) {
        id = in.readInt();
        type = in.readString();
        name = in.readString();
        phone = in.readString();
        description = in.readString();
        date = in.readString();
        location = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public static final Creator<LostFoundItem> CREATOR = new Creator<LostFoundItem>() {
        @Override
        public LostFoundItem createFromParcel(Parcel in) {
            return new LostFoundItem(in);
        }

        @Override
        public LostFoundItem[] newArray(int size) {
            return new LostFoundItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(type);
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeString(location);
        parcel.writeDouble(lat);
        parcel.writeDouble(lon);
    }

//    getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() {
        return name;
    }
    public String getPhone() {
        return phone;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getLocation() { return location; }
    public Double getLat() { return lat; }
    public Double getLon() { return lon; }

//    setters
    public void setId(int id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setLocation(String location) { this.location = location; }
    public void setLat(Double lat) { this.lat = lat; }
    public void setLon(Double lon) { this.lon = lon; }

}
