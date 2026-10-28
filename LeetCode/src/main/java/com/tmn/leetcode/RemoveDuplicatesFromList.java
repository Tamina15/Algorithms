package com.tmn.leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Remove all duplicated values in list with the same attribute.
 *
 * @author NhatTranMinh
 */
public class RemoveDuplicatesFromList {

    static Random r = new Random();

    public static String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = r.nextInt(1, 20);
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) (r.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    static List<Person> randomList(int n) {
        List<Person> persons = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Person p = new Person(String.valueOf(r.nextInt(n / 2)), randomString(), r.nextInt(100));
            persons.add(p);
        }
        return persons;
    }

    static boolean checkEqualList(int[] a, int[] b) {
        Set<Integer> sa = new HashSet<>();
        for (int i : a) {
            sa.add(i);
        }
        Set<Integer> sb = new HashSet<>();
        for (int i : b) {
            sb.add(i);
        }
        return sa.equals(sb);
    }

    public static void main(String[] args) {
        System.out.println(checkEqualList(new int[]{1, 2, 3, 4, 4}, new int[]{1, 2, 3, 4, 5}));
        // var persons = randomList(1000);
        // var dedup = persons.stream().distinct().sorted((o1, o2) ->
        // Integer.parseInt(o1.id) - Integer.parseInt(o2.id)).toList();
        // for (var p : dedup) {
        // System.out.println(p);
        // }
        // var set = new HashSet<Person>(persons);
        // for (var p : set) {
        // System.out.println(p);
        // }
        // for (var p : set) {
        // System.out.println(p);
        // }
    }

}

class Person {

    String id;
    String name;
    int age;

    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }

}

class Person2 extends Person {

    public Person2(String id, String name, int age) {
        super(id, name, age);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id) + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (Objects.equals(this.name, other.name)) {
            return true;
        }
        return Objects.equals(this.id, other.id);
    }

}
