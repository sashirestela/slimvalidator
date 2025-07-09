package io.github.sashirestela.slimvalidator;

import io.github.sashirestela.slimvalidator.metadata.ClassMetadata.AnnotationMetadata;
import io.github.sashirestela.slimvalidator.util.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Detail of every constraint violation.
 */
public class ConstraintViolation {

    private final Object value;
    private final String name;
    private final AnnotationMetadata annotationMetadata;
    private final String message;

    public ConstraintViolation(Object value, String name, AnnotationMetadata annotationMetadata, String message) {
        this.value = value;
        this.name = name;
        this.annotationMetadata = annotationMetadata;
        this.message = message;
    }

    public Object getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    /**
     * Prepare the violation message using the constraint's message field as template and replacing
     * placeholders with the other constraint's fields.
     * 
     * @return The concrete violation message.
     */
    public String getMessage() {
        if (this.message != null && !message.isBlank()) {
            return this.message;
        }
        final String TMPL_LOOP = "#for(";
        final String TMPL_CONDITION = "#if(";
        var values = annotationMetadata.getValuesByAnnotMethod();
        var statedMessage = values.get("message").toString();
        if (statedMessage.contains(TMPL_LOOP)) {
            return replaceLoop(statedMessage, getSubMessages(annotationMetadata));
        } else if (statedMessage.contains(TMPL_CONDITION)) {
            return replaceCondition(statedMessage, values);
        } else {
            return replaceValues(statedMessage, values);
        }
    }

    private List<String> getSubMessages(AnnotationMetadata annotationMetadata) {
        List<String> subMessages = new ArrayList<>();
        var subAnnotations = annotationMetadata.getSubAnnotations();
        for (var subAnnotation : subAnnotations) {
            var subViolation = new ConstraintViolation(null, null, subAnnotation, null);
            var subMessage = subViolation.getMessage();
            subMessages.add(subMessage);
        }
        return subMessages;
    }

    private String replaceLoop(String message, List<String> subMessages) {
        final String REGEX_LOOP = "#for\\(([^\\)]+)\\)([^#]+)#endfor";
        final int LOOP_BODY = 2;
        var pattern = Pattern.compile(REGEX_LOOP);
        var matcher = pattern.matcher(message);
        return matcher.replaceFirst(mr -> {
            var replacement = new StringBuilder();
            for (String subMessage : subMessages) {
                var values = Map.of("message", (Object) subMessage);
                replacement.append(replaceValues(mr.group(LOOP_BODY), values));
            }
            return replacement.toString();
        });
    }

    private String replaceCondition(String message, Map<String, Object> values) {
        final String REGEX_CONDITION = "#if\\(([^\\)]+)\\)([^#]+)#endif";
        final int VARIABLE_NAME = 1;
        final int CONDITION_BODY = 2;
        var pattern = Pattern.compile(REGEX_CONDITION);
        var matcher = pattern.matcher(message);
        var newMessage = matcher.replaceAll(mr -> {
            var replacement = "";
            var object = values.get(mr.group(VARIABLE_NAME));
            if (Common.existsByAnnotMethodType(object)) {
                replacement = mr.group(CONDITION_BODY);
            }
            return replacement;
        });
        return replaceValues(newMessage, values);
    }

    private String replaceValues(String message, Map<String, Object> values) {
        final String REGEX_VARIABLE = "\\{([^}]+)\\}";
        final int VARIABLE_NAME = 1;
        var pattern = Pattern.compile(REGEX_VARIABLE);
        var matcher = pattern.matcher(message);
        return matcher.replaceAll(mr -> {
            var object = values.get(mr.group(VARIABLE_NAME));
            return Common.toStringByAnnotMethodType(object);
        });
    }

}
