package services;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class DevProperties implements Properties {

    private Config config;

    public DevProperties() {
        this.config = ConfigFactory.load("dev.properties");
    }

    public Config getConfig() {
        return this.config;
    }

}
