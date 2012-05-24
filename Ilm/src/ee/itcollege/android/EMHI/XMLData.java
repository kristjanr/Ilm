package ee.itcollege.android.EMHI;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLData {
	// XML node keys
	static final String KEY_FORECAST = "forecast";
	static final String KEY_DATE = "date";
	static final String KEY_NIGHT = "night"; 
	static final String KEY_DAY = "day"; 
	static final String KEY_PHENOMENON = "phenomenon";
	static final String KEY_TEMPMIN = "tempmin";
	static final String KEY_TEMPMAX = "tempmax";
	static final String KEY_TEXT = "text";
	static final String KEY_NPHENOMENON = "nPhenomenon";
	static final String KEY_NTEMPMIN = "nTempMin";
	static final String KEY_NTEMPMAX = "nTempMax";
	static final String KEY_NTEXT = "nText";
	static final String KEY_DPHENOMENON = "dPhenomenon";
	static final String KEY_DTEMPMIN = "dTempMin";
	static final String KEY_DTEMPMAX = "dTempMax";
	static final String KEY_DTEXT = "dText";
	
	private ArrayList<HashMap<String, String>> dbItems = new ArrayList<HashMap<String, String>>();	
	
	public ArrayList<HashMap<String, String>> getList() {
		return dbItems;
	}

	public XMLData(String url) {
	ArrayList<HashMap<String, String>> dbItems = new ArrayList<HashMap<String, String>>();		
		
	XMLParser parser = new XMLParser();
	
	String xml = parser.getXmlFromUrl(url); // getting XML
	Document doc = parser.getDomElement(xml); // getting DOM element

	NodeList nl = doc.getElementsByTagName(KEY_FORECAST);
	
	// looping through all item nodes <item>
	for (int i = 0; i < nl.getLength(); i++) {
		
		// creating new HashMap
		HashMap<String, String> map = new HashMap<String, String>();
		Element pe = (Element) nl.item(i);
		
		// adding each child node to HashMap key => value
		// lisada ka öö ja päev
		
		String date=pe.getAttribute(KEY_DATE);
		
		
		map.put(KEY_DATE, date.toString());
		NodeList n = pe.getElementsByTagName(KEY_NIGHT);
		Element e = (Element) n.item(0);
				
		map.put(KEY_NPHENOMENON, parser.getValue(e, KEY_PHENOMENON));
		map.put(KEY_NTEMPMAX, parser.getValue(e, KEY_TEMPMAX));
		map.put(KEY_NTEMPMIN, parser.getValue(e, KEY_TEMPMIN));
		map.put(KEY_NTEXT, parser.getValue(e, KEY_TEXT));
		
		n = pe.getElementsByTagName(KEY_DAY);
		e = (Element) n.item(0);
		map.put(KEY_DPHENOMENON, parser.getValue(e, KEY_PHENOMENON));
		map.put(KEY_DTEMPMAX, parser.getValue(e, KEY_TEMPMAX));
		map.put(KEY_DTEMPMIN, parser.getValue(e, KEY_TEMPMIN));
		map.put(KEY_DTEXT, parser.getValue(e, KEY_TEXT));
		
		
		// adding HashList to ArrayList
		dbItems.add(map);
		
	}
	this.dbItems=dbItems;
	}
}
