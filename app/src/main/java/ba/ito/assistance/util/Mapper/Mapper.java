package ba.ito.assistance.util.Mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class Mapper<Source, Dest> {


    public abstract Dest map(Source value);

    public abstract Source reverseMap(Dest value);

    public List<Dest> map(List<Source> values) {
        List<Dest> returnValues = new ArrayList<>(values.size());
        for (Source value : values) {
            returnValues.add(map(value));
        }
        return returnValues;
    }

    public List<Source> reverseMap(List<Dest> values) {
        List<Source> returnValues = new ArrayList<>(values.size());
        for (Dest value : values) {
            returnValues.add(reverseMap(value));
        }
        return returnValues;
    }
}
