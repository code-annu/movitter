package developer.anurag.movitter.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtil {
    public static List<String> getListOfRandomStringsFrom(List<String> list,int expectedSize){
        Set<String> randomSet=new HashSet<>();
        for(int i=0;i<expectedSize;i++){
            randomSet.add(list.get((int)(Math.random()*list.size())));
        }
        return new ArrayList<>(randomSet);
    }

}
