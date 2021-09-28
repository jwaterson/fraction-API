package fraction;

public class FractionImpl implements Fraction {

    private final int numerator, denominator;

    /**
     * Parameters are the <em>numerator</em> and the <em>denominator</em>.
     * Normalize the fraction as you create it.
     * For instance, if the parameters are <pre>(8, -12)</pre>, create a <pre>Fraction</pre> with numerator
     * <pre>-2</pre> and denominator <pre>3</pre>.
     *
     * The constructor should throw an <pre>ArithmeticException</pre> if the denominator is zero.
     *
     * @param numerator the fraction's numerator
     * @param denominator the fraction's denominator
     */
    public FractionImpl(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        int[] normalisedFraction = normalise(numerator, denominator);
        this.numerator = normalisedFraction[0];
        this.denominator = normalisedFraction[1];
    }

    /**
     * The parameter is the numerator and <pre>1</pre> is the implicit denominator.
     *
     * @param wholeNumber representing the numerator
     */
    public FractionImpl(int wholeNumber) {
        this.numerator = wholeNumber;
        this.denominator = 1;
    }

    /**
     * The parameter is a <pre>String</pre> containing either a whole number, such as `5` or `-3`, or a fraction,
     * such as "8/-12".
     * Allow blanks around (but not within) integers.
     * The constructor should throw an <pre>ArithmeticException</pre>
     * if given a string representing a fraction whose denominator is zero.
     * A <pre>NumberFormatException</pre> is thrown if given a string either, representing a fraction
     * whose numerator and/or denominator cannot be represented by <pre>int</pre> values, or that is not
     * interpreted as a valid fraction.
     * <p>
     * You may find it helpful to look at the available String API methods in the Java API.
     *
     * @param fraction the string representation of the fraction
     */
    public FractionImpl(String fraction) {
        /*
        regex matches:
        - any number of whitespaces, followed by
        - an optional negative sign, followed by
        - 1 or more contiguous digits, followed by
        - any number of whitespaces
        optionally followed by:
            - a forward slash, followed by
            - any number of whitespaces, followed by
            - an optional negative sign, followed by
            - 1 or more contiguous digits, followed by
            - any number of whitespaces
         */
        if (fraction.matches("^ *-?[0-9]+ *(?:/ *-?[0-9]+ *)?$")) {
            String[] s = fraction.split("/");
            try {
                int num = Integer.parseInt(s[0].trim());
                // if no denominator was passed, set den to 1
                int den = s.length > 1 ? Integer.parseInt(s[1].trim()) : 1;

                if (den == 0) {
                    throw new ArithmeticException("Denominator cannot be zero");
                }

                int[] normalisedFraction = normalise(num, den);
                this.numerator = normalisedFraction[0];
                this.denominator = normalisedFraction[1];
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Fraction's numerator and denominator must be within the range, " +
                        "Integer.MIN_VALUE = -2147483648, Integer.MAX_VALUE = 2147483647");
            }

        } else {
            throw new NumberFormatException("Please provide one integer, or two integers " +
                    "separated by a '/' (e.g. \"2/4\"). Integers should not contain thousands separators.");
        }
    }

    /**
     * The parameters represent the <em>numerator</em> and <em>denominator</em> of the <pre>Fraction</pre>,
     * though needn't be passed in any particular order.
     *
     * Finds the greatest common divisor shared by the two parameters, recursively.
     * As long as the two numbers are
     * both nonzero, replace the larger number with the remainder of dividing the larger by the smaller and pass them
     * into the method again. Do this until one of the numbers is equal to zero, at which point the other number is
     * the greatest common divisor.
     *
     * The method returns a <pre>long</pre> to accommodate values that may be outside of <pre>int</pre> range.
     *
     * @param num1 numerator/denominator
     * @param num2 numerator/denominator (num 2 must be whichever of these two that num1 is not)
     * @return the greatest common divisor
     */
    static long gcd(long num1, long num2) {
        // GCD is same for two numbers irrespective of their sign, so we judge the parameters by absolute value
        long larger = Math.max(Math.abs(num1), Math.abs(num2));
        long smaller = Math.min(Math.abs(num1), Math.abs(num2));

        if (smaller == 0) { return larger; }
        return gcd(larger % smaller, smaller);
    }

    /**
     * The parameters represent the fraction's <em>numerator</em> and <em>denominator</em>.
     *
     * Normalises the <pre>Fraction</pre> represented by the <em>numerator</em> and <em>denominator</em>.
     *
     * An <pre>ArithmeticException</pre> is thrown where, even after normalising, the <em>numerator</em> and <em>denominator</em>
     * are not both representable using <pre>int</pre> values.
     *
     * Returns the normalised <em>numerator</em> and <em>denominator</em> as an <pre>int[]</pre>.
     *
     * @param numerator the fraction's numerator
     * @param denominator the fraction's denominator
     * @return the normalised fraction
     */
    static int[] normalise(long numerator, long denominator) {
        // correct a negative denominator
        if (denominator < 0) {
            numerator *= -1;
            denominator *= -1;
        }
        long divisor = gcd(numerator, denominator);
        long normNumerator = numerator / divisor;
        long normDenominator = denominator / divisor;

        try {
            return new int[]{Math.toIntExact(normNumerator), Math.toIntExact(normDenominator)};
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Fraction not representable in integers.");
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction add(Fraction f) {
        // a/b + c/d is (ad + bc)/bd
        FractionImpl other = ((FractionImpl) f);

        long numerator = (long) this.numerator * other.denominator + (long) this.denominator * other.numerator;
        long denominator = (long) this.denominator * other.denominator;

        try {
            return new FractionImpl(Math.toIntExact(numerator), Math.toIntExact(denominator));
        } catch (ArithmeticException e) {
            // numerator and/or denominator are outside Integer range
            int[] operands = normalise(numerator, denominator);
            return new FractionImpl(operands[0], operands[1]);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction subtract(Fraction f) {
        // a/b - c/d is (ad - bc)/bd
        FractionImpl other = ((FractionImpl) f);
        long numerator = (long) this.numerator * other.denominator - (long) this.denominator * other.numerator;
        long denominator = (long) this.denominator * other.denominator;

        try {
            return new FractionImpl(Math.toIntExact(numerator), Math.toIntExact(denominator));
        } catch (ArithmeticException e) {
            // numerator and/or denominator are outside Integer range
            int[] operands = normalise(numerator, denominator);
            return new FractionImpl(operands[0], operands[1]);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction multiply(Fraction f) {
        // (a/b) * (c/d) is (a*c)/(b*d)
        FractionImpl other = ((FractionImpl) f);
        long numerator = (long) this.numerator * other.numerator;
        long denominator = (long) this.denominator * other.denominator;

        try {
            return new FractionImpl(Math.toIntExact(numerator), Math.toIntExact(denominator));
        } catch (ArithmeticException e) {
            // numerator and/or denominator are outside Integer range
            int[] operands = normalise(numerator, denominator);
            return new FractionImpl(operands[0], operands[1]);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction divide(Fraction f) {
        // (a/b) / (c/d) is (a*d)/(b*c)
        FractionImpl other = ((FractionImpl) f);
        long numerator = (long) this.numerator * other.denominator;
        long denominator = (long) this.denominator * other.numerator;

        try {
            return new FractionImpl(Math.toIntExact(numerator), Math.toIntExact(denominator));
        } catch (ArithmeticException e) {
            // numerator and/or denominator are outside Integer range
            int[] operands = normalise(numerator, denominator);
            return new FractionImpl(operands[0], operands[1]);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction abs() {
        if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("Cannot represent the absolute value of a fraction " +
                    "where the numerator is Integer's minimum value.");
        }
        return new FractionImpl(Math.abs(this.numerator), this.denominator);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction negate() {
        if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("Cannot negate a fraction " +
                    "where the numerator is Integer's minimum value.");
        }
        return new FractionImpl(this.numerator * -1, this.denominator);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Fraction) && this.compareTo((Fraction) obj) == 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Fraction inverse() {
        return new FractionImpl(this.denominator, this.numerator);
    }

    /**
     * @inheritDoc
     */
    @Override
    public int compareTo(Fraction o) {
        double comparator =
                (double) this.numerator / this.denominator -
                (double) ((FractionImpl) o).numerator / ((FractionImpl) o).denominator;
        return comparator > 0 ? 1 : (comparator == 0 ? 0 : -1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return this.denominator != 1 ?
                String.format("%d/%d", this.numerator, this.denominator) : String.valueOf(this.numerator);
    }
}