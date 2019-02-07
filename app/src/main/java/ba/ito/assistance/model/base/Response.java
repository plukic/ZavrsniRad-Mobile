package ba.ito.assistance.model.base;

public class Response<T> {
    public boolean IsApiError;
    public boolean IsDbError;

    public T result;
}
