import { writable, get } from "svelte/store";

let loaded = writable(false);
let language = writable("SK");
let languageJson = writable({});
export let languages = {
  SK: "SK",
  EN: "EN",
  DE: "DE",
};

export function isValidLanguage(lang) {
  console.log("lang", lang, languages[lang]);
  return languages[lang] !== undefined;
}
let languageRepository = {
  isLoaded() {
    return get(loaded);
  },
  getLanguage() {
    return get(language);
  },
  /**
   * Returns the value from a nested JSON object based on a string path.
   * @param jsonPath {string|undefined} A path like "path.to.json"
   */
  get(jsonPath) {
    const paths = jsonPath.split(".");
    let current = get(languageJson);
    for (let i = 0; i < paths.length; i++) {
      if (current[paths[i]] !== undefined) {
        current = current[paths[i]];
      } else {
        return undefined;
      }
    }
    return current;
  },
};

export function getLanguageInstance() {
  return languageRepository;
}

export function loadLanguageData(jsonData) {
  languageJson.set(jsonData);
  languageDataLoadedEvent(get(languageJson));
}

let languageDataLoadedEvent = () => {};

export let setLanguageDataLoadedEvent = (loadedEvent) => {
  languageDataLoadedEvent = loadedEvent;
};

export let fetchLanguageData = (pagetype = "", language = languages.SK) => {
  // is invalid language is passed make it default slovak
  if (!isValidLanguage(language)) language = languages.SK;

  const url = `/languageApi/dictionary?pagetype=${pagetype}&language=${language}`;

  fetch(url)
    .then((response) => {
      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      response.json().then((json) => {
        loadLanguageData(json);
      });
    })
    .catch((error) => {
      console.log(
        "There was a problem with the fetch operation:",
        error.message,
      );
    });
};

export function initLanguage(pagetype) {
  const params = new URLSearchParams(window.location.search);
  let languageParam = params.get("language");

  if (!isValidLanguage(languageParam)) languageParam = languages.SK;
  language.set(languageParam);

  fetchLanguageData(pagetype, languageParam);
}
