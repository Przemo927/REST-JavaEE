package pl.przemek.validation;

import pl.przemek.repository.UserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

	@Inject
	UserRepository userrepo;
	@Override
	public void initialize(UsernameUnique arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext arg1) {
		return userrepo.checkPresenceOfUserByUsername(username);
	}


}
