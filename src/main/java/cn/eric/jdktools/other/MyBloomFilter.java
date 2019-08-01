package cn.eric.jdktools.other;

/**
 * @author Eric
 * @version 1.0
 * @ClassName: MyBloomFilter
 * @Description: 布隆过滤器
 * @date 2018/11/30 10:17
 **/
public class MyBloomFilter {

    private int[] array;

    private int arraySize;

    public MyBloomFilter(){}

    public MyBloomFilter(int arraySize ){
        this.arraySize = arraySize;
        array = new int[arraySize];
    }

    public void add(String a){
        int hash$1 = hashcode_1(a + "");
        int hash$2 = hashcode_2(a + "");
        int hash$3 = hashcode_3(a + "");

        array[hash$1%arraySize] = 1;
        array[hash$2%arraySize] = 1;
        array[hash$3%arraySize] = 1;
    }

    public boolean check(String b) {
        int hash$1 = hashcode_1(b + "");
        int hash$2 = hashcode_2(b + "");
        int hash$3 = hashcode_3(b + "");

        if(array[hash$1%arraySize] == 1 && array[hash$2%arraySize] == 1
            && array[hash$3%arraySize] == 1){
            return true;
        }
        return false;
    }


    /**
     * hash 算法1
     * @param key
     * @return
     */
    private int hashcode_1(String key) {
        int hash = 0;
        int i;
        for (i = 0; i < key.length(); ++i) {
            hash = 33 * hash + key.charAt(i);
        }
        return Math.abs(hash);
    }
    /**
     * hash 算法2
     * @param data
     * @return
     */
    private int hashcode_2(String data) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < data.length(); i++) {
            hash = (hash ^ data.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        return Math.abs(hash);
    }
    /**
     *  hash 算法3
     * @param key
     * @return
     */
    private int hashcode_3(String key) {
        int hash, i;
        for (hash = 0, i = 0; i < key.length(); ++i) {
            hash += key.charAt(i);
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return Math.abs(hash);
    }

    public static void main(String[] args) {

        long timebegin = System.currentTimeMillis();
        MyBloomFilter filter = new MyBloomFilter(100000000);
        for (int i = 0; i < 10000000; i++) {
            filter.add(i + "");
        }
        System.out.println(filter.check(4 + ""));
        System.out.println(filter.check(67 + ""));
        System.out.println(filter.check(267 + ""));
        System.out.println(filter.check(22267 + ""));
        System.out.println(filter.check(99999 + ""));
        System.out.println(filter.check(488999992 + ""));

        long end = System.currentTimeMillis();
        System.out.println("执行时间：" + (end - timebegin));
    }
}
