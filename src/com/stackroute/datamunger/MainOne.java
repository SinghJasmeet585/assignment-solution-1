package com.stackroute.datamunger;

import java.util.Scanner;

public class MainOne {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string=scanner.nextLine();
        DataMunger data=new DataMunger();

       // System.out.println(data.getConditionsPartQuery(string));
        String[] ans=data.getGroupByFields(string);
        for(int i=0;i< ans.length;i++) {
            System.out.println(ans[i]);
        }




    }
}
