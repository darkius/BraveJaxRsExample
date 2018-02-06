package lt.darkius;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.jaxrs2.TracingFeature;
import lt.darkius.rest.RestService;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.okhttp3.OkHttpSender;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by darkius on 2018-01-30.
 */
@ApplicationPath("/brave-application")
public class BraveApplication extends Application{

    private final static String APP_NAME = "brave-example";

    private final static String ZIPKIN_URL = "http://127.0.0.1:9411/api/v2/spans";

    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> empty = new HashSet<>();

    @Inject
    private RestService restService;

    @PostConstruct
    public void addSingletons() {
        singletons.add(restService);
        singletons.add(TracingFeature.create(getHttpTracing()));
    }

    @Produces
    public HttpTracing getHttpTracing() {
        return HttpTracing.newBuilder(buildTracing()).build();
    }

    private Tracing buildTracing() {
        return Tracing.newBuilder()
                .localServiceName(APP_NAME)
                .spanReporter(AsyncReporter.create(OkHttpSender.create(ZIPKIN_URL)))
                .build();
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
