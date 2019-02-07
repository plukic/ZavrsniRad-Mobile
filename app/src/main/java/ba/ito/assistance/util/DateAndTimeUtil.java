package ba.ito.assistance.util;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

public class DateAndTimeUtil {

    @Inject
    public DateAndTimeUtil() {

    }

    public String FormatForRoadConditions(DateTime dateFrom, DateTime dateTo){

        Interval today = new Interval( dateFrom.withTimeAtStartOfDay(), dateFrom.plusDays(1).withTimeAtStartOfDay() );

        if(today.contains(dateTo)){
            return getTimeSpanString(dateFrom,dateTo);
        }
        else if(dateFrom.monthOfYear().get()==dateTo.monthOfYear().get()){
            return getDateTimeSpanOnSameMonth(dateFrom,dateTo);
        }else{
            return getDateTimeSpanOnDiffMonths(dateFrom,dateTo);
        }
    }

    private String getDateTimeSpanOnDiffMonths(DateTime dateFrom, DateTime dateTo) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM");
        return dtf.print(dateFrom) + " - " + dtf.print(dateTo);
    }

    private String getDateTimeSpanOnSameMonth(DateTime dateFrom, DateTime dateTo) {
        DateTimeFormatter dtfDateDay = DateTimeFormat.forPattern("dd");
        DateTimeFormatter dtfDate = DateTimeFormat.forPattern("dd.MM");
        DateTimeFormatter dtfHours = DateTimeFormat.forPattern("HH:mm");
        return dtfDateDay.print(dateFrom)+"-" +dtfDate.print(dateTo)+" "+ dtfHours.print(dateFrom) + " - " + dtfHours.print(dateTo);
    }

    private String getTimeSpanString(DateTime dateFrom, DateTime dateTo) {
        DateTimeFormatter dtfHours = DateTimeFormat.forPattern("HH:mm");
        return dtfHours.print(dateFrom) + " - " + dtfHours.print(dateTo);
    }

    public String FormatForLastUpdateSubtitle(DateTime validUntil) {
        if(validUntil==null)
            return "-";

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy.'g'. 'u' HH:mm");
        return dtf.print(validUntil);
    }

    public String FormatForGasCompanyUpdate(DateTime updateAt) {

        DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM");
        return dtf.print(updateAt);
    }

    public String FormatAccountActiveTo(DateTime accountActiveTo) {
        if(accountActiveTo==null)
            return "-";

        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/yyyy");
        return dtf.print(accountActiveTo);    }
}
