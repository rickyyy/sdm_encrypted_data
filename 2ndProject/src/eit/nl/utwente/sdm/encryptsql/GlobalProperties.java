package eit.nl.utwente.sdm.encryptsql;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GlobalProperties {

	private static GlobalProperties instance = null;
	private Properties propsFromFile;

	/**
	 * See
	 * http://docs.oracle.com/javase/tutorial/essential/environment/properties
	 * .html for details
	 * 
	 * @throws IOException
	 */
	private GlobalProperties() throws IOException {
		propsFromFile = new Properties();

		FileInputStream in = new FileInputStream("configs/configs");
		propsFromFile.load(in);
		in.close();
		System.out.println(propsFromFile.keySet());
	}

	public static GlobalProperties getInstance() {
		if (instance == null) {
			try {
				instance = new GlobalProperties();
			} catch (IOException e) {
				return null;
			}
		}
		return instance;
	}

	public String getProperty(String key) {
		return (String) propsFromFile.get(key);
	}

}