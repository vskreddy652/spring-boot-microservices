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
	
  $("#form1 #confirm").click(function(){
      var user = { 
		  "userName" : $("#form1 #userName").val(),
		  "token" : $("#form1 #token").val()
	  };
	  $.ajax({
		type : "POST",
		url : contextPath+"/users/confirmUser",
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
									<h4>Confirm User</h4>
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
								<td>&nbsp;</td>
								<td style="height: 50px;">
								   <input class="normalbutton" value="Apply" type="button" id="confirm" />
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