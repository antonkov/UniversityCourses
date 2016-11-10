public abstract class LipschitzFunction implements Function {
    private int ptsCnt = 100;

    public double getL() {
        double dx = (getRightX() - getLeftX()) / ptsCnt;
        double prevPoint = getLeftX();
        double curPoint = getLeftX() + dx;
        double l = 0;
        while (curPoint <= getRightX()) {
            l = Math.max(l, Math.abs(getValue(curPoint) - getValue(prevPoint)) / (curPoint - prevPoint));
            prevPoint = curPoint;
            curPoint += dx;
        }
        return l;
    }
}