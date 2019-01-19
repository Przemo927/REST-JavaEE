package pl.przemek.validation;

import pl.przemek.repository.JpaDiscoveryRepository;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class URLUniqueValidator implements ConstraintValidator<URLUnique, String> {

	@Inject
	JpaDiscoveryRepository discrepo;
	@Override
	public void initialize(URLUnique arg0) {

	}

	@Override
	public boolean isValid(String url, ConstraintValidatorContext arg1) {
		return discrepo.checkPresenceDiscveryByUrl(url);
	}


}
