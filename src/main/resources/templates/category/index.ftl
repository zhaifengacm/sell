<html>
<#include "../common/head.ftl">
<body>
<div id="wrapper" class="toggled">
<#--边栏-->
    <#include "../common/nav.ftl">
<#--主要内容区域-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form role="form" method="post" action="/sell/seller/category/save">
                        <div class="form-group">
                            <label>名称</label>
                            <input type="text" class="form-control" name="categoryName"
                                   value="${(category.categoryName)!''}"/>
                        </div>
                        <div class="form-group">
                            <label>Type</label>
                            <input type="number" class="form-control" name="categoryType"
                                   value="${(category.categoryType)!''}"/>
                        </div>

                        <input type="text" name="categoryId" value="${(category.categoryId)!''}" hidden>
                        <button type="submit" class="btn btn-default">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>