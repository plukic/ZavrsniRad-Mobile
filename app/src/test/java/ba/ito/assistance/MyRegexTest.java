package ba.ito.assistance;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ba.ito.assistance.util.MyRegex;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class MyRegexTest {
    private MyRegex myRegex;

    @Before
    public void setUp(){
        myRegex = new MyRegex();
    }
    @Test
    public void PasswordIsValid()
    {
        Assert.assertTrue(myRegex.isValidPassword("Password1!"));
    }

    @Test
    public void PasswordNotValidMissingNumber(){
        Assert.assertFalse(myRegex.isValidPassword("Password"));
    }

    @Test
    public void PasswordNotValidMissingLowerCaseLetter(){
        Assert.assertFalse(myRegex.isValidPassword("PASSWORD1"));
    }

    @Test
    public void PasswordNotValidMissingUpperCaseLetter(){
        Assert.assertFalse(myRegex.isValidPassword("password1"));
    }
    @Test
    public void PasswordNotValidMin7Letters(){
        Assert.assertFalse(myRegex.isValidPassword("Pas12"));
    }

    @Test
    public void PhoneValid(){

        Assert.assertTrue(myRegex.isValidPhoneNumber("+38763111710"));
        Assert.assertTrue(myRegex.isValidPhoneNumber("063103730"));
        Assert.assertTrue(myRegex.isValidPhoneNumber("063103730"));
    }

    @Test
    public void PhoneInvalid(){
        Assert.assertFalse(myRegex.isValidPhoneNumber("-38763103730"));
        Assert.assertFalse(myRegex.isValidPhoneNumber("403730"));
        Assert.assertFalse(myRegex.isValidPhoneNumber("063a103730"));
    }


    @Test
    public void EmailValid(){

        Assert.assertTrue(myRegex.isValidEmail("test@email.com"));
        Assert.assertTrue(myRegex.isValidEmail("eas@asd.com"));
        Assert.assertTrue(myRegex.isValidEmail("ito@ito.ba"));
    }

    @Test
    public void EmailInvalid(){
        Assert.assertFalse(myRegex.isValidEmail("@email.com"));
        Assert.assertFalse(myRegex.isValidEmail("!eas@asd.com"));
        Assert.assertFalse(myRegex.isValidEmail("ito.ba.com"));
    }

}