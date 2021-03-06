package io.openshift.booster;

import io.vertx.ext.sql.SQLOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.jdbc.JDBCClient;
import rx.Completable;
import rx.Observable;

/**
 * Simple helper to bootstrap your Database.
 *
 * @author Paulo Lopes
 */
public class DBInitHelper {

  private DBInitHelper() {
    // Private constructor.
  }

  public static Completable initDatabase(Vertx vertx, JDBCClient jdbc) {
    return jdbc.rxGetConnection()
      .map(c -> c.setOptions(new SQLOptions().setAutoGeneratedKeys(true)))
      .flatMapCompletable(connection ->
        vertx.fileSystem().rxReadFile("ddl.sql")
          .flatMapObservable(buffer -> Observable.from(buffer.toString().split(";")))
          .flatMapSingle(connection::rxExecute)
          .doAfterTerminate(connection::close)
          .toCompletable()
      );
  }
}
