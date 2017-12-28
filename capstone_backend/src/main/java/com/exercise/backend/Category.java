package com.exercise.backend;

import java.util.HashMap;
import java.util.Hashtable;

public class Category {

    public static final String CATEGORY_1 = "Squat";
    public static final String CATEGORY_2 = "Pull";
    public static final String CATEGORY_3 = "Push";

    public static String SQUAT_EXERCISES = "{\n" +
            "    \"category\": \"Squat\",\n" +
            "    \"description\":\"Squat Exercises\",\n" +
            "    \"exercises\": [\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"name\": \"Situp\",\n" +
            "        \"description\": \"\",\n" +
            "        \"image\": \"imageurl\",\n" +
            "        \"video\": \"videourl\",\n" +
            "        \"steps\": [\n" +
            "          {\n" +
            "            \"action\": \"step1\",\n" +
            "            \"times\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"action\": \"step1\",\n" +
            "            \"times\": \"\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 2,\n" +
            "        \"name\": \"Situp\",\n" +
            "        \"description\": \"\",\n" +
            "        \"image\": \"imageurl\",\n" +
            "        \"video\": \"videourl\",\n" +
            "        \"steps\": [\n" +
            "          {\n" +
            "            \"action\": \"step1\",\n" +
            "            \"times\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"action\": \"step1\",\n" +
            "            \"times\": \"\"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }";

    public static String PULL_EXERCISES = "";

    public static String PUSH_EXERCISES = "";


    private static HashMap<String,String> categoriesHashMap = new HashMap<String,String>();

    // initialize the Exercise Category list
    static {
        categoriesHashMap.put(CATEGORY_1,SQUAT_EXERCISES);
        categoriesHashMap.put(CATEGORY_2,PULL_EXERCISES);
        categoriesHashMap.put(CATEGORY_3,PUSH_EXERCISES);
    }


    public static void addCategory(String categoryName, String categoryJSONStr) {

    }

    public static void updateCategory(String categoryName, String categoryJSONStr) {

    }

    public static HashMap<String,String> getHashMap() {
        return categoriesHashMap;
    }

}
