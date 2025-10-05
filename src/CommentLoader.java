package src;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * CommentLoader - Loads and manages commentary strings from external JSON files.
 * This separates content from code, making it easier to maintain and localize.
 * Uses native Java without external dependencies.
 */
public class CommentLoader {
    private static Map<String, Object> comments = null;
    private static String currentLanguage = null;
    
    /**
     * Load comments from JSON file.
     * @param language Language code (e.g., "zh_CN", "en_US")
     */
    public static void loadComments(String language) {
        try {
            String filePath = "database/comments/comments_" + language + ".json";
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            comments = parseJSON(content);
            currentLanguage = language;
        } catch (IOException e) {
            System.err.println("Error loading comments file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load comments using the language from LocalizedStrings.
     */
    public static void loadComments() {
        String languageCode = LocalizedStrings.getCurrentLanguage().getCode();
        loadComments(languageCode);
    }
    
    /**
     * Ensure comments are loaded before use.
     */
    private static void ensureLoaded() {
        if (comments == null) {
            loadComments();
        }
    }
    
    /**
     * Simple JSON parser for our specific use case.
     * Handles objects, arrays, and strings.
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
            } else if (json.charAt(i) == '[') {
                // Array
                int bracketCount = 0;
                int start = i;
                do {
                    if (json.charAt(i) == '[') bracketCount++;
                    else if (json.charAt(i) == ']') bracketCount--;
                    i++;
                } while (bracketCount > 0 && i < json.length());
                value = parseArray(json.substring(start, i));
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
    
    private static List<String> parseArray(String json) {
        List<String> list = new ArrayList<>();
        json = json.trim().substring(1, json.length() - 1).trim();
        
        int i = 0;
        while (i < json.length()) {
            // Skip whitespace
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
            if (i >= json.length()) break;
            
            if (json.charAt(i) == '"') {
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
                list.add(sb.toString());
                i++; // skip closing quote
            }
            
            // Skip comma
            while (i < json.length() && (Character.isWhitespace(json.charAt(i)) || json.charAt(i) == ',')) i++;
        }
        
        return list;
    }
    
    /**
     * Get a string array from JSON path.
     * @param path JSON path (e.g., "layup", "dunk.basic")
     * @return Array of strings
     */
    @SuppressWarnings("unchecked")
    public static String[] getStringArray(String path) {
        ensureLoaded();
        try {
            String[] parts = path.split("\\.");
            Object current = comments;
            
            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<String, Object>) current).get(part);
                }
            }
            
            if (current instanceof List) {
                List<String> list = (List<String>) current;
                return list.toArray(new String[0]);
            }
        } catch (Exception e) {
            System.err.println("Error getting string array for path: " + path);
            e.printStackTrace();
        }
        return new String[0];
    }
    
    /**
     * Get a single string from JSON path.
     * @param path JSON path (e.g., "shotPosition.underBasket")
     * @return Single string
     */
    @SuppressWarnings("unchecked")
    public static String getString(String path) {
        ensureLoaded();
        try {
            String[] parts = path.split("\\.");
            Object current = comments;
            
            for (String part : parts) {
                if (current instanceof Map) {
                    current = ((Map<String, Object>) current).get(part);
                }
            }
            
            if (current instanceof String) {
                return (String) current;
            }
        } catch (Exception e) {
            System.err.println("Error getting string for path: " + path);
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * Format a string with parameters (e.g., "{0}" will be replaced with args[0]).
     * @param template Template string with {0}, {1}, etc.
     * @param args Arguments to replace placeholders
     * @return Formatted string
     */
    public static String format(String template, Object... args) {
        String result = template;
        for (int i = 0; i < args.length; i++) {
            result = result.replace("{" + i + "}", String.valueOf(args[i]));
        }
        return result;
    }
    
    /**
     * Get a random string from an array at the given path and format it.
     * @param random Random object
     * @param path JSON path
     * @param args Arguments for formatting
     * @return Formatted random string
     */
    public static String getRandomFormatted(Random random, String path, Object... args) {
        String[] array = getStringArray(path);
        if (array.length == 0) return "";
        
        int index = Utilities.generateRandomNum(random, 1, array.length) - 1;
        return format(array[index], args);
    }
}
