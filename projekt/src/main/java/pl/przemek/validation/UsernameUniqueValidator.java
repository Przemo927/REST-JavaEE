package pl.przemek.validation;

import pl.przemek.repository.JpaUserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

	@Inject
	JpaUserRepository userrepo;
	@Override
	public void initialize(UsernameUnique arg0) {

	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext arg1) {
		return userrepo.checkPresenceOfUserByUsername(username);
	}


}
