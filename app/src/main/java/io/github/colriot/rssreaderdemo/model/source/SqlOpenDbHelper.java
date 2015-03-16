package io.github.colriot.rssreaderdemo.model.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import io.github.colriot.rssreaderdemo.model.Article;
import io.github.colriot.rssreaderdemo.model.Feed;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * @author Sergey Ryabov <colriot@gmail.com>
 *         14/03/15
 */
public class SqlOpenDbHelper extends SQLiteOpenHelper {

  private static final String DB_NAME = "rssreader.db";
  private static final int VERSION = 18;

  static {
    cupboard().register(Feed.class);
    cupboard().register(Article.class);
  }

  public SqlOpenDbHelper(Context context) {
    super(context, DB_NAME, null, VERSION);
  }

  @Override public void onCreate(@NonNull SQLiteDatabase db) {
    cupboard().withDatabase(db).createTables();
  }

  @Override public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    cupboard().withDatabase(db).upgradeTables();
  }

  @Override public void onDowngrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
    cupboard().withDatabase(db).dropAllTables();
    onCreate(db);
  }
}
