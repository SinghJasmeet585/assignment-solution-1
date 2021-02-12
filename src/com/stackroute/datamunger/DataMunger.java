package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
 *
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 *
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 *
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 *
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 *
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 *
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 *
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class DataMunger {

    /*
     * This method will split the query string based on space into an array of words
     * and display it on console
     */

    public String[] getSplitStrings(String queryString) {
        String queryLower = queryString.toLowerCase();
        String[] splitString = queryLower.split(" ");
        return splitString;
    }

    /*
     * Extract the name of the file from the query. File name can be found after a
     * space after "from" clause. Note: ----- CSV file can contain a field that
     * contains from as a part of the column name. For eg: from_date,from_hrs etc.
     *
     * Please consider this while extracting the file name in this method.
     */

    public String getFileName(String queryString) {
        String queryLower = queryString.toLowerCase();
        String[] splitFrom = queryString.split("from ");
        String[] splitSpace = splitFrom[1].split(" ");
        return splitSpace[0].trim();
    }

    /*
     * This method is used to extract the baseQuery from the query string. BaseQuery
     * contains from the beginning of the query till the where clause
     *
     * Note: ------- 1. The query might not contain where clause but contain order
     * by or group by clause 2. The query might not contain where, order by or group
     * by clause 3. The query might not contain where, but can contain both group by
     * and order by clause
     */

    public String getBaseQuery(String queryString) {

        if (queryString.contains("where")) {
            String[] splitWhere = queryString.split(" where");
            return splitWhere[0];
        } else if (queryString.contains("group by")) {
            String[] splitGroup = queryString.split(" group by");
            return splitGroup[0];
        } else if (queryString.contains("order by")) {
            String[] splitOrder = queryString.split(" order by");
            return splitOrder[0];
        } else if (queryString.contains("group by") && queryString.contains("order by")) {
            String[] splitGroup = queryString.split(" group by");
            return splitGroup[0];
        }

        return queryString;
    }

    /*
     * This method will extract the fields to be selected from the query string. The
     * query string can have multiple fields separated by comma. The extracted
     * fields will be stored in a String array which is to be printed in console as
     * well as to be returned by the method
     *
     * Note: 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
     * name can contain '*'
     *
     */

    public String[] getFields(String queryString) {
        String queryLower = queryString.toLowerCase();
        if (queryString.contains("*")) {
            String[] stringAns = {"*"};
            return stringAns;
        }
        String[] splitFrom = queryLower.split(" from ");
        String[] splitSelect = splitFrom[0].split("select ");
        splitSelect[1].replaceAll(" ", "");
        String[] splitAns = splitSelect[1].split(",");
        return splitAns;
    }

    /*
     * This method is used to extract the conditions part from the query string. The
     * conditions part contains starting from where keyword till the next keyword,
     * which is either group by or order by clause. In case of absence of both group
     * by and order by clause, it will contain till the end of the query string.
     * Note:  1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    public String getConditionsPartQuery(String queryString) {
        if (queryString.contains("where")) {
            String[] stringSplit = queryString.split("where");
            String[] stringOrderBy = stringSplit[1].trim().split("order by");
            String[] stringGroupBy = stringOrderBy[0].trim().split("group by");
            String stringAns = stringGroupBy[0];
            return stringAns.toLowerCase().trim();
        }
        return null;
    }

    /*
     * This method will extract condition(s) from the query string. The query can
     * contain one or multiple conditions. In case of multiple conditions, the
     * conditions will be separated by AND/OR keywords. for eg: Input: select
     * city,winner,player_match from ipl.csv where season > 2014 and city
     * ='Bangalore'
     *
     * This method will return a string array ["season > 2014","city ='bangalore'"]
     * and print the array
     *
     * Note: ----- 1. The field name or value in the condition can contain keywords
     * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
     * might not contain where clause at all.
     */

    //season > 2014 and city ='Bangalore' or date > '31-12-2014'
    public String[] getConditions(String queryString) {
        queryString = getConditionsPartQuery(queryString.toLowerCase());
        if (queryString == null) return null;
        String[] queryAndOr = queryString.split(" and | or ");
        for (int i = 0; i < queryAndOr.length; i++) {
            String temp2 = queryAndOr[i].trim();
            queryAndOr[i] = temp2;
        }
        return queryAndOr;
    }

    /*
     * This method will extract logical operators(AND/OR) from the query string. The
     * extracted logical operators will be stored in a String array which will be
     * returned by the method and the same will be printed Note:  1. AND/OR
     * keyword will exist in the query only if where conditions exists and it
     * contains multiple conditions. 2. AND/OR can exist as a substring in the
     * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
     * these as well when extracting the logical operators.
     *
     */

    public String[] getLogicalOperators(String queryString) {
        String[] splitSpace = queryString.split(" ");
        ArrayList<String> list = new ArrayList<>();
        if(queryString.contains("where ")) {
            if (queryString.contains(" and ") || queryString.contains(" or ") || queryString.contains(" not ")) {
                for (String values : splitSpace) {
                    if (values.equals("and")) list.add(values);
                    if (values.equals("or")) list.add(values);
                    if (values.equals("not")) list.add(values);
                }
                String[] answerLogical = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    answerLogical[i] = list.get(i);
                }
                return answerLogical;
            }
        }
        return null;
    }

    /*
     * This method extracts the order by fields from the query string. Note:
     * 1. The query string can contain more than one order by fields. 2. The query
     * string might not contain order by clause at all. 3. The field names,condition
     * values might contain "order" as a substring. For eg:order_number,job_order
     * Consider this while extracting the order by fields
     */

    public String[] getOrderByFields(String queryString) {
        //String queryLower = queryString.toLowerCase();
        if (queryString.contains("order by")) {
            String[] splitOrderBy = queryString.split("order by");
            String[] splitAnswer = new String[1];
            splitAnswer[0] = splitOrderBy[1].trim();
            return splitAnswer;
        }
        return null;
    }

    /*
     * This method extracts the group by fields from the query string. Note:
     * 1. The query string can contain more than one group by fields. 2. The query
     * string might not contain group by clause at all. 3. The field names,condition
     * values might contain "group" as a substring. For eg: newsgroup_name
     *
     * Consider this while extracting the group by fields
     */

    public String[] getGroupByFields(String queryString) {
        if (queryString.contains("group by")) {
            String[] splitGroupBy = queryString.split("group by");
            String[] splitAnswer = new String[1];
            splitAnswer[0] = splitGroupBy[1].trim();
            return splitAnswer;
        }
        return null;
    }

    /*
     * This method extracts the aggregate functions from the query string. Note:
     *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
     * followed by "(" 2. The field names might
     * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
     * account_number,consumed_qty,nominee_name
     *
     * Consider this while extracting the aggregate functions
     */

    public String[] getAggregateFunctions(String queryString) {
        String queryLower = queryString.toLowerCase();
        ArrayList<String> list = new ArrayList<>();
        String[] querySpace = queryString.split(" ");
        String[] queryAggregate = querySpace[1].split(",");
        boolean flag = false;
        for (int i = 0; i < queryAggregate.length; i++) {
            if (queryAggregate[i].contains("max(") || queryAggregate[i].contains("min(") || queryAggregate[i].contains("count(")
                    || queryAggregate[i].contains("avg(") || queryAggregate[i].contains("sum(")) {
                list.add(queryAggregate[i]);
                flag = true;
            }
        }
        String[] answer = new String[list.size()];
        if (flag == true) {
            for (int i = 0; i < list.size(); i++) {
                answer[i] = list.get(i);
            }
            return answer;
        } else {
            return null;
        }

    }

}