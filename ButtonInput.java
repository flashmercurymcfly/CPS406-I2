package hobogame;

class ButtonInput {

    private final boolean[] buttons;
    private final boolean[] buttonConsumed;
    private int lastButton;

    public ButtonInput(int buttonCount) {
        buttons = new boolean[buttonCount];
        buttonConsumed = new boolean[buttonCount];
        lastButton = -1;
    }

    private boolean check(int n) {
        return n >= 0 && n < buttons.length;
    }

    void down(int n) {
        if (check(n) && !buttons[n]) {
            lastButton = n;
            buttons[n] = true;
            buttonConsumed[n] = false;
        }
    }

    void up(int n) {
        if (check(n)) {
            buttons[n] = false;
        }
    }

    boolean buttonDown(int n) {
        return check(n) && buttons[n];
    }

    boolean buttonAvailable(int n) {
        return check(n) && buttons[n] && !buttonConsumed[n];
    }

    boolean buttonClaim(int n) {
        if (buttonAvailable(n)) {
            buttonConsumed[n] = true;
            return true;
        }
        return false;
    }

    int lastButton() {
        return lastButton;
    }
}
