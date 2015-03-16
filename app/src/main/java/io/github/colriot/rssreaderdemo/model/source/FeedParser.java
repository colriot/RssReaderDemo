package io.github.colriot.rssreaderdemo.model.source;

import android.net.ParseException;
import android.text.format.Time;
import android.util.Xml;
import io.github.colriot.rssreaderdemo.model.Article;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import rx.Subscriber;
import timber.log.Timber;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class FeedParser {

  // Constants indicting XML element names that we're interested in
  private static final int TAG_ID = 1;
  private static final int TAG_TITLE = 2;
  private static final int TAG_PUBLISHED = 3;
  private static final int TAG_LINK = 4;
  private static final int TAG_CONTENT = 5;

  //private static final Pattern IMG_URL_REGEX = Pattern.compile(".*<img.*src=\\\\\"([^\"]*)\\\\\".*>.*");
  private static final Pattern IMG_URL_REGEX = Pattern.compile("[.\\S\\s]*?(https?://[\\S]+\\.(?:jpg|gif|png))[.\\S\\s]*");
  //Pattern imgRegex = Pattern.compile("<img[^>]+src=\\s*['\\\\\"]([^'\"]+)['\\\\\"][^>]*>");

  // We don't use XML namespaces
  private static final String ns = null;

  /** Parse an Atom feed, returning a collection of Entry objects.
   *
   * @param in Atom feed, as a stream.
   * @param subscriber Subscriber for emitting articles.
   * @return List of {@link Article} objects.
   * @throws org.xmlpull.v1.XmlPullParserException on error parsing feed.
   * @throws java.io.IOException on I/O error.
   */
  public List<Article> parse(InputStream in, Subscriber<? super Article> subscriber)
      throws XmlPullParserException, IOException, ParseException {
    try {
      XmlPullParser parser = Xml.newPullParser();
      parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
      parser.setInput(in, null);
      parser.nextTag();
      return readFeed(parser, subscriber);
    } finally {
      in.close();
    }
  }

  /**
   * Decode a feed attached to an XmlPullParser.
   *
   * @param parser Incoming XMl
   * @param subscriber Subscriber for emitting articles
   * @return List of {@link Article} objects.
   * @throws org.xmlpull.v1.XmlPullParserException on error parsing feed.
   * @throws java.io.IOException on I/O error.
   */
  private List<Article> readFeed(XmlPullParser parser, Subscriber<? super Article> subscriber)
      throws XmlPullParserException, IOException, ParseException {
    List<Article> entries = new ArrayList<>();
    Article article;
    // Search for <feed> tags. These wrap the beginning/end of an Atom document.
    parser.require(XmlPullParser.START_TAG, ns, "feed");
    while (parser.next() != XmlPullParser.END_TAG && !subscriber.isUnsubscribed()) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      String name = parser.getName();
      // Starts by looking for the <entry> tag. This tag repeates inside of <feed> for each
      // article in the feed.
      if (name.equals("entry")) {
        article = readEntry(parser);
        Timber.d("Article ready: %s.", article.getImgUrl());
        subscriber.onNext(article);
        entries.add(article);
      } else {
        skip(parser);
      }
    }
    return entries;
  }

  /**
   * Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
   * off to their respective "read" methods for processing. Otherwise, skips the tag.
   */
  private Article readEntry(XmlPullParser parser)
      throws XmlPullParserException, IOException, ParseException {
    parser.require(XmlPullParser.START_TAG, ns, "entry");
    String id = null;
    String title = null;
    String link = null;
    String content = null;
    String imgUrl = null;
    long publishedOn = 0;

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.getEventType() != XmlPullParser.START_TAG) {
        continue;
      }
      switch (parser.getName()) {
        case "id": {
          // Example: <id>urn:uuid:218AC159-7F68-4CC6-873F-22AE6017390D</id>
          id = readTag(parser, TAG_ID);
        }
        break;
        case "title": {
          // Example: <title>Article title</title>
          title = readTag(parser, TAG_TITLE);
        }
        break;
        case "link": {
          // Example: <link rel="alternate" type="text/html" href="http://example.com/article/1234"/>
          //
          // Multiple link types can be included. readAlternateLink() will only return
          // non-null when reading an "alternate"-type link. Ignore other responses.
          String tempLink = readTag(parser, TAG_LINK);
          if (tempLink != null) {
            link = tempLink;
          }
        }
        break;
        case "published": {
          // Example: <published>2003-06-27T12:00:00Z</published>
          Time t = new Time();
          t.parse3339(readTag(parser, TAG_PUBLISHED));
          publishedOn = t.toMillis(false);
        }
        break;
        case "content": {
          content = readTag(parser, TAG_CONTENT);
          if (content != null) {
            final Matcher matcher = IMG_URL_REGEX.matcher(content);
            if (matcher.matches()) {
              imgUrl = matcher.group(1);
            }
          }
        }
        break;
        default:
          skip(parser);
      }
    }
    return new Article(title, content, link, imgUrl);
  }

  /**
   * Process an incoming tag and read the selected value from it.
   */
  private String readTag(XmlPullParser parser, int tagType)
      throws IOException, XmlPullParserException {
    String tag = null;
    String endTag = null;

    switch (tagType) {
      case TAG_ID:
        return readBasicTag(parser, "id");
      case TAG_TITLE:
        return readBasicTag(parser, "title");
      case TAG_PUBLISHED:
        return readBasicTag(parser, "published");
      case TAG_CONTENT:
        return readBasicTag(parser, "content");
      case TAG_LINK:
        return readAlternateLink(parser);
      default:
        throw new IllegalArgumentException("Unknown tag type: " + tagType);
    }
  }

  /**
   * Reads the body of a basic XML tag, which is guaranteed not to contain any nested elements.
   *
   * <p>You probably want to call readTag().
   *
   * @param parser Current parser object
   * @param tag XML element tag name to parse
   * @return Body of the specified tag
   * @throws java.io.IOException
   * @throws org.xmlpull.v1.XmlPullParserException
   */
  private String readBasicTag(XmlPullParser parser, String tag)
      throws IOException, XmlPullParserException {
    parser.require(XmlPullParser.START_TAG, ns, tag);
    String result = readText(parser);
    parser.require(XmlPullParser.END_TAG, ns, tag);
    return result;
  }

  /**
   * Processes link tags in the feed.
   */
  private String readAlternateLink(XmlPullParser parser)
      throws IOException, XmlPullParserException {
    String link = null;
    parser.require(XmlPullParser.START_TAG, ns, "link");
    String tag = parser.getName();
    String relType = parser.getAttributeValue(null, "rel");
    if (relType == null || relType.equals("alternate")) {
      link = parser.getAttributeValue(null, "href");
    }
    while (true) {
      if (parser.nextTag() == XmlPullParser.END_TAG) break;
      // Intentionally break; consumes any remaining sub-tags.
    }
    return link;
  }

  /**
   * For the tags title and summary, extracts their text values.
   */
  private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
    String result = null;
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.getText();
      parser.nextTag();
    }
    return result;
  }

  /**
   * Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
   * if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
   * finds the matching END_TAG (as indicated by the value of "depth" being 0).
   */
  private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
    if (parser.getEventType() != XmlPullParser.START_TAG) {
      throw new IllegalStateException();
    }
    int depth = 1;
    while (depth != 0) {
      switch (parser.next()) {
        case XmlPullParser.END_TAG:
          depth--;
          break;
        case XmlPullParser.START_TAG:
          depth++;
          break;
      }
    }
  }
}
