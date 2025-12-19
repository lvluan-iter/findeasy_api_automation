package utils;

import net.datafaker.Faker;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.UUID;

public class DataGenerateUtils {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final String ALPHANUM =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Faker faker;

    private static Faker faker() {
        if (faker == null) {
            String locale = ConfigReader.getProperty("locale");
            faker = new Faker(Locale.forLanguageTag(locale));
        }
        return faker;
    }

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(secureRandom.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    public static String randomUUID() {
        return UUID.randomUUID()
                .toString();
    }

    public static String randomHexToken(int byteLength) {
        byte[] bytes = new byte[byteLength];
        secureRandom.nextBytes(bytes);
        return new BigInteger(1, bytes).toString(16);
    }

    public static int randomInt(int min, int max) {
        return secureRandom.nextInt((max - min) + 1) + min;
    }

    public static String fullName() {
        return faker().name()
                .fullName();
    }

    public static String password() {
        return faker().internet()
                .password();
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
}

