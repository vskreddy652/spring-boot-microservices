<%@ page isELIgnored="false"%>

<!doctype html>
<html lang="en">

<head>
<meta charset="utf-8">
<title>Mentor On Demand Portal</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/x-icon" href="favicon.ico">
<link href="css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="css/styles.css" rel="stylesheet" type="text/css">

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-1.12.4.js" type="text/javascript"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/jquery_004.js" type="text/javascript"></script>

<script type="text/javascript">
var contextPath = "http://${hostName}:${port}";

$(document).ready(function() {

  $('.password').pstrength();

  $("#form1 #password").keyup(function () {
      var textValue = $(this).val();
      if (textValue == "") {
         $("#password_text").find("span").remove();
         $("#password_bar").css("border", "1px solid #ace4e9");
         $("#password_bar").css("margin-top", "5px");
         $("#password_bar").css("font-size", "1px");
         $("#password_bar").css("height", "5px");
         $("#password_bar").css("width", "0px");
      }
  });
  
  $.descByFieldName = function (name) {
    var text;
    switch (name) {
      case "password":
        text = "New Pasword";
        break;
      case "repassword":
        text = "Confirm New Password";
        break;
      default:
        text = "";
    }
    return text;
  };
	
  $("#form1 #resetPasswd").click(function(){
	  var status = true;

      $("#form1 .requiredfield").css("border", "1px solid #cacaca");
      $("#form1 .requiredfield").each(function () {
         var v = $(this).val();
         if (v == '') {
             $(this).css("border", "solid 1px #f00");
             $(this).parent().children(".blankfield").html($.descByFieldName(this.name) + " required");
             $(this).parent().children(".blankfield").addClass("errorMessage");
             status = false;
         } else {
            $(this).parent().children(".blankfield").html("");
         }
      });

      var pas = $("#form1 #password").val();
      var repas = $("#form1 #repassword").val();
      if (pas != repas) {
         $("#form1 #repassword").css("border", "solid 1px #f00");
         $("#form1 #repassword").css("border", "solid 1px #f00");
         $("#form1 #repassword").parent().children(".blankfield").html("Password & Confirm Password should be same");
         $("#form1 #repassword").parent().children(".blankfield").addClass("errorMessage");
         status = false;
      }	  
			
	  if(status){
		 var user = { 
		    "userName" : $("#form1 #userName").val(),
			"password" : $("#form1 #password").val(),
			"token" : $("#form1 #token").val()
		 };
         $.ajax({
           type : "POST",
		   url : contextPath+"/users/resetPassword",
		   data: JSON.stringify(user),
		   cache : false,
		   dataType : "json",
		   beforeSend: function(xhr) { 
			   $('#wait').show(); 
			   xhr.setRequestHeader("Accept", "application/json");
	           xhr.setRequestHeader("Content-Type", "application/json");
		   },
		   complete: function() { $('#wait').hide(); },
		   success: function (jsonResponse) {
			   alert(jsonResponse.message);
			   $("#form1 #password").val("");
			   $("#form1 #repassword").val("");
			   $("#password_text").find("span").remove();
		       $("#password_bar").css("border", "1px solid #ace4e9");
		       $("#password_bar").css("margin-top", "5px");
		       $("#password_bar").css("font-size", "1px");
		       $("#password_bar").css("height", "5px");
		       $("#password_bar").css("width", "0px");
		   },
		   error: function(xhr, errorType, exception){
			  try {
			     responseText = jQuery.parseJSON(xhr.responseText);
			     alert(responseText.Message);
			  }catch (e) {
			     responseText = xhr.responseText;
			     alert(responseText);
			  }
		   }
	    });
     }
  });

  $(window).load(function () {
	  
      $("#bodyTD").css("height", (GetHeight() - 198));
      //$("#bodyDiv").css("width", GetWidth() - 300);
      
  });

  function GetWidth() {
      var x = 0;
      if (self.innerWidth) {
        x = self.innerWidth;
      } else if (document.documentElement && document.documentElement.clientHeight) {
        x = document.documentElement.clientWidth;
      } else if (document.body) {
        x = document.body.clientWidth;
      }
      return x;
  };

  function GetHeight() {
      var y = 0;
      if (self.innerHeight) {
        y = self.innerHeight;
      } else if (document.documentElement && document.documentElement.clientHeight) {
        y = document.documentElement.clientHeight;
      } else if (document.body) {
        y = document.body.clientHeight;
      }
      return y;
  };
    
});

window.history.forward(1);
function noBack() {
	window.history.forward();
}

</script>

</head>

<body oncontextmenu="return false;" onLoad="noBack();" onpageshow="if(event.persisted) noBack();" onUnload="">

	<table cellpadding="0" cellspacing="0" border="0" class="container theme-showcase" role="main">
		<!--Header Part-->
		<tr>
			<td>
				<div style="text-align: center" id="logo-head">
					<div align="center">
						<h1 style="color: #FFFFFF; font-size: 35px; text-shadow: 2px 2px #333333;">
							Mentor On Demand Online Portal
						</h1>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr valign="top">
			<td id="bodyTD" style="width: 100%; overflow-y: scroll; float: left; overflow: auto;" align="center">

				<div class="rectangleBox" style="width:600px">
					<form id="form1" name="form1">
						<input type="hidden" name="token" id="token" readonly="readonly" value="${userDtls.token}" />
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td colspan="2">
									<h4>Reset Password</h4>
								</td>
							</tr>
							<tr>
								<td width="120px" align="right">
								   <label>User Name:&nbsp;</label>
								</td>
								<td width="500px">
								   <input type="text" name="userName" id="userName" readonly="readonly" value="${userDtls.userName}" />
								</td>
							</tr>
							<tr>
								<td align="right">
								    <label><font color='red'>*</font>New Password:&nbsp;</label>
								</td>
								<td>
								    <input type="password" name="password" id="password" placeholder="Password" class="requiredfield password" maxlength="25">
								</td>
							</tr>
							<tr>
								<td align="right">
								    <label><font color='red'>*</font>Confirm New Password :&nbsp;</label>
								</td>
								<td>
								    <input name="repassword" id="repassword" placeholder="Confirm Password" class="requiredfield" type="password" maxlength="25">
									&nbsp;&nbsp;<span class="blankfield"></span>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<td style="height: 50px;">
								   <input class="normalbutton" value="Reset Password" type="button" id="resetPasswd" />
								</td>
							</tr>
						</table>
					</form>
				</div>

			</td>
		</tr>
		<!--Footer Part-->
		<tr>
			<td>
				<div style="text-align: right" id="footer">Mentor On
					Demand&nbsp;</div>
			</td>
		</tr>
	</table>

	<div class="wait" id="wait"></div>

</body>

</html>