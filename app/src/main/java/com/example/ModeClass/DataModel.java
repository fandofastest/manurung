package com.example.ModeClass;

public class DataModel {

    Integer id,favorite,vote;
    String minecraft_id,category,title,gambar,preview_3d,url_download,url_apply;


    public DataModel(Integer id, String minecraft_id, String category, String title, String gambar, String preview_3d, String url_download, String url_apply) {
        this.id = id;
        this.minecraft_id = minecraft_id;
        this.category = category;
        this.title = title;
        this.gambar = gambar;
        this.preview_3d = preview_3d;
        this.url_download = url_download;
        this.url_apply = url_apply;
    }

    public DataModel() {

    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMinecraft_id() {
        return minecraft_id;
    }

    public void setMinecraft_id(String minecraft_id) {
        this.minecraft_id = minecraft_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getPreview_3d() {
        return preview_3d;
    }

    public void setPreview_3d(String preview_3d) {
        this.preview_3d = preview_3d;
    }

    public String getUrl_download() {
        return url_download;
    }

    public void setUrl_download(String url_download) {
        this.url_download = url_download;
    }

    public String getUrl_apply() {
        return url_apply;
    }

    public void setUrl_apply(String url_apply) {
        this.url_apply = url_apply;
    }
}
