package com.example.coctails.utils

class TranslitUtils {

    private fun cyr2lat(ch: Char): String? {
        return when (ch) {
            'a' -> "а"
            'b' -> "б"
            'c' -> "ц"
            'd' -> "д"
            'e' -> "е"
            'f' -> "ф"
            'g' -> "г"
            'h' -> "х"
            'i' -> "и"
            'k' -> "к"
            'l' -> "л"
            'm' -> "м"
            'n' -> "н"
            'o' -> "о"
            'p' -> "п"
            'r' -> "р"
            's' -> "с"
            't' -> "т"
            'u' -> "у"
            'v' -> "в"
            'x' -> "х"
            'y' -> "у"
            'z' -> "з"
            'ё' -> "е"
            else -> ch.toString()
        }
    }

    fun cyr2lat(s: String): String? {
        val sb = StringBuilder(s.length * 2)
        for (ch in s.toCharArray()) {
            sb.append(cyr2lat(ch))
        }
        return sb.toString()
    }
}

