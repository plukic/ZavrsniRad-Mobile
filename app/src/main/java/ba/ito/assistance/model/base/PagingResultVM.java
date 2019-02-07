package ba.ito.assistance.model.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagingResultVM<T> {
    @SerializedName("TotalItems")
    public int TotalItems;
    @SerializedName("CurrentPage")
    public int CurrentPage;
    @SerializedName("PageSize")
    public int PageSize;
    @SerializedName("TotalPages")
    public int TotalPages;
    @SerializedName("StartPage")
    public int StartPage;
    @SerializedName("EndPage")
    public int EndPage;
    @SerializedName("Result")
    public List<T> Result;
}
