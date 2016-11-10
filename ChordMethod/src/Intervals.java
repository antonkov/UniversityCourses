/**
 * Created by root on 27.03.15.
 */
public class Intervals {
    static boolean ExIn(int l, int m, int r) {
        if (l < r)
            return l < m && m <= r;
        return l < m || m <= r;
    }

    static boolean InEx(int l, int m, int r) {
        if (l < r)
            return l <= m && m < r;
        return l <= m || m < r;
    }

    static boolean InIn(int l, int m, int r) {
        if (l < r)
            return l <= m && m <= r;
        return l <= m || m <= r;
    }

    static boolean ExEx(int l, int m, int r) {
        if (l < r)
            return l < m && m < r;
        return l < m || m < r;
    }
}
