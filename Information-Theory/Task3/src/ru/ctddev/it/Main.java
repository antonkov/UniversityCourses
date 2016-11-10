package ru.ctddev.it;

/**
 * User: gtkachenko
 * Date: 21/12/14
 */
public class Main {
    private static final String[] DATA = {"IF_WE_CANNOT_DO_AS_WE_WOULD_WE_SHOULD_DO_AS_WE_CAN"};
    protected static final String[] PROVERBS = new String[]{
            "На острую косу много и покосу. Покоси-ка, коса!",
            "Чужая сторона и без ветра сушит и без зимы знобит.",
            "To be or not to be - that is the question.",
            "After dinner sit a while, after supper walk a mile"};
//    private static final String[] DATA = PROVERBS;
//    private static final String[] DATA = {"ABBACD"};


    private void test(Algorithm algorithm) {
        for (String proverb : PROVERBS) {
            algorithm.encode(proverb, true);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        new Main().test(new AdaptiveArithmeticCoding());
    }
}
