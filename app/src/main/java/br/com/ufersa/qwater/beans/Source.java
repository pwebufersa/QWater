package br.com.ufersa.qwater.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Fonte de água cadastrada pelo usuário, identificada pelo nome e localizada por
 * coordenadas.
 *
 * O nome é único. Quando a localização não é informada, latitude e longitude
 * guardam o texto do recurso R.string.nao_informada.
 */
@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class Source implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String type;
    private String latitude;
    private String longitude;

    public Source(@NonNull String name, String type, String latitude, String longitude) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Source() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeInt(id);
        destination.writeString(name);
        destination.writeString(type);
        destination.writeString(latitude);
        destination.writeString(longitude);
    }

    protected Source(Parcel source) {
        id = source.readInt();
        name = source.readString();
        type = source.readString();
        latitude = source.readString();
        longitude = source.readString();
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel source) {
            return new Source(source);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
