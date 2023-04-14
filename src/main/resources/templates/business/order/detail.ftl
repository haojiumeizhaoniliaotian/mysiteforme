<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>订单详情</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <style type="text/css">
        .layui-form-item .layui-inline{ width:33.333%; float:left; margin-right:0; }
        @media(max-width:1240px){
            .layui-form-item .layui-inline{ width:100%; float:none; }
        }
        .layui-form-item .role-box {
            position: relative;
        }
        .layui-form-item .role-box .jq-role-inline {
            height: 100%;
            overflow: auto;
        }

        /**
            设置 name=requestContent 的文本域高度为260px；
         */
        .layui-form-item .requestContent {
            height: 260px;
        }


    </style>
</head>
<body class="childrenBody">
<form class="layui-form" style="width:80%;">
    <input class="layui-hide" name="id" value="${order.id}"/>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">订单号</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" name="orderNo" disabled value="${order.orderNo}">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">订单状态</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" name="statusText" disabled value="${order.statusText}">
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">激活账号</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" name="nickName" disabled value="${order.account}">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">订单金额</label>
            <div class="layui-input-block">
                <input type="numer" class="layui-input" name="price" disabled value="${order.price?string('#.##')}">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label">下单时间</label>
            <div class="layui-input-block">
                <input type="text" class="layui-input" name="placeOrderTime" disabled value="${order.placeOrderTime?string('yyyy-MM-dd hh:mm:ss')}">
            </div>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">请求内容</label>
        <div class="layui-input-block">
            <textarea name="requestContent" disabled class="layui-textarea requestContent">${order.requestContent}</textarea>
        </div>
    </div>
</form>
<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script>
    var index = parent.layer.getFrameIndex(window.name); //当前窗口索引
    layui.use(['form','jquery','layer'],function(){
        var form = layui.form,
                $    = layui.jquery,
                layer = layui.layer,
                delFlage = ${order.delFlag?string};
    });
</script>
</body>
</html>