<%--
  Created by IntelliJ IDEA.
  User: meidejing
  Date: 2016/6/15
  Time: 15:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link type="text/css" rel="stylesheet" href="public/css/bootstrap.min.css">
    <link type="text/css" rel="stylesheet" href="public/css/bootstrap-datetimepicker.min.css">
    <link type="text/css" rel="stylesheet" href="public/css/bootstrap-select.min.css">


    <style>
        .fromDiv{
              margin: auto;
              padding: 20px;
           }
    </style>
</head>
<body>
<div class="fromDiv">
    <form class="form-horizontal" role="form">
        <div class="form-group">
            <label for="promotionType" class="col-sm-2 control-label">推广类型:</label>
            <div class="col-sm-3">
                <select id="promotionType" name="promotionType" class="form-control">
                    <option value="subwayServiceImpl" selected>直通车</option>
                    <option value="taobaoCustomersServiceImpl">淘宝客</option>
                    <option value="majibaoServiceImpl">麻吉宝</option>
                    <option value="diamondBoothServiceImpl">钻石展</option>
                    <option value="productBaoServiceImpl">品销宝</option>
                    <option value="accountantServiceImpl">账房</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label for="promotionType" class="col-sm-2 control-label">抓取频率:</label>
            <div class="col-sm-3">
                <div class="radio">
                    <label>
                        <input type="radio" name="grabFrequency" id="optionsRadios1" value="day" checked>
                        按天抓取（抓取所选日期之前15天数据）
                    </label>
                </div>
                <div class="radio">
                    <label>
                        <input type="radio" name="grabFrequency" id="optionsRadios2" value="week">
                        按周抓取（抓取上周7天统计数据）
                    </label>
                </div>
                <div class="radio">
                    <label>
                        <input type="radio" name="grabFrequency" id="optionsRadios3" value="month">
                        按月抓取（抓取上个月统计数据）
                    </label>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="account" class="col-sm-2 control-label">店铺:</label>
            <div class="col-sm-3">
                    <select id="account" name="account" class="selectpicker show-tick form-control" hideDisabled="true" multiple data-live-search="false">
                        <%--<option value="0">苹果</option>--%>
                        <%--<option value="1">菠萝</option>--%>
                    </select>
            </div>
        </div>
        <div class="form-group">
            <label for="dtp_input2" class="col-sm-2 control-label">日期：</label>
            <div class="input-group date form_date col-sm-2" data-date="" data-date-format="yyyy-mm-dd"
                 data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                <input class="form-control" size="16" type="text" value="2016-08-28" readonly>

                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
            </div>
            <input type="hidden" id="dtp_input2" value="2016-08-28" /><br/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" id="submitButton" class="btn btn-info">提交</button>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript" src="public/js/jquery.min.js"></script>
<script type="text/javascript" src="public/js/bootstrap.js"></script>
<script type="text/javascript" src="public/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="public/js/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript"  src="public/js/bootstrap-select.min.js"></script>

<script type="text/javascript">
    $(function () {
        var shopList=[];
        $("#promotionType").on("change",function(){
            var promotionType=$(this).val();
            if(promotionType=="accountantServiceImpl"){
                $("#optionsRadios1").attr("disabled","disabled");
                $("#optionsRadios2").attr("disabled","disabled");
                $("#optionsRadios3").attr("checked","checked");
            }else {
                $("#optionsRadios1").removeAttr("disabled").attr("checked","checked");
                $("#optionsRadios2").removeAttr("disabled");
            }
            $("#account").empty();
            var optionStr="";
            $.each(shopList,function(i,item){
                switch (promotionType){
                    case "subwayServiceImpl":
                        if(item.subwayflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    case "taobaoCustomersServiceImpl":
                        if(item.taobaocustomersflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    case "majibaoServiceImpl":
                        if(item.majibaoflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    case "diamondBoothServiceImpl":
                        if(item.diamondflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    case "productBaoServiceImpl":
                        if(item.productflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    case "accountantServiceImpl":
                        if(item.accountantflag==1){
                            var option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                        }
                        optionStr+=option;
                        break;
                    default:
                        break;
                }
            });
            $("#account").append(optionStr);
            $("#account").selectpicker('refresh');

        });

        $.ajax({
            type: "get",
            url: "promotion/getshopList",
            dataType: "json",
            beforeSend: function() {
                $("#account").empty();
            },
            success: function (data) {
                shopList=data;
                var optionStr="";
                $.each(data,function(i,item){
                   var option="";
                   if(item.subwayflag==1){
                       option="<option value='"+item.id+"'>"+item.id+"_"+item.loginName+"</option>";
                   }
                   optionStr+=option;
                });
                $("#account").append(optionStr);
                $("#account").selectpicker('refresh');
            },
            error:function(){
                alert("获取店铺列表失败");
            }
        });


        $('#account').selectpicker({
            'noneSelectedText': '请选择店铺'
        });
       var nowDate=new Date();
       var nowStr=nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"-"+nowDate.getDate();
        $(".form_date").parent().find("input").val(nowStr);
        $('.form_date').datetimepicker({
            language: 'zh-CN',
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0
        });


        $("#submitButton").on("click", function () {
            var promotionType = $("#promotionType").val();
            var grabFrequency = $('input[name="grabFrequency"]:checked').val();
            var accountArr = $("#account").val();
            var accountStr="";
            if(!accountArr||accountArr.length==0){
                alert("请选择店铺！！");
                return;
            }else{
                accountStr=accountArr.join('|');
            }
            var dateStr = $("#dtp_input2").val();
            if(!dateStr){
                alert("请选择日期！！");
                return;
            }

            var url = "promotion/" + promotionType + "/" + accountStr + "/" + dateStr + "/" + grabFrequency;
            $.ajax({
                type: "get",
                url: url,
                dataType: "text",
                beforeSend: function (data) {
                    $("#submitButton").attr({"disabled": "disabled"});
                },
                success: function (data) {
                    alert("提交成功！");
                    $("#submitButton").removeAttr("disabled");
                },
                error:function(){
                    alert("提交失败！！");
                    $("#submitButton").removeAttr("disabled");
                }
            });


        });
    });

</script>

</body>
</html>
