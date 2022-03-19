package com.josh;

/**
 * Uses Pascal's triangle to find combinations instead of recalculating
 * factorials
 */
public class Combination {
    // maximum support is COMBINATION_MAX choose COMBINATION_MAX
    final static int COMBINATION_MAX = 45;

    public static int nChooseK(int n, int k) {
        // create pascal's triangle
        int combination[][] = new int[COMBINATION_MAX + 1][COMBINATION_MAX + 1];

        for (int i = 0; i <= COMBINATION_MAX; ++i)
            combination[i][i] = 1; // k choose k = 1

        // there is only one way of choosing an empty set from a set
        for (int i = 0; i <= COMBINATION_MAX; ++i)
            combination[i][0] = 1; // k choose 0 = 1

        for (int i = 2; i <= COMBINATION_MAX; ++i)
            for (int j = 1; j < i; ++j)
                combination[i][j] = combination[i - 1][j - 1] + combination[i - 1][j];

        return combination[n][k];
    }

    public static void main(String[] args) {
        System.out.println(nChooseK(45, 20));
    }
}
