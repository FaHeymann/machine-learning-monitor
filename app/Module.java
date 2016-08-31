import com.google.inject.AbstractModule;

import helpers.DataFileParser;
import play.libs.akka.AkkaGuiceSupport;
import services.Properties;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        bind(Properties.class);
        bind(DataFileParser.class);
    }

}