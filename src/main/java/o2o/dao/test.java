package o2o.dao;

import java.util.*;

public class test {
    
    public static void main(String[] args){

        String[] strings = {"321", "3", "32","322","2"};
        List<String> strList = Arrays.asList(strings);

        Collections.sort(strList, (o1, o2) -> (o1+o2).compareTo(o2+o1));

        System.out.println(strList);
    }





}
