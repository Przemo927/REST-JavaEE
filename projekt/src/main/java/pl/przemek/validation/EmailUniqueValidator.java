package pl.przemek.validation;

import pl.przemek.repository.DiscoveryRepository;
import pl.przemek.repository.UserRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

	@Inject
	UserRepository userrepo;
	@Override
	public void initialize(EmailUnique arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext arg1) {
		return userrepo.checkPresenceOfEmail(email);
	}


}
