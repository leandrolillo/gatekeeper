package com.gatekeeper.util

/**
 * Detects gate-open intent in a WhatsApp message using three layers:
 *
 *  1. **Regex / stem matching** — each built-in stem is matched as `\bSTEM\w*` so a
 *     single entry covers all conjugations:
 *       "open"  → open, opens, opening, opened
 *       "abr"   → abrir, abre, abra, abri, abriu, abrindo
 *
 *  2. **Negation guard** — if the message contains a negation token the whole
 *     message is rejected before any intent check, preventing false positives like
 *     "don't open the gate" or "não abre pra ninguém".
 *
 *  3. **Multilingual synonym dictionary** — built-in stems and phrases cover
 *     English, Portuguese (BR/PT), and Spanish without any user configuration.
 *
 * User-defined custom keywords (from Settings) are also matched as stems on top of
 * the built-in set.
 */
object MessageMatcher {

    /**
     * Single-word stems matched with `\bSTEM\w*` — covers root + all suffixes.
     * Exposed so the Settings screen can display them.
     */
    val BUILT_IN_STEMS: List<String> = listOf(
        "abr",        // abrir, abre, abra, abri, abriendo
        "desbloque"   // desbloquear, desbloquea
    )

    /**
     * Multi-word phrases matched literally with flexible internal whitespace.
     * Exposed so the Settings screen can display them.
     */
    val BUILT_IN_PHRASES: List<String> = listOf(
        "abre", "abrir",
        "abre la puerta", "abre el portón", "abre el porton",
        "abre la reja", "abre el garage",
        "por favor abre", "por favor abrir",
        "dejame entrar", "déjame entrar",
        "ábreme", "abreme",
        "me abrís", "me abris", "me podés abrir", "me podes abrir",
        "podés abrir", "podes abrir", "puedes abrir",
        "abrí", "abri",
        "afuera", "estoy afuera", "en el portón", "en el porton"
    )

    /**
     * Negation tokens. If ANY appear in the message the whole message is rejected.
     */
    val NEGATION_TOKENS: List<String> = listOf(
        "no abr", "no abras", "no abrir",
        "cancela", "cancelar",
        "no desbloques", "no desbloquees"
    )

    /**
     * Returns true if [message] expresses a gate-open intent and contains no negation.
     * [keywords] is the full active set (built-ins + user-defined). Single-word entries
     * are matched as stems (`\bSTEM\w*`); entries with spaces are matched as phrases.
     */
    fun matches(message: String, keywords: Set<String>): Boolean {
        val lower = message.lowercase().trim()

        // Negation guard — evaluated first, short-circuits everything
        if (NEGATION_TOKENS.any { lower.contains(it) }) return false

        return keywords.any { kw ->
            val k = kw.lowercase().trim()
            if (k.contains(' ')) matchPhrase(lower, k) else matchStem(lower, k)
        }
    }

    /**
     * Matches `\bSTEM\w*` — stem anchored at a word boundary, any trailing suffix.
     * For multi-word stems, each word is individually anchored and words are joined
     * with flexible whitespace.
     */
    private fun matchStem(message: String, stem: String): Boolean {
        if (stem.isBlank()) return false
        val pattern = stem.trim().split(Regex("\\s+"))
            .joinToString(separator = "\\s+") { word -> "\\b${Regex.escape(word)}\\w*" }
        return Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(message)
    }

    /**
     * Matches a phrase allowing flexible whitespace between words.
     */
    private fun matchPhrase(message: String, phrase: String): Boolean {
        if (phrase.isBlank()) return false
        val pattern = phrase.trim().split(Regex("\\s+"))
            .joinToString(separator = "\\s+") { Regex.escape(it) }
        return Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(message)
    }
}
