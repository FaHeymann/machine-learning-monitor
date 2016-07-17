package services;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

@Singleton
public class Properties {

    private Config config;

    public Properties() {
        this.config = ConfigFactory.load("dev.properties");
    }

    public Config getConfig() {
        return this.config;
    }

}
