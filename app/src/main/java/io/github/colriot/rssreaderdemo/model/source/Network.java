package io.github.colriot.rssreaderdemo.model.source;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.github.colriot.rssreaderdemo.NetworkException;
import io.github.colriot.rssreaderdemo.model.Article;
import java.io.IOException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.xmlpull.v1.XmlPullParserException;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
@Singleton public class Network {
  private final OkHttpClient client;
  private final FeedParser feedParser;

  @Inject public Network(OkHttpClient client) {
    this.client = client;
    this.feedParser = new FeedParser();
  }

  public Observable<Article> fetchArticles(final String rssUrl) {
    return Observable.create(new Observable.OnSubscribe<Article>() {
      @Override public void call(Subscriber<? super Article> subscriber) {
        try {
          Timber.d("Starting network request.");
          final Response response =
              client.newCall(new Request.Builder().url(rssUrl).build()).execute();
          Timber.d("Network request ready.");
          if (response.isSuccessful()) {
            feedParser.parse(response.body().byteStream(), subscriber);
            subscriber.onCompleted();
            Timber.d("Parsing finished.");
          } else {
            subscriber.onError(new NetworkException(response.code(), response.message()));
          }
        } catch (XmlPullParserException | IOException e) {
          subscriber.onError(e);
        }
      }
    });
  }
}
