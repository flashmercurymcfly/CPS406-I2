package hobogame;

final class Mouse extends ButtonInput {

    private IntCoord loc;

    public Mouse(int n) {
        super(n);
        loc = IntCoord.ORIGIN;
    }

    public void setLoc(IntCoord loc) {
        this.loc = loc;
    }

    public IntCoord getLoc() {
        return loc;
    }
}
