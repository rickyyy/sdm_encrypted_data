package eit.nl.utwente.sdm.test;

import java.sql.SQLException;

import eit.nl.utwente.sdm.encryptsql.actors.Client;
import eit.nl.utwente.sdm.encryptsql.actors.Consultant;

public class PopulateDB {

	public static void main(String[] args) throws SQLException {
		Consultant c1 = new Consultant("John Smith", "Berkshire Hathaway");
		c1.persist();
		Consultant c2 = new Consultant("Marie Dubois", "AXA");
		c2.persist();
		Consultant c3 = new Consultant("Johan Schmidt", "Alianz");
		c3.persist();
		Consultant c4 = new Consultant("Mark de Jong", "ING");
		c4.persist();
		Client cl1 = new Client("Melanie J. Smith", c1.getId(), "Amsinckstrasse 67, 02897 Ostritz");
		cl1.persist();
		Client cl2 = new Client("Raymond B. Nichols", c1.getId(), "2570 Pine Tree Lane Mc Lean, MD 22102");
		cl2.persist();
		Client cl3 = new Client("Delmare Latourelle", cl2.getId(), "23, rue Mare aux Carats 93100 MONTREUIL");
		cl3.persist();
		Client cl4 = new Client("Monika Reiniger", cl3.getId(), "Heiligengeistbrücke 46, 91465 Ergersheim");
		cl4.persist();
		Client cl5 = new Client("Stefan Ebersbach", cl3.getId(), "Stuttgarter Platz 82, 56294 Gierschnach");
		cl5.persist();
		Client cl6 = new Client("Marie Hofmann", cl3.getId(), "Koepenicker Str. 59, 56357 Weyer");
		cl6.persist();
		Client cl7 = new Client("Ada van de Wardt", cl4.getId(), "Bloemstraat 66, 8603 XV  Sneek");
		cl7.persist();
	}
	
}
