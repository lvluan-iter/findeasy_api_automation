package utils;

import net.datafaker.Faker;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DataGenerateUtils {
    private static final ThreadLocal<Faker> FAKER =
            ThreadLocal.withInitial(() ->
                    new Faker(Locale.forLanguageTag(
                            ConfigReader.init()
                                    .getPropertyOrDefault("locale", "vi")
                    ))
            );

    private static Faker faker() {
        return FAKER.get();
    }

    public static String fullName() {
        return faker().name()
                .fullName();
    }

    public static String gender() {
        return Randomizer.random()
                .nextBoolean() ? "MALE" : "FEMALE";
    }

    public static Date birthday() {
        LocalDate end = LocalDate.now()
                .minusYears(18);
        LocalDate start = LocalDate.now()
                .minusYears(60);

        long daysBetween = ChronoUnit.DAYS.between(start, end);
        LocalDate birthday = start.plusDays(Randomizer.random()
                .nextInt(0, (int) daysBetween));
        return Date.valueOf(birthday);
    }

    public static String username() {
        return faker().internet()
                .username();
    }

    public static String password() {
        return Randomizer.randomAlphabets(1)
                .toUpperCase()
                + Randomizer.randomAlphabets(1)
                .toLowerCase()
                + Randomizer.randomNumeric(1)
                + Randomizer.randomSpecialCharacters(1)
                + Randomizer.randomAlphaNumeric(6);
    }

    public static String email() {
        return faker().internet()
                .emailAddress();
    }

    public static String phone() {
        return faker().phoneNumber()
                .phoneNumber();
    }

    public static String address() {
        return faker().address()
                .fullAddress();
    }

    public static String city() {
        return faker().address()
                .city();
    }

    public static String country() {
        return faker().address()
                .country();
    }

    public static String street() {
        return faker().address()
                .streetAddress();
    }

    public static String jobTitle() {
        return faker().job()
                .title();
    }

    public static String programmingLanguage() {
        return faker().programmingLanguage()
                .name();
    }

    public static String lorem() {
        return faker().lorem()
                .sentence(20);
    }

    public static String imageUrl() {
        return faker().internet()
                .image();
    }

}

