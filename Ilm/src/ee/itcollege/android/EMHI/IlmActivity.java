package ee.itcollege.android.EMHI;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ee.itcollege.android.EMHI.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class IlmActivity extends ListActivity {

	// All static variables
	
	static final String KEY_DATE = "date";
	static final String KEY_TRACK = "track";
	static final String KEY_LOCATION = "location";
	//võtmesõnad listi jaoks
	static final String KEY_NPHENOMENON = "nPhenomenon";
	static final String KEY_NTEMPMIN = "nTempMin";
	static final String KEY_NTEMPMAX = "nTempMax";
	static final String KEY_NTEXT = "nText";
	static final String KEY_DPHENOMENON = "dPhenomenon";
	static final String KEY_DTEMPMIN = "dTempMin";
	static final String KEY_DTEMPMAX = "dTempMax";
	static final String KEY_DTEXT = "dText";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (isNetworkConnected()){
		setContentView(R.layout.main);
		final ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
		
		
		long lastConnDate=0;
		
		SharedPreferences readPreferences = 
			    getSharedPreferences("MyTime", MODE_PRIVATE);
		
		lastConnDate = readPreferences.getLong("lastConnDateTime", 0);
		Date lastDate = new Date(lastConnDate);
		
		int x =3;
		long millisInXHours = 1000 * 60 * 60 * x;
		
		Date hoursAgo = new Date(new Date().getTime() - millisInXHours);
		Log.d("Ilm","Viimane uuendus "+ lastDate);
		Log.d("Ilm","Praegusest "+ x +" tundi tagasi "+ hoursAgo);
		
		if(lastDate.before(hoursAgo)){
		// read from web and add to database
			XMLData xmlData=new XMLData(getString(R.string.url));
			addMissing(xmlData.getList());
		//save now as new last connection time
		
		
		SharedPreferences writePreferences = 
			    getSharedPreferences("MyTime", MODE_PRIVATE);
			SharedPreferences.Editor editor = writePreferences.edit();
			editor.putLong("lastConnDateTime", new Date().getTime());
			editor.commit();
	
		}
		menuItems.addAll(readDb());
		
		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, menuItems,
				R.layout.list_item,
				new String[] { KEY_DATE, KEY_NPHENOMENON, KEY_NTEMPMAX, KEY_NTEMPMIN,KEY_DPHENOMENON, KEY_DTEMPMAX, KEY_DTEMPMIN}, new int[] {
						R.id.date, 
						//night forecast
						R.id.nphenomenon, R.id.ntempmax,R.id.ntempmin, 
						//day forecast
						R.id.dphenomenon, R.id.dtempmax,R.id.dtempmin,});

		setListAdapter(adapter);

		
		// nupp andmebaasi tühjendamiseks.
		/*Button dropButton = (Button) findViewById(R.id.clearDb);
		dropButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getContentResolver().delete(IlmContentProvider.CONTENT_URI,null,null);
			
			}
		});*/
		Button listenButton = (Button) findViewById(R.id.listen);
		listenButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				XMLParser parser = new XMLParser();
				
				String xml = parser.getXmlFromUrl(getString(R.string.urlMp3)); // getting XML
				Document doc = parser.getDomElement(xml); // getting DOM element

				NodeList tracks = doc.getElementsByTagName(KEY_TRACK);
				Element firstTrack = (Element) tracks.item(0);
				NodeList locations = firstTrack.getElementsByTagName(KEY_LOCATION);
				Element location = (Element) locations.item(0);
				String mp3Location =location.getTextContent();
				Log.d("Ilm", "Sound location:"+mp3Location+":");
				
				Intent intent = new Intent(Intent.ACTION_VIEW);		    
				intent.setData(Uri.parse(mp3Location));		    	
		    	startActivity(intent);	    	
		    	
			}
		});
		// selecting single ListView item
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
		
				String date = ((TextView) view.findViewById(R.id.date)).getText().toString();
				String nphen = ((TextView) view.findViewById(R.id.nphenomenon)).getText().toString();
				String ntempmax = ((TextView) view.findViewById(R.id.ntempmax)).getText().toString();
				String ntempmin = ((TextView) view.findViewById(R.id.ntempmin)).getText().toString();				
				String dphen = ((TextView) view.findViewById(R.id.dphenomenon)).getText().toString();
				String dtempmax = ((TextView) view.findViewById(R.id.dtempmax)).getText().toString();
				String dtempmin = ((TextView) view.findViewById(R.id.dtempmin)).getText().toString();
				String ntext = null;
				String dtext = null;
				for (HashMap<String, String> map : menuItems) {
				if (map.get(KEY_DATE)==date){
					ntext = map.get(KEY_NTEXT);
					dtext = map.get(KEY_DTEXT);
				}
				}
				
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);
				
				in.putExtra(KEY_DATE, date);
				in.putExtra(KEY_NPHENOMENON, nphen);
				in.putExtra(KEY_NTEMPMAX, ntempmax);
				in.putExtra(KEY_NTEMPMIN, ntempmin);
				in.putExtra(KEY_NTEXT, ntext);
				in.putExtra(KEY_DPHENOMENON, dphen);
				in.putExtra(KEY_DTEMPMAX, dtempmax);
				in.putExtra(KEY_DTEMPMIN, dtempmin);
				in.putExtra(KEY_DTEXT, dtext);
				startActivity(in);

			}
		});
		}
		else {
			
			showDialog(1);
		}
	}

	
@Override
	protected Dialog onCreateDialog(int id) {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(R.string.noNetwork)
	       .setCancelable(false)
	       .setPositiveButton(R.string.quit, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                finish();
	           }
	       });
	       
			AlertDialog alert = builder.create();
			alert.show();
		return super.onCreateDialog(id);
	}


