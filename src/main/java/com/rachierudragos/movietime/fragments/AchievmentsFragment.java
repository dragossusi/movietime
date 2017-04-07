package com.rachierudragos.movietime.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rachierudragos.movietime.MainActivity;
import com.rachierudragos.movietime.R;
import com.rachierudragos.movietime.utils.Ranks;

import java.util.ArrayList;

/**
 * Created by Dragos on 21.01.2017.
 */

public class AchievmentsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.hide();
        View rootView = inflater.inflate(R.layout.fragment_achievements, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_achieve);
        ArrayList<String> achievements = Ranks.getAchievments(((MainActivity) getActivity()).getMinutes());
        listView.setAdapter(new ArrayAdapter(getActivity(),R.layout.item_achieve,achievements));
        return rootView;
    }
}
