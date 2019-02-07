package ba.ito.assistance.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;


public class Resource<T> {
    public DateTime UpdatedAt;
    public T data;
}