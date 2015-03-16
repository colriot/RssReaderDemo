package io.github.colriot.rssreaderdemo.model;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
@ToString
@NoArgsConstructor
public class Feed implements Serializable {
  private Long _id;
  private String title;
  private String url;

  public Feed(String title, String url) {
    this.title = title;
    this.url = url;
  }

  public Long getId() {
    return _id;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }
}
