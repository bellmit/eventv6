<!DOCTYPE html>
<html lang="en" style="overflow: hidden;">
<head>
    <meta charset="UTF-8">
    <title>图片查看器</title>
    <link rel="stylesheet" href="${uiDomain}/web-assets/extend//picGallery/jquery-photo-gallery-master/jquery-photo-gallery/photoGallery.css"/>
    <script src="${uiDomain}/web-assets/extend//picGallery/jquery-photo-gallery-master/jquery-photo-gallery/jquery.js"></script>
    <script src="${uiDomain}/web-assets/extend//picGallery/jquery-photo-gallery-master/jquery-photo-gallery/jquery.photo.gallery.js"></script>
</head>
<body>
<div class="box">
	<header drag>
		<div class="winControl" noDrag>
	        <span class="closeWin" title="关闭"><i class="icon_close-big"></i></span>
	    </div>
	</header>
	<div class="gallery"></div>
</div>
<script>
	$.initGallery();
</script>
</body>
</html>
