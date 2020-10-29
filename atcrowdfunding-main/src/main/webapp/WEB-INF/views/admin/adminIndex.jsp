<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    </style>
</head>

<body>
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
                    <%--条件查询--%>
                    <form id="queryForm" action="${appPath}/admin/index" method="post" class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input name="keyWord" class="form-control has-success" value="${param.keyWord}" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" onclick="$('#queryForm').submit()" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <sec:authorize access="hasRole('大师') and hasAuthority('项目经理')">
                        <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;" id="deleteBath"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                        <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${appPath}/admin/toAdd'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    </sec:authorize>
                    <br>

                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox" id="checkAll"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${pageInfo.list}" var="admin" varStatus="state" >
                                <tr>
                                    <td>${state.count}</td>
                                    <td><input type="checkbox" class="adminCheckBox" id="${admin.id}"></td>
                                    <td>${admin.loginacct}</td>
                                    <td>${admin.username}</td>
                                    <td>${admin.email}</td>
                                    <td>
                                        <button  type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                        <button type="button"  class="btn btn-primary btn-xs" onclick="location.href='${appPath}/admin/toEdit?id=${admin.id}&pageNum=${pageInfo.pageNum}'"><i class=" glyphicon glyphicon-pencil"></i></button>
                                        <button type="button" class="btn btn-danger btn-xs" onclick="location.href='${appPath}/admin/delete?id=${admin.id}&pageNum=${pageInfo.pageNum}'"><i class=" glyphicon glyphicon-remove"></i></button>
                                    </td>
                                </tr>
                            </c:forEach>

                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                        <%--如果当前页是第一页，则上一页功能禁用--%>
                                        <c:if test="${pageInfo.isFirstPage}">
                                            <li class="disabled"><a href="#">首  页</a></li>
                                            <li class="disabled"><a href="#">上一页</a></li>
                                        </c:if>
                                        <c:if test="${not pageInfo.isFirstPage}">
                                            <li><a href="${appPath}/admin/index?pageNum=${1}&keyWord=${param.keyWord}">首  页</a></li>
                                            <li><a href="${appPath}/admin/index?pageNum=${pageInfo.pageNum-1}&keyWord=${param.keyWord}">上一页</a></li>
                                        </c:if>
                                        <c:forEach items="${pageInfo.navigatepageNums}" var="i" >
                                            <c:if test="${pageInfo.pageNum==i}">
                                                <li class="active"><a href="${appPath}/admin/index?pageNum=${i}&keyWord=${param.keyWord}">${i} <span class="sr-only">(current)</span></a></li>
                                            </c:if>
                                            <c:if test="${pageInfo.pageNum!=i}">
                                                <li><a href="${appPath}/admin/index?pageNum=${i}&keyWord=${param.keyWord}">${i} <span class="sr-only">(current)</span></a></li>
                                            </c:if>
                                        </c:forEach>
                                        <%--如果当前页是最后一页，则下一页功能禁用--%>
                                        <c:if test="${pageInfo.isLastPage}">
                                            <li class="disabled"><a href="#">下一页</a></li>
                                            <li class="disabled"><a href="#">尾  页</a></li>
                                        </c:if>
                                        <c:if test="${not pageInfo.isLastPage}">
                                            <li><a href="${appPath}/admin/index?pageNum=${pageInfo.pageNum+1}&keyWord=${param.keyWord}">下一页</a></li>
                                            <li><a href="${appPath}/admin/index?pageNum=${pageInfo.pages}&keyWord=${param.keyWord}">尾  页</a></li>
                                        </c:if>
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
    $(function () {
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

        var checkAll = $("#checkAll");//总复选框  checkAll jquery 对象

        var adminCheckBoxs = $(".adminCheckBox");//每一行中的复选框  adminCheckBoxs集合

        checkAll.click(function () {
            //复选框的选中效果 checked属性
            // 把jQuery对象转换成 DOM  (checkAll[0].checked)   checkAll.get(0)
            $.each(adminCheckBoxs, function (i, checkBox) {// checkBox DOM对象
                checkBox.checked = checkAll[0].checked;
            });
        });
      /*  var num1=  prompt("请输入一个数字");*/

        $("#deleteBath").click(function () {

            var adminChecks = $(".adminCheckBox:checked");
            if (adminChecks.length == 0) {
                layer.msg("没有选中要删除的选项!!", {time: 2000, icon: 5, shift: 6});

            } else {
                /*var loginacct = $(this).parent().parent().find("td:eq(2)").html();  可以获得单个的loginacct的值*/
                layer.confirm("是否删除这【"+adminChecks.length+"】项?", {btn: ['yes', 'no']},
                    function () {
                        /*  保存所有的id值 ids 数组*/
                        var ids = new Array();
                        for (var i = 0; i < adminChecks.length; i++) {
                            var id = $(adminChecks[i]).attr("id");
                            ids.push(id);
                        }
                        location.href = "${appPath}/admin/deleteBath?ids=" + ids;// *,* 字符串*!/

                    },
                    function () {
                        layer.alert("取消删除");
                    });

            }
        });


    });

</script>
</body>
</html>

