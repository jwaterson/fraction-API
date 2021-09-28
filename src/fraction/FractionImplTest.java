package fraction;

import java.util.*;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class FractionImplTest {
    /*
    Tests in this file should test each of the methods implemented in the FractionImpl class.
    This includes ensuring that the correct exceptions are thrown, where appropriate.

    Note:
    - Tests are parameterized except for tests for expected exceptions. Expected exceptions
      are sometimes grouped in a single class of related operations (i.e. A class for Arithmetic Exceptions,
      accounting for exceptions caused by add, subtract, multiply, divide)
    - Tests are provided with single line comments above the test body/parameters for convenience where one or more
      of the following statements are true:
        * the premise of the test/group of tests is not immediately clear
        * a test's code may take a little longer to decipher than for most other tests
    */

    public static class ConstructorExpectedException {

        @Test(expected = ArithmeticException.class)
        public void intX2ConstructorException1() {
            // tests that -0 is handled the same as 0
            new FractionImpl(new SplittableRandom().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE), -0);
        }

        @Test(expected = ArithmeticException.class)
        public void intX2ConstructorException2() {
            // tests that any Integer over 0 is disallowed
            new FractionImpl(new SplittableRandom().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE), 0);
        }

        @Test(expected = ArithmeticException.class)
        public void intX2ConstructorException3() {
            // tests where denominator is Integer's min value and is indivisible with numerator, an exception is thrown
            new FractionImpl(3, Integer.MIN_VALUE);
        }

        @Test(expected = ArithmeticException.class)
        public void stringConstructorException1() {
            // tests that String constructor throws exception where denominator is 0
            new FractionImpl("10/0");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException2() {
            new FractionImpl("1/");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException3() {
            new FractionImpl("2/3#");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException4() {
            new FractionImpl(" / ");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException5() {
            new FractionImpl("");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException6() {
            new FractionImpl("2\4");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException7() {
            new FractionImpl("1       121/         22");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException8() {
            new FractionImpl("01/04/21");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException9() {
            new FractionImpl("This assignment is due on 09/04");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException10() {
            new FractionImpl("--4/1");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException11() {
            // tests that whitespace(s) between negative sign and an apparently related number are disallowed
            new FractionImpl("    -43   /                 - 2    ");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException12() {
            new FractionImpl("34//1");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException13() {
            new FractionImpl("34\\0");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException14() {
            // tests that floating point numbers are disallowed
            new FractionImpl("0.5/2");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException15() {
            // tests that thousands separators are disallowed
            new FractionImpl("100,000");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException16() {
            // tests that numbers outside of int range are disallowed
            new FractionImpl("10000000000000");
        }

        @Test(expected = NumberFormatException.class)
        public void stringConstructorException17() {
            // tests that numbers outside of int range are disallowed
            new FractionImpl("-10000000000000");
        }
    }

    @RunWith(Parameterized.class)
    public static class GcdTest {

        @Parameters(name = "{index}: gcd({1}, {2}) = {0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {expected, input1, input2}

                {1, 2, 999_999_999},

                {512, 1_048_576, -40_000_000},

                // tests that inverting numerator and denominator gets same result
                {17, -17, 119},
                {17, 119, -17},

                // tests both positive and negative inputs of same absolute value, get same result
                {333, 666, 999},
                {333, -666, -999},

                // tests that gcd works with max int value (and multiples)
                {1, 3, Integer.MAX_VALUE},
                {2, 4096, (long) Integer.MAX_VALUE * 2},
                {Integer.MAX_VALUE, 15_032_385_529L, 81_604_378_586L},

                // tests that absolute values of Integer's min and max values are not conflated
                {1, Integer.MAX_VALUE, Integer.MIN_VALUE},
            });
        }

        private final long expOutput;
        private final long numeratorInput;
        private final long denominatorInput;

        public GcdTest(long expOutput, long numeratorInput, long denominatorInput) {
            this.expOutput = expOutput;
            this.numeratorInput = numeratorInput;
            this.denominatorInput = denominatorInput;
        }

        @Test
        public void gcdTest() {
            Assert.assertEquals(expOutput, FractionImpl.gcd(numeratorInput, denominatorInput));
        }
    }

    @RunWith(Parameterized.class)
    public static class NormaliseTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {expected, input1, input2}

                {new int[] {2, 43}, 356, 7654},
                {new int[] {72, 1}, 6_440_648_040L, 89453445},

                // tests negative denominator is handled correctly
                {new int[] {-3, 2}, 12, -8},
                {new int[] {-7, 8}, 7, -8},

                // tests Integer's max value (and multiples) are handled correctly
                {new int[] {-1, 1}, -Integer.MAX_VALUE, Integer.MAX_VALUE},
                {new int[] {-1, 1}, Integer.MAX_VALUE, -Integer.MAX_VALUE},
                {new int[] {1, 250}, (long) Integer.MAX_VALUE * 2, (long) Integer.MAX_VALUE * 500},

                // tests operations using Integer's min value (and multiples) are handled correctly
                {new int[] {-3, Integer.MIN_VALUE / 2 * - 1}, 6, Integer.MIN_VALUE},
                {new int[] {1, 1}, Integer.MIN_VALUE, Integer.MIN_VALUE},
                {new int[] {1, 2}, Integer.MIN_VALUE, (long) Integer.MIN_VALUE * 2},

                // tests that 0 over anything returns {0, 1}
                {new int[] {0, 1}, 0, new SplittableRandom().nextLong(Long.MIN_VALUE, Long.MAX_VALUE)}
            });
        }

        private final int[] expOutput;
        private final long numeratorInput;
        private final long denominatorInput;

        public NormaliseTest(int[] expOutput, long numeratorInput, long denominatorInput) {
            this.expOutput = expOutput;
            this.numeratorInput = numeratorInput;
            this.denominatorInput = denominatorInput;
        }

        @Test
        public void normaliseTest() {
            Assert.assertArrayEquals(expOutput, FractionImpl.normalise(numeratorInput, denominatorInput));
        }
    }


    @RunWith(Parameterized.class)
    public static class AddTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {type, expNum, expDen, origFraction, addFraction}

                // tests that a whole number is derived as the result of an appropriate addition
                {1, 1, new FractionImpl(3, 4), new FractionImpl(1, 4)},
                {0, 1, new FractionImpl(12, -8), new FractionImpl("6/4")},

                // tests max and min Integers are handled correctly
                {-1, 1, new FractionImpl(Integer.MIN_VALUE), new FractionImpl(Integer.MAX_VALUE)},
                {2, Integer.MAX_VALUE, new FractionImpl(1, Integer.MAX_VALUE),
                        new FractionImpl(1, Integer.MAX_VALUE)},

                // tests that same result is derived where different constructors are used
                {5, 8, new FractionImpl("3 / 4"), new FractionImpl("-1/8")},
                {5, 8, new FractionImpl(3, 4), new FractionImpl(-1, 8)},

                // tests improper fractions are rendered correctly
                {22, 3, new FractionImpl("11/3"), new FractionImpl("22/6")},
                {541, 8, new FractionImpl("4"), new FractionImpl("509/8")},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;
        private final FractionImpl newFraction;

        public AddTest(int expNumerator, int expDenominator, FractionImpl origFraction, FractionImpl newFraction) {
            this.expFraction = new FractionImpl(expNumerator, expDenominator);
            this.origFraction = origFraction;
            this.newFraction = newFraction;
        }

        @Test
        public void addTest() {
            Assert.assertEquals(expFraction, origFraction.add(newFraction));
        }
    }

    @RunWith(Parameterized.class)
    public static class SubtractTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {type, expNum, expDen, origFraction, addFraction}

                // tests that same result is derived from same addition using Fractions of varying constructors
                {7, 8, new FractionImpl("3 / 4"), new FractionImpl("   -1/8")},
                {7, 8, new FractionImpl(3, 4), new FractionImpl(-1, 8)},

                {-1, 999_999_999, new FractionImpl(0), new FractionImpl("1/ 999999999")},

                // tests that immediate proximity to int's limits doesn't affect correctness of result
                {Integer.MIN_VALUE, 1, new FractionImpl(-1), new FractionImpl(Integer.MAX_VALUE)},
                {Integer.MIN_VALUE, Integer.MAX_VALUE, new FractionImpl(-1, Integer.MAX_VALUE),
                        new FractionImpl(Integer.MAX_VALUE, Integer.MAX_VALUE)},

                // tests that working backwards from addTest's last set of parameters yields correct result
                {509, 8, new FractionImpl("541/8"), new FractionImpl("4")},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;
        private final FractionImpl newFraction;

        public SubtractTest(int expNumerator, int expDenominator, FractionImpl origFraction, FractionImpl newFraction) {
            this.expFraction = new FractionImpl(expNumerator, expDenominator);
            this.origFraction = origFraction;
            this.newFraction = newFraction;
        }

        @Test
        public void subtractTest() {
            Assert.assertEquals(expFraction, origFraction.subtract(newFraction));
        }
    }

    @RunWith(Parameterized.class)
    public static class MultiplyTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {type, expNum, expDen, origFraction, addFraction}

                // tests that 0/1 multiplied by any fraction returns 0/1
                {0, 1, new FractionImpl(0), new FractionImpl(
                        new SplittableRandom().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE),
                        new SplittableRandom().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE))},

                // tests multiplications using Integer's max and min values
                {Integer.MIN_VALUE, 1, new FractionImpl(Integer.MIN_VALUE / 2),
                        new FractionImpl(2, 1)},
                {Integer.MAX_VALUE - 1, 1, new FractionImpl(Integer.MAX_VALUE / 2),
                        new FractionImpl(2, 1)},

                {1, 4, new FractionImpl(1), new FractionImpl(32, 128)},

                {9, 100, new FractionImpl("   3/10"), new FractionImpl("   3/10")},

                {6084, 79, new FractionImpl("   78/79"), new FractionImpl("   78")},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;
        private final FractionImpl newFraction;

        public MultiplyTest(int expNumerator, int expDenominator, FractionImpl origFraction, FractionImpl newFraction) {
            this.expFraction = new FractionImpl(expNumerator, expDenominator);
            this.origFraction = origFraction;
            this.newFraction = newFraction;
        }

        @Test
        public void multiplyTest() {
            Assert.assertEquals(expFraction, origFraction.multiply(newFraction));
        }
    }

    @RunWith(Parameterized.class)
    public static class DivideTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {type, expNum, expDen, origFraction, addFraction}

                {-4, 1, new FractionImpl("   8/8"), new FractionImpl("   -2/8    ")},

                {-9, 100, new FractionImpl(3, 10), new FractionImpl(-10, 3)},

                {34, 33, new FractionImpl(34), new FractionImpl(33)},

                // tests two indivisible fractions
                {-500, 3, new FractionImpl("   500"), new FractionImpl("   -3/1   ")},

                // tests that a fraction divided by an equal fraction yields 1/1
                {1, 1, new FractionImpl("   1/3"), new FractionImpl("   1/3    ")},

                // tests that proximity to limit of int range works as expected
                {Integer.MIN_VALUE / 4, 1, new FractionImpl(Integer.MIN_VALUE), new FractionImpl("4     ")},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;
        private final FractionImpl newFraction;

        public DivideTest(int expNumerator, int expDenominator, FractionImpl origFraction, FractionImpl newFraction) {
            this.expFraction = new FractionImpl(expNumerator, expDenominator);
            this.origFraction = origFraction;
            this.newFraction = newFraction;
        }

        @Test
        public void divideTest() {
            Assert.assertEquals(expFraction, origFraction.divide(newFraction));
        }
    }

    public static class ArithmeticExpectedExceptions {

        @Test(expected = ArithmeticException.class)
        public void addException1() {
            Fraction f = new FractionImpl(Integer.MAX_VALUE);
            Fraction g = new FractionImpl("1/ 2147483647");
            f.add(g);
        }

        @Test(expected = ArithmeticException.class)
        public void addException2() {
            Fraction f = new FractionImpl("1/ 2147483647");
            Fraction g = new FractionImpl(Integer.MAX_VALUE, 1);
            f.add(g);
        }

        @Test(expected = ArithmeticException.class)
        public void addException3() {
            Fraction f = new FractionImpl(Integer.MIN_VALUE, 1);
            Fraction g = new FractionImpl(-1, Integer.MAX_VALUE);
            f.add(g);
        }

        @Test(expected = ArithmeticException.class)
        public void subtractException1() {
            Fraction f = new FractionImpl(-2, 1);
            Fraction g = new FractionImpl(Integer.MAX_VALUE);
            f.subtract(g);
        }

        @Test(expected = ArithmeticException.class)
        public void subtractException2() {
            Fraction f = new FractionImpl(Integer.MAX_VALUE);
            Fraction g = new FractionImpl(-Integer.MAX_VALUE);
            f.subtract(g);
        }

        @Test(expected = ArithmeticException.class)
        public void multiplyException1() {
            Fraction f = new FractionImpl(-2, 1);
            Fraction g = new FractionImpl(Integer.MAX_VALUE);
            f.multiply(g);
        }

        @Test(expected = ArithmeticException.class)
        public void multiplyException2() {
            Fraction f = new FractionImpl(Integer.MAX_VALUE);
            Fraction g = new FractionImpl(2, -1);
            f.multiply(g);
        }

        @Test(expected = ArithmeticException.class)
        public void divideException1() {
            Fraction f = new FractionImpl(Integer.MAX_VALUE);
            Fraction g = new FractionImpl(1, 2);
            f.divide(g);
        }
    }

    @RunWith(Parameterized.class)
    public static class AbsTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // {expNum, expDen, origFraction}

                {1, 7, new FractionImpl(-1, 7)},

                // tests that negative and positive numerator yields same result when expected
                {5, 2, new FractionImpl(-20, 8)},
                {5, 2, new FractionImpl(20, 8)},

                {Integer.MAX_VALUE, 8, new FractionImpl(Integer.MAX_VALUE, 8).negate()},
                {Integer.MAX_VALUE, 8, new FractionImpl(-Integer.MAX_VALUE, -8).negate()},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;

        public AbsTest(int expNum, int expDen, FractionImpl origFraction) {
            this.expFraction = new FractionImpl(expNum, expDen);
            this.origFraction = origFraction;
        }

        @Test
        public void absTest() {
            Assert.assertEquals(expFraction, origFraction.abs());
        }
    }

    @RunWith(Parameterized.class)
    public static class NegateTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {expNum, expDen, origFraction}

                {-5, 2, new FractionImpl("20/8")},

                {0, 1, new FractionImpl("0/8")},

                // tests lowest negatable Integer
                {Integer.MAX_VALUE, 1, new FractionImpl(Integer.MIN_VALUE + 1)},
                {-Integer.MAX_VALUE, 1, new FractionImpl(Integer.MAX_VALUE)},
                {-Integer.MAX_VALUE, 8, new FractionImpl(-Integer.MAX_VALUE, 8).negate()},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;

        public NegateTest(int expNum, int expDen, FractionImpl origFraction) {
            this.expFraction = new FractionImpl(expNum, expDen);
            this.origFraction = origFraction;
        }

        @Test
        public void negateTest() {
            Assert.assertEquals(expFraction, origFraction.negate());
        }
    }

    @RunWith(Parameterized.class)
    public static class InverseTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {expNum, expDen, origFraction}

                {1, 24, new FractionImpl(72, 3)},

                {-45456156, 561171161, new FractionImpl(-1122342322, 90912312)},

                // tests identical numerator and denominator
                {1, 1, new FractionImpl("-5/-5")},

                // tests a negative numerator doesn't become a negative denominator
                {-1111111, 11111111, new FractionImpl("-99999999/9999999")},

                {-1, Integer.MAX_VALUE, new FractionImpl(-Integer.MAX_VALUE)},
            });
        }

        private final FractionImpl expFraction;
        private final FractionImpl origFraction;

        public InverseTest(int expNum, int expDen, FractionImpl origFraction) {
            this.expFraction = new FractionImpl(expNum, expDen);
            this.origFraction = origFraction;
        }

        @Test
        public void inverseTest() {
            Assert.assertEquals(expFraction, origFraction.inverse());
        }
    }

    public static class ModifiedFractionExpectedExceptions {
        @Test(expected = ArithmeticException.class)
        public void absException1() {
            new FractionImpl(Integer.MIN_VALUE, 1).abs();
        }

        @Test(expected = ArithmeticException.class)
        public void absException2() {
            new FractionImpl("-2147483648").abs();
        }

        @Test(expected = ArithmeticException.class)
        public void negateException1() {
            new FractionImpl(Integer.MIN_VALUE).negate();
        }

        @Test(expected = ArithmeticException.class)
        public void inverseException1() {
            new FractionImpl(Integer.MIN_VALUE).inverse();
        }

        @Test(expected = ArithmeticException.class)
        public void inverseException2() {
            new FractionImpl("0").inverse();
        }

        @Test(expected = ArithmeticException.class)
        public void inverseException3() {
            Fraction f = new FractionImpl("-1");
            Fraction g = new FractionImpl(-Integer.MAX_VALUE);
            f.add(g).inverse();
        }
    }

    @RunWith(Parameterized.class)
    public static class CompareToTest {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                // Parameter format: {expResult, origFraction, newFraction}

                // tests with constant 1st fraction parameter
                {0, new FractionImpl(1, 4) , new FractionImpl(2, 8)},
                {-1, new FractionImpl(1, 4), new FractionImpl("3/8")},
                {1, new FractionImpl(1, 4), new FractionImpl(1,8)},
                {1, new FractionImpl(1, 4), new FractionImpl("1/8")},
                {0, new FractionImpl(1, 4), new FractionImpl("16/4").inverse()},
                {-1, new FractionImpl(1, 4), new FractionImpl("31/8").inverse()},

                // tests minute variance in compared values
                {1, new FractionImpl(1, Integer.MAX_VALUE / 2),
                        new FractionImpl(2, Integer.MAX_VALUE)},
                {-1, new FractionImpl(2, Integer.MAX_VALUE),
                        new FractionImpl(1, Integer.MAX_VALUE / 2)},

                // tests that 0 over any Integer is the same as 0 over 1
                {0, new FractionImpl(0, 1), new FractionImpl(0,
                        new SplittableRandom().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE))},

                // tests that fractions that both normalise to 1/1 are considered equal
                {0, new FractionImpl(-Integer.MAX_VALUE, -Integer.MAX_VALUE),
                        new FractionImpl(Integer.MAX_VALUE, Integer.MAX_VALUE)},

                {-1, new FractionImpl(Integer.MIN_VALUE), new FractionImpl(Integer.MAX_VALUE)},

                // testing between large negative ints
                {-1, new FractionImpl(Integer.MIN_VALUE, 1), new FractionImpl(Integer.MIN_VALUE, 2)}
            });
        }

        private final int expOut;
        private final FractionImpl origFraction;
        private final FractionImpl newFraction;

        public CompareToTest(int expOut, FractionImpl origFraction, FractionImpl newFraction) {
            this.expOut = expOut;
            this.origFraction = origFraction;
            this.newFraction = newFraction;
        }

        @Test
        public void compareToTest() {
            Assert.assertEquals(expOut, origFraction.compareTo(newFraction));
        }
    }

    @RunWith(Parameterized.class)
    public static class EqualsTest<T> {

        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    // Parameter format: {expResult, origFraction, item}

                    {true, new FractionImpl(19, 18) , new FractionImpl(-19, -18)},

                    {true, new FractionImpl("-1 / -2"), new FractionImpl("256/      512")},

                    // tests that a 'fraction' of a different type is not equal to a Fraction object
                    {false, new FractionImpl(1, 20), "1/20"},
                    {false, new FractionImpl(-1, -1), 1},

                    // tests that Fractions created by different constructors yield correct result
                    {true, new FractionImpl(Integer.MIN_VALUE), new FractionImpl("-2147483648")},
                    {true, new FractionImpl(Integer.MIN_VALUE, 1), new FractionImpl("-2147483648")},
            });
        }

        private final boolean expOut;
        private final FractionImpl origFraction;
        private final T item;

        public EqualsTest(boolean expOut, FractionImpl origFraction, T item) {
            this.expOut = expOut;
            this.origFraction = origFraction;
            this.item = item;
        }

        @Test
        public void compareToTest() {
            Assert.assertEquals(expOut, origFraction.equals(item));
        }
    }

    @RunWith(Parameterized.class)
    public static class ToStringTest {

        @Parameters(name = "{index}: {1} = {0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][] {
                    // Parameter format: {expected, input}

                    // testing whole numbers
                    {"-1", new FractionImpl(12, -12).toString()},
                    {"-2", new FractionImpl(-10, 5).toString()},
                    {"361", new FractionImpl(6859, 19).toString()},
                    {"-1", new FractionImpl(Integer.MAX_VALUE, -Integer.MAX_VALUE).toString()},

                    //tests that each constructor yields same result for equivalent fraction input
                    {"0", new FractionImpl(0, 1).toString()},
                    {"0", new FractionImpl(0).toString()},
                    {"0", new FractionImpl("0").toString()},

                    {"1/3", new FractionImpl(12, 36).toString()},

                    {"3/10", new FractionImpl(3, 10).toString()},

                    // tests int's lowest and highest values
                    {String.valueOf(Integer.MIN_VALUE), new FractionImpl(Integer.MIN_VALUE).toString()},
                    {String.valueOf(Integer.MAX_VALUE), new FractionImpl(Integer.MAX_VALUE).toString()},

                    // tests two negative inputs
                    {"1", new FractionImpl("-1020/-1020").toString()},
                    {"1/3", new FractionImpl("-3/-9").toString()},
                    {"393/112", new FractionImpl("-4323/ -1232").toString()},

                    // tests indivisible numerator and denominator
                    {"4/5", new FractionImpl("4/5").toString()},
                    {"48/7", new FractionImpl("48/7").toString()},

                    // testing with irregular input strings
                    {"11/2", new FractionImpl("       121/         22").toString()},
                    {"1", new FractionImpl("1                             ").toString()},
                    {"9/4", new FractionImpl("09/04").toString()},
                    {"1/9", new FractionImpl("    -3            /            -27    ").toString()},
                    {"-1/9", new FractionImpl("00003000            /            -000027000").toString()},
            });
        }

        private final String expOutput;
        private final String stringInput;

        public ToStringTest(String expOutput, String stringInput) {
            this.expOutput = expOutput;
            this.stringInput = stringInput;
        }

        @Test
        public void toStringTest() {
            Assert.assertEquals(expOutput, stringInput);
        }
    }
}
