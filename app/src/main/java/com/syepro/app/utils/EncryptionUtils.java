package com.syepro.app.utils;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 加密工具类
 */
public class EncryptionUtils {

    /**
     * 排序，把所有参数连接成一个字符串
     *
     * @param str
     */
    public static String encryption(String... str) {
        StringBuffer strs = new StringBuffer();
        if(str!=null){
            Arrays.sort(str);
            for (int i = 0; i < str.length; i++) {
                String s = str[i];
                strs.append(s);
            }
        }
        String srotStr = strs.toString();
        String md5Str = toMD5(srotStr);
        return insert(md5Str);
    }

    public static String sort(String str) {
        char chs[] = str.toCharArray();
        Arrays.sort(chs);
        return new String(chs);
    }

    private final static String appId = "337f5637b295b573e67223545b38c4bc";

    //逢偶数位插入
    private static String insert(String str) {
        StringBuffer inStrBu = new StringBuffer();
        char idChs[] = appId.toCharArray();
        char inChs[] = str.toCharArray();
        for (int i = 0; i < appId.length(); i++) {
            inStrBu.append(idChs[i]);
            inStrBu.append(inChs[i]);
        }
        return inStrBu.toString();
    }

    public static String toMD5(String plainText) {
        try {
            //生成实现指定摘要算法的 MessageDigest 对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(plainText.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 拼接带有accessToken的url
     */
    public static String bindUrl(String url, Map<String, String> params) {
        String[] sort = new String[params.size()];
        if (params == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if(value==null){
                value ="";
                entry.setValue(value);
            }
            if (builder.length() < 1) {
                builder = builder.append("?").append(entry.getKey() + "=" + value);
            } else {
                builder = builder.append("&").append(entry.getKey() + "=" + value);
            }
            sort[i] = value;
            i++;
        }
        if (builder.length() < 1) {
            builder.append("?");
        } else {
            builder.append("&");
        }
        builder = builder.append("accessToken" + "=" + encryption(sort));
        return url + builder.toString();
    }

    /**
     * post添加accessToken
     */
    public static Map<String, String> bindUrl(Map<String, String> params) {
        String[] sort = new String[params.size()];
        if (params == null) {
            params = new HashMap<>();
        }
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            sort[i] = value;
            i++;
        }
        params.put("accessToken", encryption(sort));
        return params;
    }


}
