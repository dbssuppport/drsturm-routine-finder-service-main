package com.e2x.bigcommerce.routinefinder.cli.mindmup;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static com.e2x.bigcommerce.routinefinder.enquiry.TextUtils.*;

@Getter
public class MindMupJsonNode {
    @Setter
    private long id;
    private final MindMupJsonNode parentNode;
    private final Map<String, String> attributes = Maps.newHashMap();

    public MindMupJsonNode(long id) {
        this(id, null);
    }

    public MindMupJsonNode(long id, MindMupJsonNode parentNode) {
        this.id = id;
        this.parentNode = parentNode;
    }

    public String getTitle() {
        return attributes.get("title");
    }

    public void addAttribute(String key, String value) {
        var before = trim(replaceWhitespace(value));
        String after;
        while (!(after = removeDuplicateWhitespace(before)).equals(before)) {
            before = after;
        };

        attributes.put(key, trim(removeDuplicateWhitespace(replaceWhitespace(after))));
    }

    @Override
    public String toString() {
        if (!Strings.isNullOrEmpty(getTitle())) {
            if (parentNode != null) {
                return "Node(title=" + getTitle() + ") -> " + parentNode;
            } else {
                return "Node(title=" + getTitle() + ")";
            }
        } else {
            return "Node(attributes=" + attributes + ") -> " + parentNode;
        }
    }
}
