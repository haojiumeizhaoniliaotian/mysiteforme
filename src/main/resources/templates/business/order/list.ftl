<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>订单列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <link rel = "shortcut icon" href="${site.logo}">
    <link rel="stylesheet" href="${base}/static/layui/css/layui.css" media="all" />
    <link rel="stylesheet" href="${base}/static/css/user.css" media="all" />
</head>
<body class="childrenBody">
<fieldset class="layui-elem-field">
    <legend>订单搜索</legend>
    <div class="layui-field-box">
    <form class="layui-form">
        <div class="layui-inline" style="width: 15%">
            <input type="text" value="" name="s_key" placeholder="可以输入订单号" class="layui-input search_input">
        </div>
        <div class="layui-inline">
            <a class="layui-btn" lay-submit="" lay-filter="searchForm">查询</a>
        </div>
<#--        <div class="layui-inline">-->
<#--            <a class="layui-btn layui-btn-normal" data-type="addOrder">添加订单</a>-->
<#--        </div>-->
    </form>
    </div>
</fieldset>
<div class="layui-form users_list">
    <table class="layui-table" id="test" lay-filter="demo"></table>
    <script type="text/html" id="statusText">
        {{# if(d.status == 2 || d.status == 3 || d.status == 4){ }}
        <span class="layui-badge layui-bg-blue">{{ d.statusText }}</span>
        {{#  }else if(d.status == 5){ }}
        <span class="layui-badge layui-bg-green">{{ d.statusText }}</span>
        {{#  }else if(d.status == -1){ }}
        <span class="layui-badge layui-bg-red">{{ d.statusText }}</span>
        {{#  } else { }}
        <span class="layui-badge layui-bg-gray">{{ d.statusText }}</span>
        {{#  } }}
    </script>
    <script type="text/html" id="barDemo">
        <a class="layui-btn layui-btn-xs" lay-event="detail">查看详情</a>
    </script>
</div>
<div id="page"></div>
<script type="text/javascript" src="${base}/static/layui/layui.js"></script>
<script type="text/javascript" src="${base}/static/js/tools.js"></script>
<script>
    layui.use(['layer','form','table'], function() {
        var layer = layui.layer,
                $ = layui.jquery,
                form = layui.form,
                table = layui.table,
                t;                  //表格数据变量

        t = {
            elem: '#test',
            url:'${base}/biz/order/list',
            method:'post',
            page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
                layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局
                //,curr: 5 //设定初始在第 5 页
                groups: 2, //只显示 1 个连续页码
                first: "首页", //显示首页
                last: "尾页", //显示尾页
                limits:[3,10, 20, 30]
            },
            width: $(parent.window).width()-223,
            cols: [[
                {type:'checkbox'},
                {field:'orderNo', title: '订单号'},
                {field:'account',  title: '激活账号',    width:'13%'},
                {field:'placeOrderTime',     title: '下单时间',    width:'15%' , templet:'<div>{{ layui.laytpl.toDateString(d.placeOrderTime) }}</div>'},
                {field:'price',       title: '订单金额（元）',    width:'13%'},
                {field:'statusText',    title: '订单状态',width:'12%', templet: '#statusText'},
                {field:'createDate',  title: '接收时间',width:'15%',templet:'<div>{{ layui.laytpl.toDateString(d.createDate) }}</div>',unresize: true}, //单元格内容水平居中
                {fixed:'right',    width: '10%', align: 'center',toolbar: '#barDemo'}
            ]]
        };
        table.render(t);

        //监听工具条
        table.on('tool(demo)', function(obj){
            var data = obj.data;
            if(obj.event === 'detail'){
                var editIndex = layer.open({
                    title : "查看详情",
                    type : 2,
                    content : "${base}/biz/order/detail?id="+data.id,
                    success : function(layero, index){
                        setTimeout(function(){
                            layer.tips('点击此处返回订单列表', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3
                            });
                        },500);
                    }
                });
                //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                $(window).resize(function(){
                    layer.full(editIndex);
                });
                layer.full(editIndex);
            }
        });

        //功能按钮
        var active={
            addOrder : function(){
                var addIndex = layer.open({
                    title : "添加订单",
                    type : 2,
                    content : "${base}/biz/order/add",
                    success : function(layero, addIndex){
                        setTimeout(function(){
                            layer.tips('点击此处返回订单列表', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3
                            });
                        },500);
                    }
                });
                //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                $(window).resize(function(){
                    layer.full(addIndex);
                });
                layer.full(addIndex);
            },
            deleteSome : function(){                        //批量删除
                var checkStatus = table.checkStatus('test'),
                     data = checkStatus.data;
                if(data.length > 0){
                    layer.confirm("你确定要删除这些订单么？",{btn:['确定','取消']},
                        function(){
                            var deleteindex = layer.msg('删除中，请稍候',{icon: 16,time:false,shade:0.8});
                            $.ajax({
                                type:"POST",
                                url:"${base}/biz/order/deleteSome",
                                dataType:"json",
                                contentType:"application/json",
                                data:JSON.stringify(data),
                                success:function(res){
                                    layer.close(deleteindex);
                                    if(res.success){
                                        layer.msg("删除成功",{time: 1000},function(){
                                            table.reload('test', t);
                                        });
                                    }else{
                                        layer.msg(res.message);
                                    }
                                }
                            });
                        }
                    )
                }else{
                    layer.msg("请选择需要删除的订单",{time:1000});
                }
            }
        };

        $('.layui-inline .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        //搜索
        form.on("submit(searchForm)",function(data){
            t.where = data.field;
            table.reload('test', t);
            return false;
        });

    });
</script>
</body>
</html>