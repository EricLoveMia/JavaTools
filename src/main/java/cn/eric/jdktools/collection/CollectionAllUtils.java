package cn.eric.jdktools.collection;


import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: CollectionAllUtils
 * @Description: 集合工具类，整合list set map 等
 * @company lsj
 * @date 2019/8/5 18:26
 **/
public class CollectionAllUtils {

    private static Maps maps;
    private static Lists lists;
    private static Sets sets;

    static {
       maps = new Maps();
       lists = new Lists();
       sets = new Sets();
    }

    private CollectionAllUtils() {
    }

    public static Maps mapUtil() {
        return maps;
    }
    public static Lists listUtil() {
        return lists;
    }
    public static Sets setUtil() {
        return sets;
    }

    private static class Maps<K extends Comparable<? super K>,T> {

        /** map value 转 list */
        public ArrayList<T> mapValueToList(Map<K,T> map){
            Collection<T> mapC = map.values();
            return new ArrayList<>(mapC);
        }

        /** map value 转 set */
        public Set<T> mapValueToSet(Map<K,T> map){
            Collection<T> mapC = map.values();
            return new HashSet<>(mapC);
        }

        /** map key 转 list */
        public ArrayList<K> mapKeyToList(Map<K,T> map){
            return new ArrayList<>(map.keySet());
        }

        /** map key 转 set */
        public Set<K> mapKeyToSet(Map<K,T> map){
            return map.keySet();
        }

        /**
         * 自定义排序方式，不穿就是按照自然顺序
         * @author Eric
         * @date 9:41 2019/8/6
         * @param map
         * @param comparator
         * @throws
         * @return java.util.Map<K,T>
         **/
        public Map<K, T> sortByKeyAndComparator(Map<K, T> map, Comparator<K> comparator) {
            if(CollectionUtils.isEmpty(map)){
                return null;
            }
            Map<K, T> treeMap;
            //treeMap适用于按自然顺序或自定义顺序遍历键(key)
            if(comparator != null) {
                treeMap = new TreeMap<>(comparator);
            }else{
                treeMap = new TreeMap<>(Comparator.naturalOrder());
            }

            treeMap.putAll(map);
            return treeMap;
        }

        /**
         * 根据value排序
         * @author Eric
         * @date 9:41 2019/8/6
         * @param map
         * @param asc true 升序 false 降序
         * @throws
         * @return java.util.Map<K,V>
         **/
        public <K, T extends Comparable<? super T>> Map<K, T> sortByValue(Map<K, T> map, boolean asc)
        {
            Map<K, T> result = new LinkedHashMap<>();
            Stream<Map.Entry<K, T>> stream = map.entrySet().stream();
            if (asc) //升序
            {
                //stream.sorted(Comparator.comparing(e -> e.getValue()))
                stream.sorted(Map.Entry.<K, T>comparingByValue())
                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
            }
            else //降序
            {
                //stream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                stream.sorted(Map.Entry.<K, T>comparingByValue().reversed())
                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
            }
            return result;
        }

        /**
         * 根据key排序
         * @author Eric
         * @date 9:41 2019/8/6
         * @param map
         * @param asc true 升序 false 降序
         * @throws
         * @return java.util.Map<K,V>
         **/
        public <K extends Comparable<? super K>, T> Map<K, T> sortByKey(Map<K, T> map, boolean asc)
        {
            Map<K, T> result = new LinkedHashMap<>();
            Stream<Map.Entry<K, T>> stream = map.entrySet().stream();
            if (asc)
            {
                stream.sorted(Map.Entry.comparingByKey())
                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
            }
            else
            {
                stream.sorted(Map.Entry.<K, T>comparingByKey().reversed())
                        .forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
            }
            return result;
        }

