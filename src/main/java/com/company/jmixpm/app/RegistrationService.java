package com.company.jmixpm.app;

import com.company.jmixpm.entity.User;
import io.jmix.email.EmailException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RegistrationService {

    /**
     * @return true if user with this email (or login) already exists.
     */
    public boolean checkUserAlreadyExist(String email) {
        // todo check that user already exists
        return false;
    }

    public User registerNewUser(String email, String firstName, String lastName) {
        // todo register new user
        return null;
    }

    public String generateRandomActivationToken() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;

        ThreadLocalRandom current = ThreadLocalRandom.current();
        String generatedString = current.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    public void saveActivationToken(User user, String activationToken) {
        // todo save user activation token
    }

    public void sendActivationEmail(User user) throws EmailException {
        // todo send activation email
    }

    @Nullable
    public User loadUserByActivationToken(String token) {
        // todo load user by activation token
        return null;
    }

    public void activateUser(User user, String password) {
        // todo activate user
        // todo set password
        // todo assign roles
    }
}