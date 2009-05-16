<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${template.title}</title>
<link rel="stylesheet" href="/static/css/demostyles.css" type="text/css" media="screen" charset="utf-8"/>
</head>
<body>
<div id="wrap">
	<div class="header">
		<!-- TITLE -->
		<h1><a href="#">${template.title}</a></h1>		
		<!-- END TITLE -->
	</div>
	<div class="page">
		<div id="navbg">
			<div id="nav">
				${template.sidebar}
			</div>
		</div>

		<div class="page-wrap">
			<div class="content">
				<!-- CONTENT -->
				${template.content}
				<!-- END CONTENT -->

			</div>

			<div class="sidebar">
				${template.sidebar}
			</div>

			<div class="clear"></div>

			<div class="footer-navigation">
				<ul>
					<li><a href="http://www.spyka.net" title="spyka Webmaster resources">spyka webmaster</a></li>
					<li><a href="http://www.justfreetemplates.com" title="free web templates">Free web templates</a></li>
					<li><a href="http://www.spyka.net/forums" title="webmaster forums">Webmaster forums</a></li>
					<li><a href="http://www.profileartist.net" title="premium templates">Premium templates</a></li>
					<li><a href="http://www.awesomestyles.com" title="free phpbb themes">phpBB3 styles</a></li>
				</ul>
				<ul>
					<li><a href="#">Vivamus malesuada turpis ac erat.</a></li>
					<li><a href="#">Fusce vestibulum est ut felis.</a></li>
					<li><a href="#">Proin consequat odio ut diam.</a></li>
					<li><a href="#">Vivamus quis arcu id lacust.</a></li>
					<li><a href="#">Proin sed magna eget libero.</a></li>
				</ul>
				<div class="searchbox">
					<form action="#">
						<p>
							<input type="text" name="q" value="Enter your search terms" />
							<input type="submit" value="Search" class="formbutton" />
						</p>
					</form>
				</div>
				<div class="clear"></div>
			</div>
			<div class="footer">
				<p><a href="http://validator.w3.org/check/referer" title="valid XHTML">XHTML</a> | <a href="http://jigsaw.w3.org/css-validator/check/referer" title="valid CSS">CSS</a> &nbsp;&nbsp; &copy; YourWebsiteName. Design: <a href="http://www.spyka.net">spyka webmaster</a> | <a href="http://www.justfreetemplates.com">Free Web Templates</a></p>
			</div>
		</div>
	</div>
</div>
</html>
