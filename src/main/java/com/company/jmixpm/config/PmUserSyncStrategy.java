package com.company.jmixpm.config;

import com.company.jmixpm.entity.User;
import io.jmix.ldap.userdetails.AbstractLdapUserDetailsSynchronizationStrategy;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.stereotype.Component;

@Component
public class PmUserSyncStrategy extends AbstractLdapUserDetailsSynchronizationStrategy<User> {

    @Override
    protected Class getUserClass() {
        return User.class;
    }

    @Override
    protected void mapUserDetailsAttributes(User userDetails, DirContextOperations ctx) {
        userDetails.setFirstName(ctx.getStringAttribute("givenName"));
        userDetails.setLastName(ctx.getStringAttribute("sn"));
        userDetails.setEmail(ctx.getStringAttribute("mail"));
        // set another attributes

    }
}
