package com.example.task03;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class Task03Main {

    public static void main(String[] args) throws IOException {
        List<Set<String>> anagrams = findAnagrams(new FileInputStream("task03/resources/singular.txt"), Charset.forName("windows-1251"));
        for (Set<String> anagram : anagrams) {
            System.out.println(anagram);
        }
    }

    public static List<Set<String>> findAnagrams(InputStream inputStream, Charset charset) {
        Set<String> words = readWords(inputStream, charset);

        Map<String, Set<String>> signatureMap = new HashMap<>();

        for (String word : words) {
            if (word.length() < 3) continue;

            if (!containsOnlyRussianLetters(word)) continue;

            String signature = getSignature(word);

            signatureMap.computeIfAbsent(signature, k -> new TreeSet<>()).add(word);
        }

        return signatureMap.values().stream()
                .filter(set -> set.size() >= 2)
                .sorted(Comparator.comparing(set -> set.iterator().next()))
                .collect(Collectors.toList());
    }

    private static Set<String> readWords(InputStream inputStream, Charset charset) {
        Set<String> words = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.toLowerCase().trim());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading words from stream", e);
        }

        return words;
    }

    private static boolean containsOnlyRussianLetters(String word) {
        return word.chars().allMatch(ch -> {
            return (ch >= 'а' && ch <= 'я') || ch == 'ё';
        });
    }

    private static String getSignature(String word) {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
}