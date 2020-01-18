package com.watchmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.watchmovies.http.MovieHttp;
import com.watchmovies.model.exception.MovieParseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


@Entity(tableName = "movies")
public class Movie implements Parcelable {

    private static final DateFormat dateFormatter =
            new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    @PrimaryKey
    private int id;

    private String title;

    @ColumnInfo(name = "release_data")
    private Date releaseDate;

    @ColumnInfo(name = "poster_image_url")
    private String posterImageUrl;

    @ColumnInfo(name = "start", typeAffinity = ColumnInfo.REAL)
    private double stars;

    @ColumnInfo(name = "synopsis")
    private String plotSynopsis;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte [] posterImage;


    public Movie(int id, @NonNull String title, @NonNull Date releaseDate,
                 @NonNull String posterImageUrl, double stars, @NonNull String plotSynopsis,
                 boolean isFavorite, byte[] posterImage) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterImageUrl = posterImageUrl;
        this.stars = stars;
        this.plotSynopsis = plotSynopsis;
        this.isFavorite = isFavorite;
        this.posterImage = posterImage;
    }


    @Ignore
    public Movie(@NonNull final String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            this.id = jsonObject.getInt("id");
            this.title = jsonObject.optString("title");
            String date = jsonObject.optString("release_date");
            this.releaseDate = date.length() > 1 ? dateFormatter.parse(date) : new Date();
            this.posterImageUrl = MovieHttp.buildUrlFromPath(jsonObject.optString("poster_path"));
            this.stars = jsonObject.optDouble("vote_average");
            this.plotSynopsis = jsonObject.optString("overview");
            this.isFavorite = false;
            this.posterImage = null;
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
            throw new MovieParseException();
        }
    }


    @Ignore
    private Movie(Parcel parcel) {
        this.id = parcel.readInt();
        this.title = parcel.readString();
        this.releaseDate = new Date(parcel.readLong());
        this.posterImageUrl = parcel.readString();
        this.stars = parcel.readDouble();
        this.plotSynopsis = parcel.readString();
        this.isFavorite = parcel.readByte() != 0;
        if (this.posterImage != null) {
            parcel.readByteArray(this.posterImage);
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public byte[] getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(byte[] mPosterImage) {
        this.posterImage = mPosterImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeLong(this.releaseDate.getTime());
        parcel.writeString(this.posterImageUrl);
        parcel.writeDouble(this.stars);
        parcel.writeString(this.plotSynopsis);
        parcel.writeByte((byte) (this.isFavorite ? 1 : 0));
        parcel.writeByteArray(this.posterImage);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
