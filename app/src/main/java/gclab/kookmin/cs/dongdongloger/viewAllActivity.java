package gclab.kookmin.cs.dongdongloger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class viewAllActivity extends Activity {


    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_PLACE = "place";
    private static final String TAG_LAT = "lat";
    private static final String TAG_LONGI ="longi";

    JSONArray places = null;

    ArrayList<HashMap<String, String>> placeList;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all2);
        list = (ListView) findViewById(R.id.placeView);
        placeList = new ArrayList<HashMap<String,String>>();
        getData("http://52.79.159.37/viewAll.php");
    }


    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            places = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<places.length();i++){
                JSONObject c = places.getJSONObject(i);
                String id = c.getString(TAG_PLACE);
                String name = c.getString(TAG_LAT);
                String address = c.getString(TAG_LONGI);

                HashMap<String,String> places = new HashMap<String,String>();

                places.put(TAG_PLACE,id);
                places.put(TAG_LAT,name);
                places.put(TAG_LONGI,address);

                placeList.add(places);
            }

            ListAdapter adapter = new SimpleAdapter(
                    viewAllActivity.this, placeList, R.layout.list_item,
                    new String[]{TAG_PLACE,TAG_LAT,TAG_LONGI},
                    new int[]{R.id.id, R.id.name, R.id.address}
            );
            list.setAdapter(adapter);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent mapIntent = new Intent(getApplicationContext(),getOwnLocationActivity.class);
                    mapIntent.putExtra("longi",Double.valueOf(placeList.get(i).get(TAG_LONGI)).doubleValue());
                    mapIntent.putExtra("lat",Double.valueOf(placeList.get(i).get(TAG_LAT)).doubleValue());
                    startActivity(mapIntent);

                }
            };
            list.setOnItemClickListener(listener);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(viewAllActivity.this, "wait", null, true, true);
            }

            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                myJSON = s;
                showList();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    myJSON = sb.toString().trim();
                    return myJSON;
                } catch (java.net.MalformedURLException e) {
                    return null;

                } catch (java.io.IOException e) {
                    return null;
                }
            }

        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

}