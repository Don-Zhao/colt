
$(document).ready(function(){
	$("#getCode").click(function(){
		$.ajax({
			type: "POST",
			contentType: "application/x-www-form-urlencoded",
			url: "http://localhost:8080/user/getCode",
			data:{
				email: $("#email").val()
			},
			success: function(data) {
				if (data.status == "success") {
					alert("验证码已经发送");
				} else {
					alert("发送失败，因为" + data.result.errorMsg);
				}
			},
			error: function(data) {
				alert(data.responseText);
				var obj = eval('(' + data.responseText + ')');
				
				if (obj.message.contains(":")) {
					alert("发送失败，因为" + obj.message.split(":")[1].trim());
				} else {
					alert(obj.message);
				}
				
			}
		});
	});
});