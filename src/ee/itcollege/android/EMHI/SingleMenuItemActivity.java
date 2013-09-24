package ee.itcollege.android.EMHI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity  extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_list_item);
        setTitle("Hello StackOverflow");

        // getting intent data
        Intent in = getIntent();

        // Get XML values from previous intent
        String date = in.getStringExtra(IlmActivity.KEY_DATE);
        String nphen = in.getStringExtra(IlmActivity.KEY_NPHENOMENON);
        String ntmax = in.getStringExtra(IlmActivity.KEY_NTEMPMAX);
        String ntmin = in.getStringExtra(IlmActivity.KEY_NTEMPMIN);
        String ntext = in.getStringExtra(IlmActivity.KEY_NTEXT);
        String dphen = in.getStringExtra(IlmActivity.KEY_DPHENOMENON);
        String dtmax = in.getStringExtra(IlmActivity.KEY_DTEMPMAX);
        String dtmin = in.getStringExtra(IlmActivity.KEY_DTEMPMIN);
        String dtext = in.getStringExtra(IlmActivity.KEY_DTEXT);
        setTitle(date);
        // Displaying all values on the screen
       // TextView lblDate = (TextView) findViewById(R.id.date);
        TextView lblnPhen = (TextView) findViewById(R.id.nphenomenon);
        TextView lblnTmax = (TextView) findViewById(R.id.ntempmax);
        TextView lblnTmin = (TextView) findViewById(R.id.ntempmin);
        TextView lblnTxt = (TextView) findViewById(R.id.ntext);
        TextView lbldPhen = (TextView) findViewById(R.id.dphenomenon);
        TextView lbldTmax = (TextView) findViewById(R.id.dtempmax);
        TextView lbldTmin = (TextView) findViewById(R.id.dtempmin);
        TextView lbldTxt = (TextView) findViewById(R.id.dtext);

        //lblDate.setText(date);
        lblnPhen.setText(nphen);
        lblnTmax.setText(ntmax);
        lblnTmin.setText(ntmin);
        lblnTxt.setText(ntext);
        lbldPhen.setText(dphen);
        lbldTmax.setText(dtmax);
        lbldTmin.setText(dtmin);
        lbldTxt.setText(dtext);
    }
}
