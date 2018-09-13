package pl.przemek.timer;

import pl.przemek.repository.JpaUserRepository;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class ChangeUserAsInactive {

    @Inject
    private JpaUserRepository userRepository;

    @Schedule(second = "0", minute = "0", hour = "0")
    public void ChangeUserAsInactiveIfLastLoginLaterThan365Days() {
        userRepository.setInactiveIfLasLoginLAterThan365Days();
    }
}
