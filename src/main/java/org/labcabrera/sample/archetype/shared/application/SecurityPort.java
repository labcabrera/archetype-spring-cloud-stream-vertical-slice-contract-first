package org.labcabrera.sample.archetype.shared.application;

import java.util.Optional;
import java.util.Set;

public interface SecurityPort {

    Optional<AuthenticatedUser> currentUser();

    default AuthenticatedUser requireCurrentUser() {
        return currentUser().orElseThrow(() -> new SecurityException("Unauthenticated"));
    }

    public record AuthenticatedUser(String id, String username, Set<String> roles, Set<String> scopes) {

        public static final String ROLE_CASE_FOLDER_MANAGEMENT = "case-folder-management";
        public static final String ROLE_CASE_FOLDER_VIEW = "case-folder-view";

        public boolean hasRole(String role) {
            return roles.contains(role);
        }

        public void checkRole(String role) {
            if (!roles.contains(role)) {
                throw new SecurityException("User " + username + " does not have role: " + role);
            }
        }

        public void checkAnyRole(Set<String> requiredRoles) {
            for (String role : requiredRoles) {
                if (roles.contains(role)) {
                    return;
                }
            }
            throw new SecurityException("User " + username + " does not have any of the required roles: " + requiredRoles);
        }
    }
}
