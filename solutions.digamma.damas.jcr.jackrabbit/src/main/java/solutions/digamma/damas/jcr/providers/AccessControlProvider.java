package solutions.digamma.damas.jcr.providers;

import java.security.Principal;
import java.util.Objects;
import java.util.Set;
import org.apache.jackrabbit.core.security.authorization.acl.ACLProvider;
import solutions.digamma.damas.auth.JaasConfiguration;

public class AccessControlProvider extends ACLProvider {

    @Override
    public boolean isAdminOrSystem(Set<Principal> principals) {
        return principals.stream()
                .filter(Objects::nonNull)
                .map(Principal::getName)
                .filter(Objects::nonNull)
                .anyMatch(JaasConfiguration.SYS_SHADOW::equals);
    }
}