        /** 打印map */
        public String mapToString(Map<K, T> map){
            StringBuilder builder = new StringBuilder();
            Iterator<Map.Entry<K, T>> iterator = map.entrySet().iterator();

            while (iterator.hasNext()){
                Map.Entry<K, T> next = iterator.next();
                builder.append(next.getKey());
                builder.append('=').append('"');
                builder.append(next.getValue());
                builder.append('"');
                if (iterator.hasNext()) {
                    builder.append(',').append(' ');
                }
            }
            return builder.toString();
        }

    }

    /** 列表相关操作  */
    private static class Lists<T extends Comparable<? super T>> {
        /** 非空 */
        public boolean isEmpty(List<T> list){
            return CollectionUtils.isEmpty(list);
        }

        /** 打印 */
        public String print(List<T> list) throws NoSuchMethodException, IllegalAccessException {
            // 得到泛型的类型
            Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Field[] declaredFields = entityClass.getDeclaredFields();
            boolean toString = entityClass.getMethod("toString") != null;
            StringBuilder builder = new StringBuilder();

            for (T t : list) {
                if(toString) {
                    builder.append(t.toString());
                }else{
                    for (Field declaredField : declaredFields) {
                        builder.append(declaredField.getName() + " = " + declaredField.get(t)  + " , ");
                    }
                }
            }

            System.out.println(builder.toString());
            return builder.toString();
        }

        /**
         * 根据传入的function排序 不传就是默认的排序方式
         * @author Eric
         * @date 11:32 2019/8/8
         * @param list
         * @param func
         * @param asc
         * @throws
         * @return java.util.List<T>
         **/
        public List<T> sort(List<T> list, Function func,boolean asc){
            if(func != null) {
                Collections.sort(list, Comparator.comparing(func));
            }else{
                Collections.sort(list);
            }
            if(asc) {

            }else{
                Collections.reverse(list);
            }
            return list;
        }

        /**
         * 默认方式排序 可选择是否倒序
         * @author Eric
         * @date 11:34 2019/8/8
         * @param list
         * @param asc false 倒叙
         * @throws
         * @return java.util.List<T>
         **/
        public List<T> sort(List<T> list, boolean asc){
            return sort(list,null,asc);
        }

        /**
         * 默认方式排序 正序
         * @author Eric
         * @date 11:34 2019/8/8
         * @param list
         * @throws
         * @return java.util.List<T>
         **/
        public List<T> sort(List<T> list){
            return sort(list,null,true);
        }

        /** 过滤 */
        public List<T> filter(List<T> list, Predicate<T> predicate){
            return list.stream().filter(predicate).collect(Collectors.toList());
        }

        /** 多条件 过滤 */
        public List<T> filter(List<T> list, Predicate<T>[] predicates){
            Stream<T> stream = list.stream();
            for (int i = 0; i < predicates.length; i++) {
                stream.filter(predicates[i]);
            }
            return  stream.collect(Collectors.toList());
        }

        /** 合并，并集  交集 = intersection; 并集 = union; 补集 = complement; 补集 = complement*/
        public List<T> intersection(List<T> list1,List<T> list2){
            List<T> temp = list1;
            temp.retainAll(list2);
            return temp;
        }

        /** 并集 */
        public List<T> union(List<T> list1,List<T> list2){
            List<T> temp = list1;
            // 先取差集 再取并集
            temp.removeAll(list2);
            temp.addAll(list2);
            return temp;
        }

        /** 去重 要重写equals hashcode 方法*/
        public List<T> distinctDefault(List<T> list){
            return list.stream().distinct().collect(Collectors.toList());
        }

        /** 转数组 */
        public T[] toArray(List<T> list){
            return (T[]) list.toArray();
        }

        /** 变成线程安全的*/
        public List<T> changeToSafe(List<T> list){
            return Collections.synchronizedList(list);
        }

    }


    /** 列表相关操作  */
    private static class Sets<T extends Comparable<? super T>> {

        /** 打印 */


        /** 排序 */


        /** 分组 */


        /** 过滤 */


        /** 合并，并集 */


        /** 交集 */


        /** 去重 */


        /** 转数组 */


        /** 变成线程安全的Set*/




    }
}
