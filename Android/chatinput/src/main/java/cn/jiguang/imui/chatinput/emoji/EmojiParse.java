package cn.jiguang.imui.chatinput.emoji;

public class EmojiParse {
    public EmojiParse() {
    }

    public static String fromChar(char ch) {
        return Character.toString(ch);
    }

    public static String fromCodePoint(int codePoint) {
        return newString(codePoint);
    }

    public static final String newString(int codePoint) {
        return Character.charCount(codePoint) == 1?String.valueOf(codePoint):new String(Character.toChars(codePoint));
    }
}