package pl.przemek.validation.Utils;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.*;


@RunWith(JUnitParamsRunner.class)
public class WordValidatorUtilsTest {

    public Object[] oneWord(){
        return $("test", " test", " test ","test_abcd","test.abcd","test\\,abcd");
    }
    @Test
    @Parameters(method = "oneWord")
    public void shouldReturnFalseifWordDoNotConsistOfMinimumTwoWords(String word) throws Exception {
        assertFalse(WordValidatorUtils.ifContentConsistOfMinimumTwoWords(word));
    }

    public Object[] moreThanTwoWords(){
        return $("test test", "abcd test", "test abcd dcba","testt_test abcd","test\\, test\\, test\\, test");
    }
    @Test
    @Parameters(method = "moreThanTwoWords")
    public void ifWordContainsNonWordCharacterInside(String words) throws Exception {
        assertTrue(WordValidatorUtils.ifContentConsistOfMinimumTwoWords(words));
    }

    public Object[] nonWordCharacterInsideWord(){
        return $(new Object[]{"te.st",new String[]{"te","st"}},
                new Object[]{"abc*def",new String[]{"abc","def"}},
                new Object[]{"tes,stt",new String[]{"tes","stt"}},
                new Object[]{"bbb*gggg",new String[]{"bbb","gggg"}},
                new Object[]{"przemek%przemek",new String[]{"przemek","przemek"}});
    }
    @Test
    @Parameters(method = "nonWordCharacterInsideWord")
    public void shouldReturnTrueIfInsideWordIsNonWordCharacter(String word, String[] afterSplit) throws Exception {
        assertTrue(WordValidatorUtils.ifWordContainsNonWordCharacterInside(word));
    }

    @Test
    @Parameters(method = "nonWordCharacterInsideWord")
    public void shouldSplitContentByNonWordCharacter(String word, String[] afterSplit) throws Exception {
        assertEquals(Arrays.asList(afterSplit),WordValidatorUtils.splitWordByNonWordCharacter(word));
    }

}