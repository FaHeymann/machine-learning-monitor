import com.google.inject.AbstractModule;

import play.libs.akka.AkkaGuiceSupport;
import services.Properties;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        bind(Properties.class);
    }

}