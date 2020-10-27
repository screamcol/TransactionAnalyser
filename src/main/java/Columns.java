public enum Columns {
    ID(0), Date(1), AMOUNT(2), MERCHANT(3), TYPE(4), RELATED_TRANSACTION(5);

    private int index;
    Columns(int index) {
        this.index = index;
    }

    int getIndex() {
        return index;
    }
}
