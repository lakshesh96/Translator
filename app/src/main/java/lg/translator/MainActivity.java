package lg.translator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, AdapterView.OnItemSelectedListener {

    Language[] languages = Language.values();
    Locale[] locales = Locale.getAvailableLocales();
    private TextToSpeech tts;
    EditText out;
    private  EditText inp;
    private Button buttonSpeak;
    String translatedText = "",translated = "";
    String s1,allValues[],find;
    private Spinner lang_input_spinner,lang_output_spinner;
    private  static String lang_input = "AUTO_DETECT";
    private static String lang_output = "ENGLISH";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inp = (EditText) findViewById(R.id.et1);
        out = (EditText) findViewById(R.id.et2);
        buttonSpeak = (Button) findViewById(R.id.bt1);

        lang_input_spinner = (Spinner) findViewById(R.id.sp2);
        lang_output_spinner = (Spinner) findViewById(R.id.sp1);
        allValues = GetAllValues();
        loadSpinner1Data();
        //loadSpinner2Data();
        /*lang_input.setOnItemClickListener(this);
        lang_output.setOnItemClickListener(this);*/



        tts = new TextToSpeech(this,this);

        buttonSpeak.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                s1 = inp.getText().toString();


                class bgStuff extends AsyncTask<Void,Void,Void> {

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            translated = translate(s1);
                            System.out.println("Translation at button = " + translated);
                            //
                        } catch (Exception e)
                        {
                            System.out.println(e);
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result)
                    {
                        out.setText(translated);
                        //String spk = out.getText().toString();
                        speakOut(translated);
                        super.onPostExecute(result);
                    }

                }
                new bgStuff().execute();
            }
        });
    }

    @Override
    public void onDestroy()
    {
        if(tts!=null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public String translate(String text) throws Exception
    {
        Translate.setClientId("lakshesh96");
        Translate.setClientSecret("3jrjMsJOBswlh9TQWkU0Xwt0/StJcGwym/Qy8y12CWI=");
        //String translatedText = "";
        //translatedText = Translate.execute(text, Language.FRENCH);
        translatedText = Translate.execute(text, languages[lang_input_spinner.getSelectedItemPosition()],
                                                                            languages[lang_output_spinner.getSelectedItemPosition()]);
        System.out.println("Translation at Translate method = " + translatedText);
        return translatedText;
    }

    @Override
    public void onInit(int status)
    {
        //Locale fin = Locale.;
        find = lang_output_spinner.getSelectedItem().toString();
        /*for(int i=0 ; i<locales.length ; i++)
        {
            if(locales[i].toString() == find) {
                fin = locales[i];
                Toast.makeText(this, fin.toString(), Toast.LENGTH_SHORT).show();
                break;
            }
            else
                continue;
        }*/
        if(status == TextToSpeech.SUCCESS)
        {
            //int result = tts.setLanguage(Locale.GERMAN);
            //int result = tts.setLanguage(locales[lang_output_spinner.getSelectedItemPosition()]);
            int result = tts.setLanguage(Locale.forLanguageTag(find));
            String voice_check = tts.getVoice().toString();
            Toast.makeText(this, voice_check, Toast.LENGTH_SHORT).show();
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                //Log.e("TTs","This lang is not Supported");
                Toast.makeText(getApplicationContext(),"This Lang is not Supported",Toast.LENGTH_SHORT).show();
                speakOut("Sorry, This Language is not Supported.");
            }
            else
            {
                buttonSpeak.setEnabled(true);
                speakOut("Translator Initialized!");
            }
        }
    }

    public void speakOut(String txt)
    {
        try {
            //String toSpeak = translate(inp.getText().toString());
            //out.setText(txt);
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
            System.out.println(txt);
        }catch(Exception e){
            //Toast.makeText(getApplicationContext(),"Not Supported",Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }
    }



    private void loadSpinner1Data()
    {
        ArrayAdapter <String> data1Adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,allValues);
        data1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang_input_spinner.setAdapter(data1Adapter);
        lang_output_spinner.setAdapter(data1Adapter);

    }
    /*private void loadSpinner2Data()
    {
        ArrayAdapter <String> data2Adapter = new ArrayAdapter<String>
                (this,android.R.layout.simple_spinner_item,allValues);
        data2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lang_input_spinner.setAdapter(data2Adapter);
        lang_output_spinner.setAdapter(data2Adapter);
    }*/
    public String[] GetAllValues()
    {
        String lang[] = new String[languages.length];
        for(int i=0 ; i<languages.length ; i++)
        {
            lang[i] = languages[i].name();
        }
        return lang;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        /*lang_input = lang_input_spinner.getItemAtPosition(position).toString();
      lang_output = lang_output_spinner.getItemAtPosition(position).toString();*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
