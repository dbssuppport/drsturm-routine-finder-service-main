package com.e2x.bigcommerce.routinefindermodel;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Error {
    @NonNull
    private String code;
    private String message;
    private Map<String, String> attributes;

    public Error() { }

    public void addAttribute(String key, String value) {
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        attributes.put(key, value);
    }
}
