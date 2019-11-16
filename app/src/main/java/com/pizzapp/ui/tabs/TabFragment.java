package com.pizzapp.ui.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pizzapp.R;
import com.pizzapp.model.Database;
import com.pizzapp.utilities.IO;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class TabFragment extends Fragment {

    int position;
    private TextView textView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputStream jsonDatabaseInputStream = getResources().openRawResource(R.raw.database);
        Database jsonDb = new Database();
        try {
            jsonDb = IO.jsonFileToDatabase(jsonDatabaseInputStream);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textView = (TextView) view.findViewById(R.id.textView);
        switch (position) {
            case 0:
                textView.setText(jsonDb.getSizes().get(0).getName() + " / " + jsonDb.getSizes().get(1).getName() + " / " + jsonDb.getSizes().get(2).getName());
                break;
            case 1:
                textView.setText("My Pizza");
                break;
            case 2:
                textView.setText(jsonDb.getCrusts().get(0).getName() + " / " + jsonDb.getCrusts().get(1).getName() + " / " + jsonDb.getCrusts().get(2).getName());
                break;
        }


    }
}