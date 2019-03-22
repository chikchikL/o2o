/**
 * 发送更新验证码图片的请求
 * @param img
 */
function changeVerifyCode(img) {
    img.src = "../Kaptcha?" + Math.floor(Math.random()*100);
}