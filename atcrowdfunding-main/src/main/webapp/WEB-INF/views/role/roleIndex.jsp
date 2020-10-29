<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <%@include file="/WEB-INF/common/css.jsp"%>
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}

        a:hover{cursor: pointer}  /*当a标签的href属性没有了的时候，悬浮指针就没了原本的形式，这个是添加回原来的形式*/

    </style>
</head>

<body>
<%--用于角色的添加，修改。控制它的显示和隐藏--%>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">新增</h4>
            </div>
            <div class="modal-body">

                <form role="form"  method="post">

                    <div class="form-group">
                        <label for="exampleInputPassword1">角色名称</label>
                        <input name="name" type="text" class="form-control" id="exampleInputPassword1" placeholder="请输入角色名称">
                    </div>

                </form>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" id="btnAdd">保存</button>
            </div>
        </div>
    </div>
</div>


<%@include file="/WEB-INF/common/nav.jsp"%>

<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/common/side.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input name="keyWord" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="btnSearchByKeyWord" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <sec:authorize access="hasRole('大师') or hasAuthority('GL - 组长')">
                        <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;" id="deleteBath"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                        <button type="button" class="btn btn-primary" style="float:right;" id="btnSave"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    </sec:authorize>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox" id="checkAll"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="tbody">
                            <%--使用ajax来异步请求角色信息--%>

                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <%--使用异步请求进行分页--%>
                                    </ul>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/common/js.jsp"%>
