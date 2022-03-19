package com.josh;

import java.util.List;
import java.util.ArrayList;

public class Subsets {
    public static List<List<Integer>> subsets(int nums[]) {
        List<List<Integer>> subsets = new ArrayList<>();
        generateSubsets(0, nums, new ArrayList<Integer>(), subsets);
        return subsets;
    }

    public static void generateSubsets(int index, int nums[], List<Integer> tmpSubset, List<List<Integer>> subsets) {
        subsets.add(new ArrayList<>(tmpSubset));
        System.out.println("Added "+tmpSubset.toString()+" to subsets, subsets is now "+subsets.toString());
        for (int i = index; i < nums.length; ++i) {
            tmpSubset.add(nums[i]);
            System.out.println("Added "+nums[i]+" to tmp, tmp is now "+tmpSubset.toString());
            generateSubsets(i + 1, nums, tmpSubset, subsets);
            System.out.print("Removed "+tmpSubset.get(tmpSubset.size()-1)+" from tmp, tmp is now ");
            tmpSubset.remove(tmpSubset.size() - 1);
            System.out.println(tmpSubset.toString());
        }
    }

    public static void main(String[] args) {
        int items[] = { 1, 2, 3 };
        List<List<Integer>> subsets = subsets(items);
        System.out.println(subsets.toString());
    }
}
