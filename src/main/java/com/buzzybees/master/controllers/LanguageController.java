package com.buzzybees.master.controllers;

import com.buzzybees.master.language.Language;
import com.buzzybees.master.language.LanguageParser;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/languageApi")
public class LanguageController {

    @GetMapping(value = "/dictionary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDictionary(@RequestParam(value = "pagetype", defaultValue = "")  String pagetype, @RequestParam String language) {
        JSONObject dictionary;
        String prefix = "";
        if(pagetype.equals("general")){
            prefix = "general/";
        }else if(pagetype.equals("dashboard")){
            prefix = "dashboard/";
        }

        dictionary = LanguageParser.getDictionary(prefix, Language.valueOf(language));
        if (dictionary == null) {
            dictionary = new JSONObject();
        }

        return ResponseEntity.ok(dictionary.toString());
    }
}
