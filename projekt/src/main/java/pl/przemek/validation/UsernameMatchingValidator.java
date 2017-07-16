package pl.przemek.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameMatchingValidator implements ConstraintValidator<UsernameMatching, String> {

	@Override
	public void initialize(UsernameMatching arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext arg1) {
		return Character.isUpperCase(password.charAt(0));
	}


}
