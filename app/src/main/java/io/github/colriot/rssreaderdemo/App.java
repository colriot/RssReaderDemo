package io.github.colriot.rssreaderdemo;

import android.app.Application;
import com.facebook.stetho.Stetho;
import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         15/03/15
 */
public class App extends Application {

  private ObjectGraph objectGraph;
  private static App INSTANCE;

  private boolean useMocks;

  @Override public void onCreate() {
    super.onCreate();
    INSTANCE = this;

    initGraph();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());

      Stetho.initialize(Stetho.newInitializerBuilder(this)
              .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
              .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
              .build());
    }
  }

  public void inject(Object object) {
    objectGraph.inject(object);
  }

  public static App get() {
    return INSTANCE;
  }

  protected Object getModule(boolean useMocks) {
    return new AppModule(useMocks);
  }

  public void setUseMocks(boolean useMocks) {
    this.useMocks = useMocks;
    initGraph();
  }

  private void initGraph() {
    objectGraph = ObjectGraph.create(getModule(useMocks));
    objectGraph.inject(this);
  }
}
