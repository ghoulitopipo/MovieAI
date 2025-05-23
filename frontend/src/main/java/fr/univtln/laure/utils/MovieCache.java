package fr.univtln.laure.utils;

public class MovieCache {
    private static long id;
    private static String title;
    private static String genres;
    private static String posterUrl;

    public static long getId() { return id; }
    public static void setId(long id_) { id = id_; }

    public static String getTitle() { return title; }
    public static void setTitle(String title_) { title = title_; }

    public static String getGenres() { return genres; }
    public static void setGenres(String genres_) { genres = genres_; }

    public static String getPosterUrl() { return posterUrl; }
    public static void setPosterUrl(String posterUrl_) { posterUrl = posterUrl_; }
}