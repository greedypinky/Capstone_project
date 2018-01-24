package com.exercise.backend;

import java.util.HashMap;
import java.util.Hashtable;

public class Category {

    public static final String CATEGORY_SQUAT = "Squat";
    public static final String CATEGORY_PULL = "Pull";
    public static final String CATEGORY_PUSH = "Push";

    public static final String ALL_CATEGORY_JSON = "{\n" +
            "  \"exercises\": [\n" +
            "    {\n" +
            "      \"category\": \"Squat\",\n" +
            "      \"category description\": \"Squat\",\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Squat1\",\n" +
            "      \"description\": \"this is the description\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"videourl\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"step1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"step2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Squat\",\n" +
            "      \"category description\": \"Squat\",\n" +
            "      \"id\": 2,\n" +
            "      \"name\": \"Squat2\",\n" +
            "      \"description\": \"this is the description\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"videourl\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"step1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"step2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Pull\",\n" +
            "      \"category description\": \"UpperBody Pull Exercise\",\n" +
            "      \"id\": 3,\n" +
            "      \"name\": \"Pull exercise 1\",\n" +
            "      \"description\": \"this is the description\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"videourl\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"step1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"step2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Push\",\n" +
            "      \"category description\": \"UpperBody Push Exercise\",\n" +
            "      \"id\": 4,\n" +
            "      \"name\": \"Push exercise 1\",\n" +
            "      \"description\": \"this is the description\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"videourl\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"step1\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"step2\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "\n" +
            "  ]\n" +
            "} ";


    public static String getAllExerciseJSON () {
            return ALL_CATEGORY_JSON;
    }

}
