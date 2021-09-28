/*
(Example usage of Fraction API)
Brothers, Barry, Robin and Maurice are all studying Computer Science at neighbouring, competing universities.

They themselves are deeply competitive and want to know who did the best in their respective Java programming exams.

Barry got 96/120
Robin got 138/180
Maurice got 160/200

Who did the best?
(The brothers agree that the score(s) of the top student(s) should be represented as normalised fraction(s))
 */

import fraction.*;
import java.util.*;

public class Grade {

    private final String name;
    private final Fraction grade;

    public Grade(String name, Fraction grade) {
        this.name = name;
        this.grade = grade;
    }

    public static ArrayList<Grade> bestGrade(Grade... args) {

        if (args.length < 2) {
            throw new IllegalArgumentException("Please provide at least 2 grades.");
        }

        ArrayList<Grade> bestGrade = new ArrayList<>();
        Grade best = args[0];
        for (Grade g: args) {
            if (g.grade.compareTo(best.grade) > 0) {
                bestGrade.clear();
                best = g;
                bestGrade.add(best);
            } else if (g.grade.compareTo(best.grade) == 0) {
                bestGrade.add(g);
            }
        }
        return bestGrade;
    }

    public static void main(String[] args) {

        Grade barryGrade = new Grade("Barry", new FractionImpl(96, 120));
        Grade robinGrade = new Grade("Robin", new FractionImpl(138, 180));
        Grade mauriceGrade = new Grade("Maurice", new FractionImpl("160/200"));

        ArrayList<Grade> best = bestGrade(barryGrade, robinGrade, mauriceGrade);

        if (best.size() > 1) {
            System.out.println("There was a tie for the top spot!...");
            best.stream().map(i -> String.format("As a normalised fraction, %s got: %s", i.name, i.grade))
                    .forEach(System.out::println);
        } else {
            System.out.printf("%s had the best grade. As a normalised fraction, the grade was: %s",
                    best.get(0).name, best.get(0).grade.toString());
        }
    }
}
