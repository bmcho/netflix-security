
package com.bmcho.netflix.config.audit;

import java.util.Optional;

public interface RequestedByProvider {
    Optional<String> getRequestedBy();
}