/**
 * CS2030S PE1 Question 2
 * AY21/22 Semester 2
 *
 * @author A0000000X
 */
import java.util.Map;
import java.util.List;
import java.util.stream.Stream;
import java.util.function.Predicate;

class Query {

    public static <T,S> Stream<Map.Entry<T, S>> getFilteredByKey(Map<T, S> table, Predicate<T> p) {
      return table.entrySet().stream().filter(x -> p.test(x.getKey()));// Write your first answer here
    }

    public static Stream<Integer> getIdsFromName(Map<String, List<Integer>> table, String s) {
      return getFilteredByKey(table, x -> x == s).flatMap(x -> x.getValue().stream());
    }

    public static Stream<Double> getCostsFromIDs(Map<Integer, Double> table, Stream<Integer> s) {
      return s.map(x -> table.get(x)).filter(x -> x != null);
    }

   public static Stream<String> allCustomerCosts(Map<String, List<Integer>> customers, Map<Integer, Double> sales) {
      return customers.keySet().stream().flatMap(key -> getCostsFromIDs(sales, getIdsFromName(customers, key)).map(y -> key + ": " + y));
   }

   public static Stream<String> totaledCustomerCosts(Map<String, List<Integer>> customers, Map<Integer,Double> sales) {
      return customers.keySet().stream().map(key -> key + ": " + getCostsFromIDs(sales, getIdsFromName(customers, key)).reduce(0D, (x, y) -> x + y));
   }
}

