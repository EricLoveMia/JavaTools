package cn.eric.jdktools.file;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Company: 苏州渠成易销网络科技有限公司
 *
 * @version 1.0.0
 * @description: Java获取指定包名下的所有类的全类名的解决方案
 * @author: 钱旭
 * @date: 2022-01-04 16:02
 **/
public class ClazzUtils {
    private static final String CLASS_SUFFIX = ".class";
    private static final String CLASS_FILE_PREFIX = File.separator + "classes"  + File.separator;
    private static final String PACKAGE_SEPARATOR = ".";

    /**
     * 查找包下的所有类的名字
     * @param packageName
     * @param showChildPackageFlag 是否需要显示子包内容
     * @return List集合，内容为类的全名
     */
    public static List<Class<?>> getClazz(String packageName, boolean showChildPackageFlag ) {
        List<Class<?>> result = new ArrayList<>();
        String suffixPath = packageName.replaceAll("\\.", "/");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = loader.getResources(suffixPath);
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if("file".equals(protocol)) {
                        String path = url.getPath();
                        System.out.println(path);
                        result.addAll(getAllClassNameByFile(new File(path), showChildPackageFlag));
                    } else if("jar".equals(protocol)) {
                        JarFile jarFile = null;
                        try{
                            jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                        } catch(Exception e){
                            e.printStackTrace();
                        }
                        if(jarFile != null) {
                            result.addAll(getAllClassNameByJar(jarFile, packageName, showChildPackageFlag));
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 递归获取所有class文件的名字
     * @param file
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<Class<?>> getAllClassNameByFile(File file, boolean flag) throws ClassNotFoundException {
        List<Class<?>> result =  new ArrayList<>();
        if(!file.exists()) {
            return result;
        }
        if(file.isFile()) {
            String path = file.getPath();
            // 注意：这里替换文件分割符要用replace。因为replaceAll里面的参数是正则表达式,而windows环境中File.separator="\\"的,因此会有问题
            if(path.endsWith(CLASS_SUFFIX)) {
                path = path.replace(CLASS_SUFFIX, "");
                // 从"/classes/"后面开始截取
                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                        .replace(File.separator, PACKAGE_SEPARATOR);
                if(!clazzName.contains("$")) {
                    result.add(Class.forName(clazzName));
                }
            }
            return result;

        } else {
            File[] listFiles = file.listFiles();
            if(listFiles != null && listFiles.length > 0) {
                for (File f : listFiles) {
                    if(flag) {
                        result.addAll(getAllClassNameByFile(f, flag));
                    } else {
                        if(f.isFile()){
                            String path = f.getPath();
                            if(path.endsWith(CLASS_SUFFIX)) {
                                path = path.replace(CLASS_SUFFIX, "");
                                // 从"/classes/"后面开始截取
                                String clazzName = path.substring(path.indexOf(CLASS_FILE_PREFIX) + CLASS_FILE_PREFIX.length())
                                        .replace(File.separator, PACKAGE_SEPARATOR);
                                if(!clazzName.contains("$")) {
                                    result.add(Class.forName(clazzName));
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }
    }


    /**
     * 递归获取jar所有class文件的名字
     * @param jarFile
     * @param packageName 包名
     * @param flag	是否需要迭代遍历
     * @return List
     */
    private static List<Class<?>> getAllClassNameByJar(JarFile jarFile, String packageName, boolean flag) throws ClassNotFoundException {
        List<Class<?>> result =  new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while(entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 判断是不是class文件
            if(name.endsWith(CLASS_SUFFIX)) {
                name = name.replace(CLASS_SUFFIX, "").replace("/", ".");
                if(flag) {
                    // 如果要子包的文件,那么就只要开头相同且不是内部类就ok
                    if(name.startsWith(packageName) && !name.contains("$")) {
                        result.add(Class.forName(name));
                    }
                } else {
                    // 如果不要子包的文件,那么就必须保证最后一个"."之前的字符串和包名一样且不是内部类
                    if(packageName.equals(name.substring(0, name.lastIndexOf("."))) && !name.contains("$")) {
                        result.add(Class.forName(name));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 判断是否是基础数据类型的包装类型
     *
     * @param clz
     * @return
     */
    public static boolean isWrapClass(Class<?> clz) {
        String typeName = clz.getTypeName();
        if("java.lang.String".equals(typeName) || clz == Long.class
                || clz == Short.class || clz == Byte.class || clz == Integer.class
                || clz == Double.class || clz == Float.class || clz == Boolean.class){
            return true;
        }
        return false;
    }

    /**
     * 判断指定类是否是List的子类或者父类
     *
     * @param clz
     * @return
     */
    public static boolean isListOrSetTypeClass(Class<?> clz) {
        try {
            return List.class.isAssignableFrom(clz) || clz.newInstance() instanceof List
                     || Set.class.isAssignableFrom(clz) || clz.newInstance() instanceof Set;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断指定类是否是数组
     *
     * @param clz
     * @return
     */
    public static boolean isArrayTypeClass(Class<?> clz) {
        return clz.isArray();
    }
}

