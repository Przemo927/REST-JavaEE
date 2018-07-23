package pl.przemek.validation;

import pl.przemek.Store.ForbiddenWordsStore;
import pl.przemek.Utils.PropertiesFileUtils;
import pl.przemek.model.EnumRegex;
import pl.przemek.model.ForbiddenWords;
import pl.przemek.validation.Utils.WordValidatorUtils;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;


public class ForbiddenWordValidator implements ConstraintValidator<ForbiddenWord, String> {

    @Inject
    private ForbiddenWordsStore wordStore;
    private String forbiddenWords="";
    private static String directory="/ValidationMessages.properties";
    @Override
    public void initialize(ForbiddenWord forbiddenWord) {
    }

    @Override
    public boolean isValid(String comment, ConstraintValidatorContext context) {
        if(WordValidatorUtils.ifContentConsistOfMinimumTwoWords(comment)){
            List<String> splittedComment = WordValidatorUtils.splitContentToDistinctWordsByWhiteSpaces(comment);
            splittedComment.forEach(this::checkWord);
        } else {
            comment=comment.replaceAll(EnumRegex.WHITE_SPACES.getRegex(),"");
            checkWord(comment);
        }
        if(!"".equals(forbiddenWords)) {
            String message = PropertiesFileUtils.getValue(directory,"pl.przemek.validation.ForbiddenWord.message").replace("{words}", forbiddenWords);
            buildNewContraintViolationTemplate(context, message);
            forbiddenWords="";
            return false;
        }
        return true;
    }
    private void checkWord(String word){
        if(WordValidatorUtils.ifWordContainsNonWordCharacterInside(word)) {
            checkIfWordWithNonWordCharacterInsideIsForbidden(word);
        }else{
            if(checkIfWordIsForbidden(word))
                addWordToForbiddenList(word);
        }
    }
    private void checkIfWordWithNonWordCharacterInsideIsForbidden(String word){
        if (checkIfWordIsForbidden(word))
            addWordToForbiddenList(word);
        else{
            List<String>splittedWords=WordValidatorUtils.splitWordByNonWordCharacter(word);
            for(String splittedWord:splittedWords){
                if(checkIfWordIsForbidden(splittedWord))
                    addWordToForbiddenList(splittedWord);
            }
        }
    }
    private boolean checkIfWordIsForbidden(String word){
        for(ForbiddenWords forbiddenWord:wordStore.getListOfForbiddenWords()){
            if(word.matches(forbiddenWord.getWord())){
                return true;
            }
        }
        return false;
    }
    private void addWordToForbiddenList(String word){
        word=WordValidatorUtils.deleteLastCharcterIfIsNonWordCharacter(word);
        if(!forbiddenWords.contains(word)) {
            if ("".equals(forbiddenWords))
                forbiddenWords += word;
            else forbiddenWords += ", " + word;
        }
    }
    private void buildNewContraintViolationTemplate(ConstraintValidatorContext context, String message){
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
