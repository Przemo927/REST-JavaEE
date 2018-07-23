package pl.przemek.validation.Utils;


import pl.przemek.model.EnumRegex;

import java.util.Arrays;
import java.util.List;

public class WordValidatorUtils {
    public static boolean ifContentConsistOfMinimumTwoWords(String content){
        return content.matches(EnumRegex.MINIMUM_TWO_WORDS.getRegex());
    }
    public static boolean ifWordContainsNonWordCharacterInside(String content){
        return content.matches(EnumRegex.NON_WORD_CHARACTER_INSIDE_WORD.getRegex());
    }
    public static List<String> splitContentToDistinctWordsByWhiteSpaces(String content){
        return Arrays.asList(content.split(EnumRegex.WHITE_SPACES.getRegex()));
    }
    public static List<String> splitWordByNonWordCharacter(String word){
        return Arrays.asList(word.split(EnumRegex.NON_WORD_CHARACTER.getRegex()));
    }
    public static String deleteLastCharcterIfIsNonWordCharacter(String word){
        String lastCharacter= String.valueOf(word.charAt(word.length()-1));
        if(lastCharacter.matches(EnumRegex.NON_WORD_CHARACTER.getRegex()))
            word=word.substring(0,word.length()-1);
        return word;
    }

}
