package cn.eric.jdktools.bean;

import java.io.*;
import java.util.List;

/**
 * @version 1.0.0
 * @description:
 * @author: eric
 * @date: 2022-08-05 17:04
 **/
public class BeanCopyUtils {

    /**
     * Desc: 深度拷贝
     * Author: Jack
     **/
    public static <T> List<T> deepCopy(List<T> srcList) throws IOException, ClassNotFoundException {
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(srcList);

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<T> desList = (List<T>) ois.readObject();
        return desList;
    }

    /**
     * Desc: 深度拷贝
     * Author: Jack
     **/
    public static <T> T deepCopy(T src) throws IOException, ClassNotFoundException {
        //序列化
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(src);

        //反序列化
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        T des = (T) ois.readObject();
        return des;
    }
}
