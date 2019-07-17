package com.flutra.augmentedreality;
import android.os.Parcel;
import android.os.Parcelable;

public class Coord implements Parcelable {

    private Float lon;

    private Float lat;

    public Coord()
    {}
    protected Coord(Parcel in) {
        if (in.readByte() == 0) {
            lon = null;
        } else {
            lon = in.readFloat();
        }
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readFloat();
        }
    }

    public static final Creator<Coord> CREATOR = new Creator<Coord>() {
        @Override
        public Coord createFromParcel(Parcel in) {
            return new Coord(in);
        }

        @Override
        public Coord[] newArray(int size) {
            return new Coord[size];
        }
    };

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (lon == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(lon);
        }
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(lat);
        }
    }
}