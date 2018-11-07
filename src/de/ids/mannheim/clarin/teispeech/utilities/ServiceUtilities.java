package de.ids.mannheim.clarin.teispeech.utilities;

import de.ids.mannheim.clarin.teispeech.tools.DocUtilities;

public class ServiceUtilities {

    private static final String DEFAULT_LANGUAGE = "deu";

    /**
     * check and normalize a language parameter
     *
     * @param lang
     *            a String given as a language
     * @return the checked and normalized language, German ({@code"deu"}) if
     *         {@code lang} is null
     */
    public static String checkLanguage(String lang) {
        if (lang == null) {
            return DEFAULT_LANGUAGE;
        }
        if (!DocUtilities.isLanguage(lang)) {
            throw new IllegalArgumentException(
                    String.format("«%s» is not a valid language!", lang));
        } else {
            lang = DocUtilities.getLanguage(lang).get();
        }
        return lang;
    }

}
