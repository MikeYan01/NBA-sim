package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized localization system for the NBA simulation.
 * Loads localized strings from JSON files at runtime.
 * Uses native Java without external dependencies.
 */
public class LocalizedStrings {
    
    public enum Language {
        CHINESE("zh_CN"),
        ENGLISH("en_US");
        
        private final String code;
        
        Language(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
    }
    
    private static Language currentLanguage = Language.CHINESE;
    private static Map<String, Object> chineseStrings;
    private static Map<String, Object> englishStrings;
    
    static {
        loadStrings();
    }
    
    /**
     * Load localized strings from JSON files.
     */
    private static void loadStrings() {
        try {
            chineseStrings = loadJsonFile("database/localization/strings_zh_CN.json");
            englishStrings = loadJsonFile("database/localization/strings_en_US.json");
        } catch (IOException e) {
            System.err.println("Error loading localization files: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load a JSON file and return as Map.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> loadJsonFile(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return parseJSON(content);
    }
    
    /**
     * Simple JSON parser for our specific use case.
     * Handles nested objects and strings.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseJSON(String json) {
        json = json.trim();
        if (json.startsWith("{")) {
            return parseObject(json);
        }
        return new HashMap<>();
    }
    
    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseObject(String json) {
        Map<String, Object> map = new HashMap<>();
        json = json.trim().substring(1, json.length() - 1).trim();
        
        int i = 0;
        while (i < json.length()) {
            // Skip whitespace
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
            if (i >= json.length()) break;
            
            // Parse key
            if (json.charAt(i) != '"') break;
            i++; // skip opening quote
            int keyStart = i;
            while (i < json.length() && json.charAt(i) != '"') {
                if (json.charAt(i) == '\\') i++; // skip escaped char
                i++;
            }
            String key = json.substring(keyStart, i);
            i++; // skip closing quote
            
            // Skip whitespace and colon
            while (i < json.length() && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ':')) i++;
            
            // Parse value
            Object value = null;
            if (json.charAt(i) == '{') {
                // Nested object
                int braceCount = 0;
                int start = i;
                do {
                    if (json.charAt(i) == '{') braceCount++;
                    else if (json.charAt(i) == '}') braceCount--;
                    i++;
                } while (braceCount > 0 && i < json.length());
                value = parseObject(json.substring(start, i));
            } else if (json.charAt(i) == '"') {
                // String
                i++; // skip opening quote
                StringBuilder sb = new StringBuilder();
                while (i < json.length() && json.charAt(i) != '"') {
                    if (json.charAt(i) == '\\' && i + 1 < json.length()) {
                        i++;
                        char next = json.charAt(i);
                        if (next == 'n') sb.append('\n');
                        else if (next == 't') sb.append('\t');
                        else if (next == 'r') sb.append('\r');
                        else sb.append(next);
                    } else {
                        sb.append(json.charAt(i));
                    }
                    i++;
                }
                value = sb.toString();
                i++; // skip closing quote
            }
            
            map.put(key, value);
            
            // Skip comma
            while (i < json.length() && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ',')) i++;
        }
        
        return map;
    }
    
    /**
     * Set the current language for the application.
     */
    public static void setLanguage(Language lang) {
        currentLanguage = lang;
    }
    
    /**
     * Get the current language.
     */
    public static Language getLanguage() {
        return currentLanguage;
    }
    
    /**
     * Get the current language (alias for getLanguage).
     */
    public static Language getCurrentLanguage() {
        return currentLanguage;
    }
    
    /**
     * Get a localized string by key path (e.g., "stat.points.short").
     * Returns the key itself if not found (for debugging).
     */
    @SuppressWarnings("unchecked")
    public static String get(String keyPath) {
        Map<String, Object> strings = currentLanguage == Language.CHINESE ? chineseStrings : englishStrings;
        
        if (strings == null) {
            System.err.println("Warning: Localization strings not loaded");
            return "[" + keyPath + "]";
        }
        
        String[] keys = keyPath.split("\\.");
        Object current = strings;
        
        for (int i = 0; i < keys.length; i++) {
            if (current instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) current;
                if (map.containsKey(keys[i])) {
                    current = map.get(keys[i]);
                } else {
                    System.err.println("Warning: Missing localization key path: " + keyPath);
                    return "[" + keyPath + "]";
                }
            } else {
                System.err.println("Warning: Invalid key path structure: " + keyPath);
                return "[" + keyPath + "]";
            }
        }
        
        if (current instanceof String) {
            return (String) current;
        } else {
            System.err.println("Warning: Key path does not point to a string: " + keyPath);
            return "[" + keyPath + "]";
        }
    }
    
    /**
     * Get a localized string with format arguments.
     */
    public static String format(String keyPath, Object... args) {
        String template = get(keyPath);
        return String.format(template, args);
    }
    
    /**
     * Check if current language is Chinese.
     */
    public static boolean isChinese() {
        return currentLanguage == Language.CHINESE;
    }
    
    /**
     * Check if current language is English.
     */
    public static boolean isEnglish() {
        return currentLanguage == Language.ENGLISH;
    }
    
    /**
     * Reload strings from JSON files (useful for live updates during development).
     */
    public static void reload() {
        loadStrings();
    }
}
