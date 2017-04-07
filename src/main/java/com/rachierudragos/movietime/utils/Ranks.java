package com.rachierudragos.movietime.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Dragos on 19.01.2017.
 */

public class Ranks {
    public static final Map<Integer, String> achievments;
    public static final Map<Integer, String> ranks;

    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(115, "Ai vazut un film pana la capat");
        map.put(1000, "Daca ai primi un dolar pentru fiecare minut pierdut pe filme, ai avea cel putin 1000 de euro");
        map.put(1440, "Ai pierdut o zi din viata uitandu-te la filme");
        map.put(2000, "Ti-ai dublat suma");
        map.put(5000, "Ori ai prea mult timp liber, ori ai 3 in teza, trecusi de 5000 de minute irosite \"cu cap\"");
        map.put(7000, "De banii primiti ai putea sa iti cumperi un logan");
        map.put(10000, "Deja ai fi strans 10000 de euro, ar trebui sa iti gasesti pe cineva care sa te plateasca pentru asta");
        map.put(7 * 24 * 60, "Ai pierdut o saptamana din viata uitandu-te la filme");
        achievments = Collections.unmodifiableMap(map);
    }

    static {
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(60, "Ce e aia filme?");
        map.put(1000, "Pleb");
        map.put(5000, "Ma duc la cinema de 3 ori pe an");
        map.put(7500, "Nu ies des din casa, dar si cand ies, ies la film");
        map.put(10000, "Stiu actorii mai bine decat imi stiu prietenii");
        ranks = Collections.unmodifiableMap(map);
    }

    public static String getRank(int minutes) {
        int min = 20000;
        String result = "Tu nu esti om";
        for (Map.Entry<Integer, String> i : ranks.entrySet()) {
            if (minutes < i.getKey() && i.getKey()<min) {
                min = i.getKey();
                result = i.getValue();
            }
        }
        return result;
    }

    public static ArrayList<String> getAchievments(int minutes) {
        ArrayList<String> results = new ArrayList<>();
        for (Map.Entry<Integer, String> i : achievments.entrySet()) {
            if (minutes > i.getKey())
                results.add(i.getValue());
        }
        return results;
    }
}
