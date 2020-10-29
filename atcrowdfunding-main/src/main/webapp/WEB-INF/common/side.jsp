<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="col-sm-3 col-md-2 sidebar">
    <div class="tree">
        <c:if test="${not empty sessionScope.parentList}">
            <ul style="padding-left:0px;" class="list-group">
                <c:forEach var="parent" items="${sessionScope.parentList}" >
                    <%--没有子节点的父节点--%>
                    <c:if test="${parent.childMenus.size()==0}">
                        <li class="list-group-item tree-closed" >
                            <a href="${appPath}/${parent.url}"><i class="${parent.icon}"></i> ${parent.name}</a>
                        </li>
                    </c:if>
                    <%--有子节点的父节点--%>
                    <c:if test="${parent.childMenus.size()!=0}">
                        <li class="list-group-item tree-closed">
                            <span><i class="${parent.icon}"></i> ${parent.name} <span class="badge" style="float:right">${parent.childMenus.size()}</span></span>
                            <ul style="margin-top:10px;display:none;">
                                <c:forEach var="child" items="${parent.childMenus}">
                                    <li style="height:30px;">
                                        <a href="${appPath}/${child.url}"><i class="${child.icon}"></i>${child.name}</a>
                                    </li>
                                </c:forEach>

                            </ul>
                        </li>
                    </c:if>

                </c:forEach>
            </ul>
        </c:if>
    </div>
</div>