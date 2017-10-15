/*
 *  Copyright 2017 Seth W
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license.  See the LICENSE file for details.
*/

package io.swvn.discordgaming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.sentry.Sentry;

import java.io.File;

/**
 * @author swvn9
 */
public class Config {

    public static ConfigMap pull(){

        try{

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            File configFile = new File("config.yml");

            return mapper.readValue(configFile, ConfigMap.class);


        } catch(Exception exception){

            exception.printStackTrace();
            Sentry.capture(exception);
            return new ConfigMap();

        }
    }

    public static String prefix(){
        try{

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            File configFile = new File("config.yml");

            return mapper.readValue(configFile, ConfigMap.class).prefix;


        } catch(Exception exception){

            exception.printStackTrace();
            Sentry.capture(exception);
            return new ConfigMap().prefix;

        }
    }

    @SuppressWarnings("unused")
    @JsonIgnoreProperties(ignoreUnknown = false)
    static class ConfigMap {
        String dsn;
        String token;
        String env = "test";
        String prefix = "!";

        public String getDsn() {
            return dsn;
        }

        public String getToken() {
            return token;
        }

        public String getEnv() {
            return env;
        }

        public String getPrefix(){
            if("test".equals(env))
                return ">";
            return prefix;
        }

    }

}
