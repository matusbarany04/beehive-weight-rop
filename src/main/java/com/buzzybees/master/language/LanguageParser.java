package com.buzzybees.master.language;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageParser {


    public LanguageParser() {

    }

    public static LanguageParser createParser() {
        return new LanguageParser();
    }

    public void parse(Language language) {

        Resource resource = new ClassPathResource("/bundle/bundle.js");
        StringBuilder stringBuilder = new StringBuilder();

        try {
            Files.lines(resource.getFile().toPath())
                    .forEach((line) -> {
                        stringBuilder.append(parseLine(line, language));
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        saveBundleContents(stringBuilder.toString(), language);
    }


    private String parseLine(String line, Language language) {
        Pattern pattern = Pattern.compile("\\?\\([a-z0-9]+(?:\\.[a-z0-9]+)*\\)");
        Matcher matcher = pattern.matcher(line);

        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String replacement = this.parseVariableToText(matcher.group(), language);
            if (Objects.nonNull(replacement))
                matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString() + "\n";
    }


    public boolean saveBundleContents(String content, Language language) {
        Path filePath = Paths.get("src/main/resources/bundle/" + language.getBundleName());
        try {
            // Ensure the directory structure exists
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            // Write to the file (create if not exists, or overwrite if it does)
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Parses variable like ?(dashboard.beehive) to actual text inside a dictionary json
     * json will be like so
     * {
     * "dashboard": {
     * "beehive": "text"
     * }
     * }
     *
     * @param variable
     * @param language
     * @return
     */
    public String parseVariableToText(String variable, Language language) {
        String dict = language.getDictionaryName();
        String json_path = variable.substring(2, variable.length() - 1);

        String[] keys = json_path.split("\\.");


        Resource resource = new ClassPathResource("language/" + dict);
        JSONObject obj;
        try {
            String content = Files.readString(resource.getFile().toPath());
            obj = new JSONObject(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read dictionary: " + dict, e);
        }


        for (int i = 0; i < keys.length - 1; i++) {
            obj = obj.optJSONObject(keys[i]);
            if (obj == null) {
                return "not-found";  // Intermediate path not found or not an object
            }
        }

        // Fetch the string value for the last key
        return obj.optString(keys[keys.length - 1], "not-found");
    }
}
