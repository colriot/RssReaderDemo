package io.github.colriot.rssreaderdemo.model;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
@ToString
@NoArgsConstructor
public class Article implements Serializable {
  private Long _id;
  private String title;
  private String content;
  private String url;
  private String imgUrl;

  private Long feedId;

  public Article(String title, String content, String url, String imgUrl) {
    this.title = title;
    this.content = content;
    this.url = url;
    this.imgUrl = imgUrl;
  }

  public Long getId() {
    return _id;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public String getUrl() {
    return url;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public Long getFeedId() {
    return feedId;
  }

  public void setFeedId(long feedId) {
    this.feedId = feedId;
  }
}
