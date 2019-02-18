package fr.epita.quiz.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import fr.epita.logger.Logger;

public class Configuration {

	private Properties properties;

	private static Configuration instance;

	private Configuration() {
		this.properties = new Properties();
		String confLocation = System.getProperty("conf.location");
		try (InputStream is = new FileInputStream(new File(confLocation))) {
			properties.load(is);
		} catch (IOException e) {
			Logger.logMessage("Error reading configuration file");
		}

	}

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;

	}

	public String getConfigurationValue(String configurationKey) {
		return properties.getProperty(configurationKey);
	}

}