private ArrayList<HashMap<String, String>> readDb() {
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
	
	Cursor cursor = getContentResolver()
			.query(IlmContentProvider.CONTENT_URI, null, null,
					null, null);
	startManagingCursor(cursor);
	int dateIndex = cursor.getColumnIndex(IlmContentProvider.DATE);
	int nPIndex = cursor.getColumnIndex(IlmContentProvider.NPHENOMENON);
	int nTMaxIndex = cursor.getColumnIndex(IlmContentProvider.NTEMPMAX);
	int nTMinIndex = cursor.getColumnIndex(IlmContentProvider.NTEMPMIN);
	int nTxtIndex = cursor.getColumnIndex(IlmContentProvider.NTEXT);
	int dPIndex = cursor.getColumnIndex(IlmContentProvider.DPHENOMENON);
	int dTMaxIndex = cursor.getColumnIndex(IlmContentProvider.DTEMPMAX);
	int dTMinIndex = cursor.getColumnIndex(IlmContentProvider.DTEMPMIN);
	int dTxtIndex = cursor.getColumnIndex(IlmContentProvider.DTEXT);
		
	// laeme andmed contentresolverist mappi ja paneme mapi listi
	while (cursor.moveToNext()) {
		HashMap<String, String> map = new HashMap<String, String>();
		String dbDate =cursor.getString(dateIndex);
		String date =dbDate+ " "+getWeekday(dbDate);
		map.put(KEY_DATE, date);
		map.put(KEY_NPHENOMENON, cursor.getString(nPIndex));
		map.put(KEY_NTEMPMAX, cursor.getString(nTMaxIndex));
		map.put(KEY_NTEMPMIN, cursor.getString(nTMinIndex));
		map.put(KEY_NTEXT, cursor.getString(nTxtIndex));
		map.put(KEY_DPHENOMENON, cursor.getString(dPIndex));
		map.put(KEY_DTEMPMAX, cursor.getString(dTMaxIndex));
		map.put(KEY_DTEMPMIN, cursor.getString(dTMinIndex));
		map.put(KEY_DTEXT, cursor.getString(dTxtIndex));
		 			
		menuItems.add(map);			
		
	}
		return menuItems;
	}
private void addMissing(ArrayList<HashMap<String, String>> menuItems) {
	ArrayList<HashMap<String, String>> returnItems = new ArrayList<HashMap<String, String>>();
	ContentValues values = new ContentValues();
	
	Cursor cursor = getContentResolver()
			.query(IlmContentProvider.CONTENT_URI, null, null,
					null, null);
	startManagingCursor(cursor);
	
	if(cursor.moveToLast()){
	String dbLastDatetxt = cursor.getString(cursor.getColumnIndex(IlmContentProvider.DATE));
	Date dbLastDate =getDate(dbLastDatetxt);
	Log.d("Ilm","dbDate "+dbLastDate.toString());
	Log.d("Ilm","menuitems "+menuItems);
	
	// eemaldame listist need, mis juba on andmebaasis

	for (int i =0; i<menuItems.size();i++) {
		Date xmlDate =getDate(menuItems.get(i).get(KEY_DATE));
		
		// kui xml elemendi kuupäev on sama või väiksem, mis andmebaasi viimase elemendi kuupäev, siis eemaldame selle listist
		if(xmlDate.after(dbLastDate)){
			Log.d("Ilm","add to DB "+xmlDate.toString());
			returnItems.add(menuItems.get(i));
		}
	}
	} else returnItems=menuItems;
		//kirjutame ülejäänud andmebaasi
		for (HashMap<String, String> map : returnItems) {
		values.clear();
		values.put(IlmContentProvider.DATE, map.get(KEY_DATE));
		values.put(IlmContentProvider.NPHENOMENON, map.get(KEY_NPHENOMENON));
		values.put(IlmContentProvider.NTEMPMAX, map.get(KEY_NTEMPMAX));
		values.put(IlmContentProvider.NTEMPMIN, map.get(KEY_NTEMPMIN));
		values.put(IlmContentProvider.NTEXT, map.get(KEY_NTEXT));
		values.put(IlmContentProvider.DPHENOMENON, map.get(KEY_DPHENOMENON));
		values.put(IlmContentProvider.DTEMPMAX, map.get(KEY_DTEMPMAX));
		values.put(IlmContentProvider.DTEMPMIN, map.get(KEY_DTEMPMIN));
		values.put(IlmContentProvider.DTEXT, map.get(KEY_DTEXT));
		getContentResolver().insert(IlmContentProvider.CONTENT_URI, values);
			
		
		}		
	}
private Date getDate(String dateText) {
	try {  
		 
		 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		 return (Date)formatter.parse(dateText); 
		 
	  } catch (ParseException e)
	  {Log.d("Ilm","Exception :"+e);  }  
	  return null;
}

private String getWeekday(String dateText) {		
		int weekdayNr=0; 
		Date date = getDate(dateText);  
			 GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
			 calendar.setTime(date);
		weekdayNr =calendar.get(GregorianCalendar.DAY_OF_WEEK);
		 
		 
		 String[] weekdays = new DateFormatSymbols().getWeekdays();		
		return weekdays[weekdayNr];
	}
private boolean isNetworkConnected() {
	  ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	  NetworkInfo ni = cm.getActiveNetworkInfo();
	  if(ni!=null){
	  if (ni.isConnected()) {	 
	   return true;
	   // No connection
	  } else
	   return false;
	 }

else return false;
}
}