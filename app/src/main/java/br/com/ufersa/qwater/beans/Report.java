package br.com.ufersa.qwater.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// Tutorial da relação one-to-many no Room:
// https://www.bignerdranch.com/blog/room-data-storage-for-everyone/
// https://medium.com/@tonyowen/room-entity-annotations-379150e1ca82

// O parcelable é usado para passar um objeto (report) entre activities

/**
 * Relatório de análise de uma fonte de água.
 *
 * Os nomes das colunas repetem os do banco original, por meio de
 * {@link ColumnInfo}, para que a versão 1 do esquema continue válida. O nome da
 * fonte é guardado junto do identificador para evitar uma consulta extra na
 * listagem de relatórios.
 */
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Source.class,
                parentColumns = "id",
                childColumns = "souId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Source.class,
                parentColumns = "name",
                childColumns = "souName",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Report implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "ca")
    private double calcium;
    @ColumnInfo(name = "mg")
    private double magnesium;
    @ColumnInfo(name = "k")
    private double potassium;
    @ColumnInfo(name = "na")
    private double sodium;
    @ColumnInfo(name = "co3")
    private double carbonate;
    @ColumnInfo(name = "hco3")
    private double bicarbonate;
    @ColumnInfo(name = "cl")
    private double chloride;
    @ColumnInfo(name = "so4")
    private double sulfate;
    @ColumnInfo(name = "b")
    private double boron;
    @ColumnInfo(name = "cea")
    private double electricalConductivity;
    @ColumnInfo(name = "ph")
    private double ph;
    @ColumnInfo(name = "normalSar")
    private double normalSar;
    @ColumnInfo(name = "correctedSar")
    private double correctedSar;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "souId")
    private int sourceId;
    @ColumnInfo(name = "souName")
    private String sourceName;

    public Report() {

    }

    public Report(double calcium, double magnesium, double potassium, double sodium,
                  double carbonate, double bicarbonate, double chloride, double sulfate,
                  double boron, double electricalConductivity, double ph) {
        this.calcium = calcium;
        this.magnesium = magnesium;
        this.potassium = potassium;
        this.sodium = sodium;
        this.carbonate = carbonate;
        this.bicarbonate = bicarbonate;
        this.chloride = chloride;
        this.sulfate = sulfate;
        this.boron = boron;
        this.electricalConductivity = electricalConductivity;
        this.ph = ph;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeInt(id);
        destination.writeDouble(calcium);
        destination.writeDouble(magnesium);
        destination.writeDouble(potassium);
        destination.writeDouble(sodium);
        destination.writeDouble(carbonate);
        destination.writeDouble(bicarbonate);
        destination.writeDouble(chloride);
        destination.writeDouble(sulfate);
        destination.writeDouble(boron);
        destination.writeDouble(electricalConductivity);
        destination.writeDouble(ph);
        destination.writeDouble(normalSar);
        destination.writeDouble(correctedSar);
        destination.writeLong(date);
        destination.writeInt(sourceId);
        destination.writeString(sourceName);
    }

    protected Report(Parcel source) {
        id = source.readInt();
        calcium = source.readDouble();
        magnesium = source.readDouble();
        potassium = source.readDouble();
        sodium = source.readDouble();
        carbonate = source.readDouble();
        bicarbonate = source.readDouble();
        chloride = source.readDouble();
        sulfate = source.readDouble();
        boron = source.readDouble();
        electricalConductivity = source.readDouble();
        ph = source.readDouble();
        normalSar = source.readDouble();
        correctedSar = source.readDouble();
        date = source.readLong();
        sourceId = source.readInt();
        sourceName = source.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel source) {
            return new Report(source);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getCalcium() {
        return calcium;
    }

    public void setCalcium(double calcium) {
        this.calcium = calcium;
    }

    public double getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(double magnesium) {
        this.magnesium = magnesium;
    }

    public double getPotassium() {
        return potassium;
    }

    public void setPotassium(double potassium) {
        this.potassium = potassium;
    }

    public double getSodium() {
        return sodium;
    }

    public void setSodium(double sodium) {
        this.sodium = sodium;
    }

    public double getCarbonate() {
        return carbonate;
    }

    public void setCarbonate(double carbonate) {
        this.carbonate = carbonate;
    }

    public double getBicarbonate() {
        return bicarbonate;
    }

    public void setBicarbonate(double bicarbonate) {
        this.bicarbonate = bicarbonate;
    }

    public double getChloride() {
        return chloride;
    }

    public void setChloride(double chloride) {
        this.chloride = chloride;
    }

    public double getSulfate() {
        return sulfate;
    }

    public void setSulfate(double sulfate) {
        this.sulfate = sulfate;
    }

    public double getBoron() {
        return boron;
    }

    public void setBoron(double boron) {
        this.boron = boron;
    }

    public double getElectricalConductivity() {
        return electricalConductivity;
    }

    public void setElectricalConductivity(double electricalConductivity) {
        this.electricalConductivity = electricalConductivity;
    }

    public double getPh() {
        return ph;
    }

    public void setPh(double ph) {
        this.ph = ph;
    }

    public double getNormalSar() {
        return normalSar;
    }

    public void setNormalSar(double normalSar) {
        this.normalSar = normalSar;
    }

    public double getCorrectedSar() {
        return correctedSar;
    }

    public void setCorrectedSar(double correctedSar) {
        this.correctedSar = correctedSar;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
