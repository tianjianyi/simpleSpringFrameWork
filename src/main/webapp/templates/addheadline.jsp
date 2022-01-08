<%@ page pageEncoding="utf-8"%>
<html>
<head>
    <title>测试页面4</title>
</head>
<body>
    <table>
        <h2>提交表单</h2>
        <form id="headlineInfo" method="post" action="/simpleframework/headline/add">
            名：<input type="text" name="lineName"><br>
            链接：<input type="text" name="lineLink"><br>
            图片：<input type="text" name="lineImg"><br>
            优先级：<input type="text" name="priority"><br>
            结果：<h3>返回状态：${result.code} 信息: ${result.msg}</h3><br>
            <input type="submit" value="提交">
        </form>
    </table>
</body>
</html>