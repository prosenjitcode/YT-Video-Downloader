package com.bengalitutorial.ytvideodownloder.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Snippet {

  @SerializedName("publishedAt")
  @Expose
  private String publishedAt;
  @SerializedName("channelId")
  @Expose
  private String channelId;
  @SerializedName("title")
  @Expose
  private String title;
  @SerializedName("description")
  @Expose
  private String description;
  @SerializedName("thumbnails")
  @Expose
  private Thumbnails thumbnails;
  @SerializedName("channelTitle")
  @Expose
  private String channelTitle;
  @SerializedName("liveBroadcastContent")
  @Expose
  private String liveBroadcastContent;

  public String getPublishedAt() {
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
    Date date = null;
    try {
      date = inputFormat.parse(publishedAt);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    String formattedDate = outputFormat.format(date);
    return formattedDate;
  }

  public void setPublishedAt(String publishedAt) {
    this.publishedAt = publishedAt;
  }

  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Thumbnails getThumbnails() {
    return thumbnails;
  }

  public void setThumbnails(Thumbnails thumbnails) {
    this.thumbnails = thumbnails;
  }

  public String getChannelTitle() {
    return channelTitle;
  }

  public void setChannelTitle(String channelTitle) {
    this.channelTitle = channelTitle;
  }

  public String getLiveBroadcastContent() {
    return liveBroadcastContent;
  }

  public void setLiveBroadcastContent(String liveBroadcastContent) {
    this.liveBroadcastContent = liveBroadcastContent;
  }

}