package com.ant.assessment.simulation.test1;

import java.util.HashMap;

public class LongestUniqueSubstring {

    public static String longestUniqueSubstring(String s) {
        int start = 0;
        int maxL = 0;
        int startMax = 0;
        HashMap<Character, Integer> seen = new HashMap<>();

        for (int end = 0; end < s.length(); end++) {
            char current = s.charAt(end);

            if (seen.containsKey(current) && seen.get(current) >= start) {
                start = seen.get(current) + 1;
            }

            seen.put(current, end);

            if (end - start + 1 > maxL) {
                maxL = end - start + 1;
                startMax = start;
            }
        }

        return s.substring(startMax, startMax + maxL);
    }

    public static void main(String[] args) {
        System.out.println(longestUniqueSubstring("java2novice"));
        System.out.println(longestUniqueSubstring("java_language_is_sweet"));

    }
}
