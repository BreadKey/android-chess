package io.github.breadkey.chess.view.sign;

public enum LoginWith {
    KAKAO(0),
    NAVER(1),
    GOOGLE(2);

    private static LoginWith currentLoginWith;

    private int value;
    private LoginWith(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static LoginWith fromValue(int value) {
        for (LoginWith loginWith: values()) {
            if (loginWith.value == value) {
                return loginWith;
            }
        }

        return null;
    }

    public static void setCurrentLoginWith(LoginWith loginWith) {
        currentLoginWith = loginWith;
    }

    public static LoginWith getCurrentLoginWith() {
        return currentLoginWith;
    }
}
