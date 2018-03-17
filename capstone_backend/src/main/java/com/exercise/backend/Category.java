package com.exercise.backend;

import java.util.HashMap;
import java.util.Hashtable;

public class Category {

    //TODO:- can use GSON to generate the JSON data for us
    // But for now we just hardcode the JSON data

    public static final String CATEGORY_SQUAT = "Squat";
    public static final String CATEGORY_PULL = "Pull";
    public static final String CATEGORY_PUSH = "Push";

    public static final String ALL_CATEGORY_JSON ="{\n" +
            "  \"exercises\": [\n" +
            "    {\n" +
            "      \"category\": \"Squat\",\n" +
            "      \"category description\": \"Squat\",\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Goblet Squat\",\n" +
            "      \"description\": \"Level: Beginner | Body parts: Quadriceps | Equipment: Kettlebells\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"https://drive.google.com/open?id=1yU2N7s8-yS-EaKIVmplADylVW0d6-sFU\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step1:Stand holding a light kettlebell by the horns close to your chest. This will be your starting position.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step2:Squat down between your legs until your hamstrings are on your calves. Keep your chest and head up and your back straight.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step3:At the bottom position, pause and use your elbows to push your knees out. Return to the starting position, and repeat for 10-20 repetitions.\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Squat\",\n" +
            "      \"category description\": \"Squat\",\n" +
            "      \"id\": 2,\n" +
            "      \"name\": \"Split Squat\",\n" +
            "      \"description\": \"Level: Intermediate | Equipment: Bench, Dumbbells | Body parts: Glutes, Hamstrings, Quads\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"https://drive.google.com/file/d/10UyaTa62hHPa5zJgPEmhnhFs6l5veYw1/view\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step1:Hold a dumbbell in each hand with your arms fully extended at your sides and your palms facing each other. With your feet hip-width apart, place the instep of your rear foot on a bench. Your feet should be approximately three feet apart.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step2:Lower your hips toward the floor so that your rear knee comes close to the floor. Pause and drive through your front heel to return to the starting position.\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Pull\",\n" +
            "      \"category description\": \"UpperBody Pull Exercise\",\n" +
            "      \"id\": 3,\n" +
            "      \"name\": \"One-Arm Dumbbell Row\",\n" +
            "      \"description\": \"Level: Beginner | Body parts: Middle Back | Equipment: Dumbbell\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"https://drive.google.com/open?id=1pzfIgVbFkIlbMmMjZ7PUd53RaPZu4r5t\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step1:Pull the resistance straight up to the side of your chest, keeping your upper arm close to your side and keeping the torso stationary. Breathe out as you perform this step. Tip: Concentrate on squeezing the back muscles once you reach the full contracted position. Also, make sure that the force is performed with the back muscles and not the arms. Finally, the upper torso should remain stationary and only the arms should move. The forearms should do no other work except for holding the dumbbell; therefore do not try to pull the dumbbell up using the forearms.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step2:Lower the resistance straight down to the starting position. Breathe in as you perform this step.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step3:Repeat the movement for the specified amount of repetitions.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step4:Switch sides and repeat again with the other arm.\"\n" +
            "        }\n" +
            "\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Pull\",\n" +
            "      \"category description\": \"UpperBody Pull Exercise\",\n" +
            "      \"id\": 4,\n" +
            "      \"name\": \"Standing Dumbbell Upright Row\",\n" +
            "      \"description\": \"Level: Beginner | Body Part: Traps Muscles Equipment: Dumbbell\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"https://drive.google.com/open?id=10gWLhEw1KWtr8mP7x5075tnoOs1FmQN8\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step1:Grasp a dumbbell in each hand with a pronated (palms forward) grip that is slightly less than shoulder width. The dumbbells should be resting on top of your thighs.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step2:Use your side shoulders to lift the dumbbells as you exhale. The dumbbells should be close to the body as you move it up and the elbows should drive the motion.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step3:Lower the dumbbells back down slowly to the starting position. Inhale as you perform this portion of the movement.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step4:Repeat for the recommended amount of repetitions.\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "\n" +
            "    {\n" +
            "      \"category\": \"Push\",\n" +
            "      \"category description\": \"UpperBody Push Exercise\",\n" +
            "      \"id\": 5,\n" +
            "      \"name\": \"Standing Dumbbell Press\",\n" +
            "      \"description\": \"Level: Beginner | Body Parts: Shoulders | Equipment: Dumbbell\",\n" +
            "      \"image\": \"imageurl\",\n" +
            "      \"video\": \"https://drive.google.com/open?id=1ONMX-3nKgwk2v9JLlpoE-VSDz3Rl8MUH\",\n" +
            "      \"steps\": [\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step1:Standing with your feet shoulder width apart, take a dumbbell in each hand. Raise the dumbbells to head height, the elbows out and about 90 degrees. This will be your starting position.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step2:Maintaining strict technique with no leg drive or leaning back, extend through the elbow to raise the weights together directly above your head.\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"stepDescription\": \"Step3:Pause, and slowly return the weight to the starting position\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "\n" +
            "  ]\n" +
            "}";

    public static String getAllExerciseJSON () {
        return ALL_CATEGORY_JSON;
    }

}