<script src="${appPath}/static/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {   //页面加载完成后执行
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });


        //在页面加载完成后执行异步请求，进行角色信息的 第一次显示
        loadData(1);//第一次显示时，仅显示第一页
    });

    //首先获取查询条件
    var keyWord = "";//定义全局变量，先定义为空串

    //异步请求，获取角色信息和分页信息
    function loadData(pageNum){
        $.getJSON("${appPath}/role/loadData",{"pageNum":pageNum,"pageSize":2,"keyWord":keyWord},
            function(data){
                if(data=="403"){
                    layer.msg("没有权限查询！！！",{time:2000,icon:5,shift:6},function(){
                        $("tbody").html("<tr><td colspan='5' rowspan='2' style='text-align: center;background: white'><h3 >没有权限</h3></td></tr>");
                    });
                }else{
                    showRoles(data.list,data.pageNum);//显示角色信息

                    showPage(data);//显示分页
                }


        });
    }

    function showRoles(roleList,pageNum){
        var content = "";
        for(var i=0;i<roleList.length;i++){
            content+='<tr>';
            content+='  <td>'+(i+1)+'</td>';
            content+='  <td><input type="checkbox" class="roleCheck" id="'+(roleList[i].id)+'"></td>';
            content+='  <td>'+(roleList[i].name)+'</td>';
            content+=' <td>';
            content+='	  <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';
            content+='	  <button type="button" class="btn btn-primary btn-xs" onclick="getRole('+(roleList[i].id)+')"><i class=" glyphicon glyphicon-pencil"></i></button>';
            content+='	  <button type="button" class="btn btn-danger btn-xs" onclick="deleteRoleById('+(roleList[i].id)+','+(pageNum)+')"><i class=" glyphicon glyphicon-remove"></i></button>';
            content+='  </td>';
            content+='</tr>';
        }

        $("#tbody").html(content);
    }

    function showPage(pageInfo){
        var content ="";
        if(pageInfo.isFirstPage){
            content+='<li class="disabled"><a href="#">首  页</a></li>';
            content+='<li class="disabled"><a href="#">上一页</a></li>';
        }else{
            content+='<li><a onclick="loadData('+1+')">首  页</a></li>';
            content+='<li><a onclick="loadData('+(pageInfo.pageNum-1)+')">上一页</a></li>';
        }
        for(var i=0;i<pageInfo.navigatepageNums.length;i++){
            if(pageInfo.pageNum==pageInfo.navigatepageNums[i]){
                content+='<li class="active"><a onclick="loadData('+(pageInfo.navigatepageNums[i])+')">'+(pageInfo.navigatepageNums[i])+' <span class="sr-only">(current)</span></a></li>';
            }else{
                content+='<li><a onclick="loadData('+(pageInfo.navigatepageNums[i])+')">'+(pageInfo.navigatepageNums[i])+' <span class="sr-only">(current)</span></a></li>';
            }
        }

        if(pageInfo.isLastPage){
            content+='<li class="disabled"><a href="#">下一页</a></li>';
            content+='<li class="disabled"><a href="#">尾  页</a></li>';
        }else{
            content+='<li><a onclick="loadData('+(pageInfo.pageNum+1)+')">下一页</a></li>';
            content+='<li><a onclick="loadData('+(pageInfo.pages)+')">尾  页</a></li>';
        }

        $('.pagination').html(content);
    }

    $("#btnSearchByKeyWord").click(function(){
        keyWord = $(":input[name='keyWord']").val();
        loadData(1);
    });


    $("#btnSave").click(function(){
        $('#myModal').modal({
            show:true,  /*成功在原来的页面上方弹出一个模块*/
            backdrop:'static',
            keyboard:false  /*false:点击超出模块区域的位置，模块不会关闭*/
        });
        //将模态框上的改成添加，并销毁或许存在的角色文本框的value值
        $("#myModalLabel").html("添加");
        $(":input[name='name']").val("");
    });

    $("#btnAdd").click(function(){
        //获取要进行添加的信息
        var roleName = $(":input[name='name']").val();
        //获取按钮btnAdd(当前)的idStr属性,查看这个值是否为空，为空执行添加操作，不为空执行修改操作
        var idStr = $(this).attr("idStr");

        if(idStr==null||idStr==""){//执行添加操作
            //异步请求，进行信息的传递与显示
            $.get("${appPath}/role/saveRole",{"name":roleName},function(result){

                if(result=="yes"){
                    layer.msg("角色添加成功",{time:500,icon:6},function(){
                        //添加成功后要关闭模态框
                        $("#myModal").modal("hide");
                        //跳转到添加的数据页面
                        loadData(10000);
                    });
                }else{
                    layer.msg("添加角色失败！",{time:1000,icon:5});
                }

            });
        }else{//执行修改操作     idStr里的值时id值
            var pageNum = $(".active a").text();

            $.post("${appPath}/role/updateRole",{"id":idStr,"name":roleName},function(result){
                if(result=="yes"){
                    layer.msg("修改成功！",{time:1000,icon:6},function(){
                       $("#myModal").modal("hide");//修改完毕，模态框关闭
                        loadData(pageNum.charAt(0));//没保存修改的页码，所以先让他调到第一页
                        $("#btnAdd").attr("idStr","");//立即销毁btnAdd里保存的idStr值
                    });
                }
            });

        }


    });

    function getRole(id){
        $.getJSON("${appPath}/role/getRole",{"id":id},function(tRole){
            //使用之前的添加的模态框，当点击时，将上方的添加改成修改，并将角色信息显示在文本框中
            $('#myModal input[name="name"]').val(tRole.name);
            $("#myModalLabel").html("修改");
            //给btnAdd按钮添加上idStr属性，当有idStr的值时，表示的是进行修改操作
            $("#btnAdd").attr("idStr",id);

            //准备工作完成，弹出模态框
            $("#myModal").modal({
                show:true,
                backdrop:'static',//背景样式
                keyboard:false//鼠标点击模态框外，是否关闭模态框
            });
        });
    }

    $("#checkAll").click(function(){
        var roleChecks = $("#tbody .roleCheck");//获取每一个小的复选框
        $.each(roleChecks,function (i,check) {//循环遍历复选框集合
            check.checked = $("#checkAll")[0].checked;
        });
    });

    //批量删除
    $("#deleteBath").click(function(){
        //获取所有被选中的复选框
        var pageNum = $(".active a").text();
       /* alert(pageNum.charAt(0));*/

        var checkedBoxs = $("#tbody .roleCheck:checked");
        var ids =new Array();
        for(var i=0;i<checkedBoxs.length;i++){
            ids.push($(checkedBoxs[i]).attr("id"));  //将所有被选中的复选框的id属性值添加进ids集合（这些id是tRole的id值）
        }
        layer.confirm("是否删除这些角色信息？",{btn:["确定","取消"]},
            function () {//确定的操作==》删除
                $.get("${appPath}/role/deleteBath?ids="+ids,null,
                    function(result){
                        if(result=="yes"){
                            layer.msg("删除成功！",{time:500,icon:6});
                            loadData(pageNum.charAt(0));
                            $("#checkAll").attr("checked",false);//删除完毕，大的复选按钮变成没有被选中
                        }
                    }
                );
            },
            function () {
                layer.msg("取消删除！",{time:500});
            }
        )
    });

    function deleteRoleById(id,pageNum){
        layer.confirm("确认删除？",{btn:["确定","取消"]},
            function () {
                $.get("${appPath}/role/deleteRoleById",{"id":id},function(result){
                    if(result=="yes"){
                        layer.msg("删除成功!!",{time:1000,icon:6});
                        loadData(pageNum);
                    }
                });
            },
            function () {
                layer.msg("取消删除",{time:500});
            }
        );

    }

</script>
</body>
</html>

