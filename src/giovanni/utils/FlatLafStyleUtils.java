package giovanni.utils;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import javax.swing.JComponent;

public class FlatLafStyleUtils {

    public static String getStyleValue(JComponent component, String key, String defaultValue) {
        Map<String, String> styleMap = styleToMap((String) FlatClientProperties.clientProperty(component, "FlatLaf.style", null, String.class));
        return styleMap.getOrDefault(key, defaultValue);
    }

    public static void appendStyleIfAbsent(JComponent component, String style) {
        String oldStyle = (String) FlatClientProperties.clientProperty(component, "FlatLaf.style", null, String.class);
        String styles = appendStyleIfAbsent(oldStyle, style);
        component.putClientProperty("FlatLaf.style", styles);
    }

    public static String appendStyle(String oldStyle, String newStyle) {
        Map<String, String> oldStyleMap = styleToMap(oldStyle);
        Map<String, String> newStyleMap = styleToMap(newStyle);
        oldStyleMap.putAll(newStyleMap);
        return mapToString(oldStyleMap);
    }

    public static String appendStyleIfAbsent(String oldStyle, String newStyle) {
        Map<String, String> oldStyleMap = styleToMap(oldStyle);
        Map<String, String> newStyleMap = styleToMap(newStyle);
        newStyleMap.putAll(oldStyleMap);
        return mapToString(newStyleMap);
    }

    public static Map<String, String> styleToMap(String style) {
        Map<String, String> mapStyle = new HashMap<>();
        if (style != null) {
            String[] styles = style.split(";");
            for (String s : styles) {
                String[] parts = s.split(":");
                if (parts.length == 2) {
                    mapStyle.put(parts[0], parts[1]);
                }
            }
        }
        return mapStyle;
    }

    public static String mapToString(Map<String, String> mapStyle) {
        StringJoiner joiner = new StringJoiner(";");
        for (Map.Entry<String, String> entry : mapStyle.entrySet()) {
            joiner.add((String) entry.getKey() + ":" + (String) entry.getValue());
        }
        return joiner.toString();
    }
}
