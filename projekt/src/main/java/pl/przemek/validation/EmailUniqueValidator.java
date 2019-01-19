package pl.przemek.validation;

import pl.przemek.repository.JpaUserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

	@Inject
	JpaUserRepository userrepo;
	@Override
	public void initialize(EmailUnique arg0) {

	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext arg1) {
		return userrepo.checkPresenceOfEmail(email);
	}


}
