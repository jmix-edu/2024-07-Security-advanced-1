package com.company.jmixpm.app;

import com.company.jmixpm.entity.User;
import com.company.jmixpm.security.DeveloperRole;
import com.company.jmixpm.security.DeveloperRowLevelRole;
import io.jmix.core.UnconstrainedDataManager;
import io.jmix.email.EmailException;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import io.jmix.security.role.assignment.RoleAssignmentRoleType;
import io.jmix.securitydata.entity.RoleAssignmentEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RegistrationService {

    private final UnconstrainedDataManager unconstrainedDataManager;
    private final Emailer emailer;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UnconstrainedDataManager unconstrainedDataManager, Emailer emailer, PasswordEncoder passwordEncoder) {
        this.unconstrainedDataManager = unconstrainedDataManager;
        this.emailer = emailer;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @return true if user with this email (or login) already exists.
     */
    public boolean checkUserAlreadyExist(String email) {
        List<User> list = unconstrainedDataManager.load(User.class)
                .query("select u from User u where u.email = :email" +
                        " or u.username = :email")
                .parameter("email", email)
                .list();
        return !list.isEmpty();
    }

    public User registerNewUser(String email, String firstName, String lastName) {
        User user = unconstrainedDataManager.create(User.class);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setActive(false);
        user.setNeedsActivation(true);

        unconstrainedDataManager.save(user);

        return user;
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
        user = unconstrainedDataManager.load(User.class)
                .id(user.getId())
                .one();

        user.setActivationToken(activationToken);

        unconstrainedDataManager.save(user);
    }

    public void sendActivationEmail(User user) throws EmailException {
        user = unconstrainedDataManager.load(User.class)
                .id(user.getId())
                .one();

        String activationLink = "http://localhost:8080/activate?token=" +
                user.getActivationToken();

        String subject = "Jmix PM activation";
        String body = String.format("Hello, %s %s \n Please finish your Registration \n Activation link: %s",
                user.getFirstName(),
                user.getLastName(),
                activationLink);

        emailer.sendEmail(
                EmailInfoBuilder.create()
                        .setFrom("jmixpm@example.com")
                        .setAddresses(user.getEmail())
                        .setSubject(subject)
                        .setBody(body)
                        .build()
        );

    }


    @Nullable
    public User loadUserByActivationToken(String token) {
        return unconstrainedDataManager.load(User.class)
                .query("select u from User u " +
                        "where u.needsActivation = true " +
                        "and u.activationToken = :token")
                .parameter("token", token)
                .optional()
                .orElse(null);
    }

    public void activateUser(User user, String password) {
        // todo activate user
        // todo set password
        // todo assign roles

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setActivationToken(null);
        user.setNeedsActivation(false);
        user.setActive(true);

        RoleAssignmentEntity assignment1 = unconstrainedDataManager.create(RoleAssignmentEntity.class);
        assignment1.setUsername(user.getUsername());
        assignment1.setRoleType(RoleAssignmentRoleType.RESOURCE);
        assignment1.setRoleCode(DeveloperRole.CODE);

        RoleAssignmentEntity assignment2 = unconstrainedDataManager.create(RoleAssignmentEntity.class);
        assignment2.setUsername(user.getUsername());
        assignment2.setRoleType(RoleAssignmentRoleType.ROW_LEVEL);
        assignment2.setRoleCode(DeveloperRowLevelRole.CODE);

        unconstrainedDataManager.save(user, assignment1, assignment2);
    }
}