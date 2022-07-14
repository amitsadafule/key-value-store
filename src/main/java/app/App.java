package app;

import app.cache.Cache;
import io.jooby.Jooby;
import io.jooby.ServerOptions;

public class App extends Jooby {

  {

    setServerOptions(new ServerOptions()
      //.setBufferSize(16384)
      .setIoThreads(1)
      .setWorkerThreads(1)
    );

    Cache cache = Cache.getInstance();

    post("/set", ctx -> cache.set(ctx.query("key").value(), ctx.query("value").value()));
    get("/get", ctx -> cache.get(ctx.query("key").value()));
  }

  public static void main(final String[] args) {
    runApp(args, App::new);
  }

}
