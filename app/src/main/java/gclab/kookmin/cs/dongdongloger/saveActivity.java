package gclab.kookmin.cs.dongdongloger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class saveActivity extends AppCompatActivity {
    private double lat,longi;
    private EditText editPlace;
    private TextView latText;
    private TextView longiText;
    private Button insertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Intent in = getIntent();
        lat = in.getExtras().getDouble("lat");
        longi = in.getExtras().getDouble("longi");
        editPlace = (EditText)findViewById(R.id.placeText);
        latText = (TextView)findViewById(R.id.latText);
        longiText = (TextView)findViewById(R.id.longiText);
        latText.setText(Double.toString(lat));
        longiText.setText(Double.toString(longi));
        insertButton = (Button)findViewById(R.id.saveButton);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String place = editPlace.getText().toString();
                insertToDB(place);
            }
        });
    }
    private void insertToDB(String place)
    {
        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            protected void onPreExecute(){
                super.onPreExecute();
                loading = ProgressDialog.show(saveActivity.this,"wait",null,true,true);
            }
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }
            protected String doInBackground(String... params){
                try{
                    String place = (String)params[0];

                    String link = "http://52.79.159.37/addLocation.php";
                    String data = URLEncoder.encode("place","UTF-8") + "=" +URLEncoder.encode(place, "UTF-8");
                    data += "&" + URLEncoder.encode("lat","UTF-8") + "=" +URLEncoder.encode(Double.toString(lat), "UTF-8");
                    data += "&" + URLEncoder.encode("longi","UTF-8") + "=" +URLEncoder.encode(Double.toString(longi), "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader( new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line =null;
                    while((line = reader.readLine())!=null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }catch (Exception e)
                {

                }
                return null;
            }
        }
        InsertData task = new InsertData();
        task.execute(place);
    }
}
