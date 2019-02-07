/*
This program solves the following problem.

Suppose we have an integer n and a list of integers ns.

For each integer in ns, we consider all of the possible arithmetic expressions that can be created
by inserting operators between its digits.

For example, for the integer 3100100, we can create the following expressions:

3*100*100
3*100+100
3*100-100
3*100100
3+100*100
...

For each integer in ns, we want to print all expressions that evaluate to n.

If no expressions exist that evaluate to n, then we print 'impossible'.

Both n and the integers in ns are entered by the user from the command line.

Example output:

Enter an integer. Then enter as many valid sequences of digits as you would like.
Enter each sequence on a separate line. When you are done entering sequences, simply press enter.

3000
3100100
55

3*100*10+0
3*100*10-0
3100-100

impossible

*/

import java.util.*;
import java.io.*;

public class Expressions {

    private static final double numOpTypes = 4; 
    private static final List<Character> ops = Arrays.asList('+', '-', '*');

    private static StringBuilder[] genExpressions(String str) {
        double numOps = str.length() - 1;
        int numExprs = (int) Math.pow(numOpTypes, numOps);
        StringBuilder[] exprs = new StringBuilder[numExprs];

        for (int i = 0; i < numExprs; i++) {
            exprs[i] = new StringBuilder();
            exprs[i].append(str);
        }

        int numOpTypesInt = (int) numOpTypes;

        for (int j = 0; j < numOps; j++) {
            for (int i = 0; i < numExprs; i++) {
                switch ((i / (numExprs / (int) Math.pow(numOpTypes, (double) (j + 1)))) % numOpTypesInt) {
                    case 0: exprs[i].insert(j * 2 + 1, "+");
                            break;
                    case 1: exprs[i].insert(j * 2 + 1, "-");
                            break;
                    case 2: exprs[i].insert(j * 2 + 1, "*");
                            break;
                    case 3: exprs[i].insert(j * 2 + 1, "|");
                            break;  
                }
            }
        }

        return exprs;
    }

    private static void removePipeChars(StringBuilder[] sb_arr) {
        for (StringBuilder sb : sb_arr) {
            for (int j = 0; j < sb.length(); j++) {
                if (sb.charAt(j) == '|') { sb.deleteCharAt(j); }
            }
        }
    }

    private static String[] sbArrToStrArr(StringBuilder[] sb_arr) {
        int len = sb_arr.length;
        String[] str_arr = new String[len];

        for (int i = 0; i < len; i++) {
            str_arr[i] = sb_arr[i].toString();
        }

        return str_arr;
    }

    private static boolean isValidExpr(String str) {
        boolean ret = true;
        int len = str.length();

        if (str.charAt(0) == '0' && !ops.contains(str.charAt(1))) { ret = false; };

        for (int i = 2; i < len - 1; i++) {
            if (ops.contains(str.charAt(i - 1)) && str.charAt(i) == '0' && !ops.contains(str.charAt(i + 1))) {
                ret = false;
            }
        }

        return ret;
    }

    private static String[] removeInvalidExprs(String[] strs) {
        return Arrays.stream(strs).filter(x -> isValidExpr(x)).toArray(String[]::new);
    }

    private static int evaluate(String str) {
        int ret = 0;

        if (str.contains("+")) {
            String[] toAdd = str.split("\\+", 2);

            int num_a = evaluate(toAdd[0]);
            int num_b = evaluate(toAdd[1]);

            ret = num_a + num_b;
        } else if (str.contains("-")) {
            String[] toSubtract = str.split("\\-", 2);

            int num_a = evaluate(toSubtract[0]);
            int num_b = evaluate(toSubtract[1]);

            ret = num_a - num_b;
        } else if (str.contains("*")) {
            String[] toMultiply = str.split("\\*");

            ret = 1;

            for (String factor : toMultiply) {
                ret *= Integer.parseInt(factor);
            }
        } else { ret = Integer.parseInt(str); }

        return ret;
    }

    public static void main(String args[]) throws IOException {

        System.out.println("Enter an integer. Then enter as many valid sequences of digits as you would like.\nEnter each sequence on a separate line. When you are done entering sequences, simply press enter.\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int user_int = Integer.parseInt(br.readLine());

        String user_expr = br.readLine();
        ArrayList<String> user_exprs = new ArrayList<String>();    


        while (user_expr.matches(".*\\d+.*")) {
            user_exprs.add(user_expr);
            user_expr = br.readLine();
        }

        for (String expr : user_exprs) {
            StringBuilder[] arr = genExpressions(expr);
            removePipeChars(arr);
            String[] str_arr = sbArrToStrArr(arr);
            str_arr = removeInvalidExprs(str_arr);

            boolean possible = false;
            for (String str : str_arr) {
                if (evaluate(str) == user_int) {
                    System.out.println(str);
                    possible = true;
                }
            }

            if (!possible) { System.out.println("impossible"); }

            System.out.print("\n");
        }
    }

}