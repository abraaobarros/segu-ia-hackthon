package segu.segu;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by abraao on 10/12/2017.
 */

public class IAReaderStream {
    public ArrayList<Dado> read(Context context){
        ArrayList<Dado> dados = new ArrayList<>();
        InputStream is = context.getResources().openRawResource(R.raw.ai);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8")));
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(";");
                dados.add(new Dado(tokens));
                Log.d("MainActivity" ,"Just Created " +tokens);
            }
        } catch (IOException e1) {
            Log.e("MainActivity", "Error" + line, e1);
            e1.printStackTrace();
        }

        return dados;

    }

}
