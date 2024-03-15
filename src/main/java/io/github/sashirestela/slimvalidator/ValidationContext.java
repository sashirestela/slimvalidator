package io.github.sashirestela.slimvalidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Keeps a record of unaccomplished constraints during the validation of an object.
 */
public class ValidationContext {

    private List<ConstraintViolation> violations;
    private Set<Object> visited;

    public ValidationContext() {
        violations = new ArrayList<>();
        visited = new HashSet<>();
    }

    public boolean isVisited(Object object) {
        return visited.contains(object);
    }

    public void visit(Object object) {
        visited.add(object);
    }

    public void addViolation(ConstraintViolation violation) {
        violations.add(violation);
    }

    public List<ConstraintViolation> getViolations() {
        return Collections.unmodifiableList(violations);
    }

}
