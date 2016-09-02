import com.google.inject.AbstractModule;

import play.libs.akka.AkkaGuiceSupport;
import services.DevProperties;
import services.Properties;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public final void configure() {
        bind(Properties.class).to(DevProperties.class);
    }

}
