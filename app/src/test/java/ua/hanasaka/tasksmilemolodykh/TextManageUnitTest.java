package ua.hanasaka.tasksmilemolodykh;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * testing work with reg exp
 */
public class TextManageUnitTest {

    @Test
    public void ifNameFind() throws Exception {
        String test = "@name bla bla bla @name2";
        assertEquals(2, findNamesInText(test));
    }

    /**
     * find matches in text
     *
     * @param init init string
     * @return count of matches
     */
    private int findNamesInText(String init) {
        int count = 0;
        Pattern p = Pattern.compile("@(\\w+)");
        Matcher m = p.matcher(init);
        while (m.find()) {
            count++;
        }
        return count;
    }
}