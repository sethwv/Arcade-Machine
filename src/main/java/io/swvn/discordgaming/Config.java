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

    static ConfigMap pull(){

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

    @SuppressWarnings("unused")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class ConfigMap {
        private String dsn = "";
        private String token = "";
        private String env = "testing";

        String getDsn() {
            return dsn;
        }

        String getToken() {
            return token;
        }

        String getEnv() {
            return env;
        }
    }

}
