/**
 * 功能1：从后台获得区域、店铺分类下拉列表信息
 * 功能2：获取前台的填写信息，发送到后台注册店铺
 */


$(function(){
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerUrl = '/o2o/shopadmin/registershop';

    //在js加载时就调用方法获取后台
    getShopInitInfo();

    function getShopInitInfo(){
        //将后台返回的数据动态生成<option>标签插入
        $.getJSON(initUrl,function(data){
            if(data.success){
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function(item,index){
                    tempHtml += '<option data-id="'+item.shopCategoryId +'">'
                    +item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml = '<option data-id="'+item.areaId +'">'
                        +item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
        //给submit按钮添加点击事件
        $('#submit').click(function () {
            var shop = {};
            shop.shopName = $('#shop-name').val();
            shop.shopAddr = $('#shop-addr').val();
            shop.phone = $('#shop-phone').val();
            shop.shopDesc = $('#shop-desc').val();

            // 选择id,双重否定=肯定
            shop.shopCategory = {
                // 这里定义的变量要和ShopCategory.shopCategoryId保持一致，否则使用databind转换会抛出异常
                shopCategoryId : $('#shop-category').find('option').not(function() {
                    return !this.selected;
                }).data('id')
            };

            shop.area = {
                // 这里定义的变量要和Area.areaId属性名称保持一致，否则使用databind转换会抛出异常
                areaId : $('#area').find('option').not(function() {
                    return !this.selected;
                }).data('id')
            };

            // 获取图片文件流
            var shopImg = $('#shop-img')[0].files[0];
            var formData = new FormData();
            formData.append('shopImg', shopImg);
            //填写的店铺信息
            formData.append('shopStr', JSON.stringify(shop));


            var verifyCodeActual = $('#j_kaptcha').val();
            if (!verifyCodeActual) {
                $.toast("请输入验证码！");
                return;
            } else {
                formData.append('verifyCodeActual', verifyCodeActual);
            }


            $.ajax({
                url:registerUrl,
                type : 'POST',
                data : formData,
                contentType : false,
                processData : false,
                cache : false,
                success : function(data) {
                    if (data.success) {
                        $.toast("提交成功！");
                    } else {
                        $.toast("提交失败！" + data.errMsg);
                    }
                    // 更换验证码
                    $('#kaptcha_img').click();
                }
            });
        })
    }
});