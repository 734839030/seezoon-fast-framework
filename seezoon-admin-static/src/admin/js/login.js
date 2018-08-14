$(function() {
	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			$("#login").click();
		}
	});
	$('input:checkbox').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' // optional
	});
	$("#login").click(function() {
		var username = $.trim($("input[name='username']").val());
		var password = $.trim($("input[name='password']").val());
		if (!username) {
			layer.msg("用户名不能为空", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (username.length < 2) {
			layer.msg("用户名过短", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (!password) {
			layer.msg("密码不能为空", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		if (password.length < 6) {
			layer.msg("密码不能少于6位", {
				offset : 'm',
				anim : 6
			});
			return false;
		}
		$.post(adminContextPath + "/login.do", {
			username : username,
			password : password,
			rememberMe : $("input[name='rememberMe']").val()
		}, function(respone) {
			if (respone.responeCode == "0") {
				sessionStorage.clear();
				window.location.href = "/admin/pages/index.html";
			}
		});
	});
	//个人中心
	$.post(adminContextPath + "/user/getUserInfo.do",function(respone){
		if (respone.responeCode == '0') {
			window.location.href = "/admin/pages/index.html";
		}
    });
});