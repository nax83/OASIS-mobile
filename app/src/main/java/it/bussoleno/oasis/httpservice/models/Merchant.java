package it.bussoleno.oasis.httpservice.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Alessandro on 31/05/2014.
 */
public class Merchant {

    public static final String PLACE_OWNER = "place_owner";
    public static final String CONCESSION_NUMBER = "concession_number";
    public static final String AUTHORIZATION_NUMBER ="authorization_number";
    public static final String DESCRIPTION = "description";

    public String description;
    public String name;
    public int id;
    public int past_presences;
    public ArrayList<Detail> details;


    public String getValue(String kind){

        if(kind == null || "".equals(kind))
            return null;

        for(Detail detail : details){
            if(kind.equals(detail.kind)){
                Log.d("TAG", kind + " " + detail.value);
                return detail.value;
            }
        }
        return null;
    }


}
