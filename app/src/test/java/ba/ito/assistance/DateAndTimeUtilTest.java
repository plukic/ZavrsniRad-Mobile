package ba.ito.assistance;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ba.ito.assistance.util.DateAndTimeUtil;

@RunWith(JUnit4.class)
public class DateAndTimeUtilTest {

    private DateAndTimeUtil dateAndTimeUtil;

    @Before
    public void setUp() throws Exception {
        dateAndTimeUtil = new DateAndTimeUtil();
    }

    @Test
    public void shouldDisplayCorrectRoadConditionWithSameDay(){
        DateTime start = DateTime.now().withTimeAtStartOfDay();
        DateTime end = start.plusHours(3).plusMinutes(30);

        String expected = "00:00 - 03:30";
        String result = dateAndTimeUtil.FormatForRoadConditions(start,end);

        Assert.assertEquals(expected,result);
    }

    @Test
    public void shouldDisplayCorrectRoadConditionWithDiffDaySameMonth(){
        DateTime start = DateTime.parse("2017-01-15T13:00:00.000");
        DateTime end = start.plusDays(1).plusMinutes(30);
        String expected = "15-16.01 13:00 - 13:30";
        String result = dateAndTimeUtil.FormatForRoadConditions(start,end);

        Assert.assertEquals(expected,result);
    }

    @Test
    public void shouldDisplayCorrectRoadConditionWithDiffDayAndMonth(){
        DateTime start = DateTime.parse("2017-01-15T13:00:00.000");
        DateTime end = start.plusMonths(1);
        String expected = "15.01 - 15.02";
        String result = dateAndTimeUtil.FormatForRoadConditions(start,end);

        Assert.assertEquals(expected,result);
    }

    @Test
    public void shouldDisplayCorrectRoadConditionLastUpdateTime(){
        DateTime start = DateTime.parse("2017-01-15T13:00:00.000");
        String expected = "15.01.2017.g. u 13:00";
        String result = dateAndTimeUtil.FormatForLastUpdateSubtitle(start);

        Assert.assertEquals(expected,result);
    }

}
