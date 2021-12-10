<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
	<meta charset="utf-8" /> 
	<title>轨迹回放(路书)</title> 
	<style type="text/css">
		body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
		#map_canvas{width:100%;height:500px;}
		#result {width:100%}
	</style>
	<script src="http://api.map.baidu.com/api?v=2.0&ak=OBm0kGjTPSEXKjr3igMrjT9Hgml8fGPP" type="text/javascript"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/library/LuShu/1.2/src/LuShu_min.js"></script>
</head> 
<body> 
	<div id="map_canvas"></div> 
	<div id="result"></div>
	<button id="run">开始</button> 
	<button id="stop">停止</button> 
	<button id="pause">暂停</button> 
	<button id="hide">隐藏信息窗口</button> 
	<button id="show">展示信息窗口</button>
	<script> 
    var marker;
	var map = new BMap.Map('map_canvas');
	map.enableScrollWheelZoom();
	map.centerAndZoom();
	var lushu;
    
	var arrPois = [ 
	        new BMap.Point(116.403984, 39.914004),
			new BMap.Point(116.402116, 39.913938),
			new BMap.Point(116.402116, 39.913938),
			new BMap.Point(116.402046, 39.913928),
			new BMap.Point(116.401856, 39.913927),
			new BMap.Point(116.401547, 39.913926),
			new BMap.Point(116.401008, 39.913923),
			new BMap.Point(116.400599, 39.913921),
			new BMap.Point(116.39999, 39.913908),
			new BMap.Point(116.399471, 39.913875),
			new BMap.Point(116.399042, 39.913852),
			new BMap.Point(116.398035, 39.913835),
			new BMap.Point(116.395593, 39.913683),
			new BMap.Point(116.395383, 39.913681),
			new BMap.Point(116.395114, 39.913658),
			new BMap.Point(116.394666, 39.913664),
			new BMap.Point(116.390265, 39.91349),
			new BMap.Point(116.38925, 39.913456),
			new BMap.Point(116.388931, 39.913461),
			new BMap.Point(116.388643, 39.913457),
			new BMap.Point(116.388335, 39.913463),
			new BMap.Point(116.386455, 39.913424),
			new BMap.Point(116.386166, 39.913429),
			new BMap.Point(116.385898, 39.913425),
			new BMap.Point(116.384327, 39.913399),
			new BMap.Point(116.384168, 39.913396),
			new BMap.Point(116.38384, 39.913391),
			new BMap.Point(116.383472, 39.913395),
			new BMap.Point(116.383323, 39.913402),
			new BMap.Point(116.382796, 39.913403),
			new BMap.Point(116.382747, 39.913402),
			new BMap.Point(116.38227, 39.913394),
			new BMap.Point(116.381594, 39.913372),
			new BMap.Point(116.381465, 39.91337),
			new BMap.Point(116.381445, 39.913369),
			new BMap.Point(116.381197, 39.913365),
			new BMap.Point(116.380779, 39.913348),
			new BMap.Point(116.38066, 39.913346),
			new BMap.Point(116.380491, 39.913343),
			new BMap.Point(116.380488, 39.913341),
			new BMap.Point(116.38014, 39.913344),
			new BMap.Point(116.379435, 39.913342),
			new BMap.Point(116.378848, 39.913331),
			new BMap.Point(116.378103, 39.913307),
			new BMap.Point(116.376722, 39.913282),
			new BMap.Point(116.374238, 39.913227),
			new BMap.Point(116.373105, 39.913226),
			new BMap.Point(116.37221, 39.91321),
			new BMap.Point(116.370004, 39.913171),
			new BMap.Point(116.369865, 39.913169),
			new BMap.Point(116.369706, 39.913166),
			new BMap.Point(116.369616, 39.913175),
			new BMap.Point(116.366474, 39.913103),
			new BMap.Point(116.366276, 39.913109),
			new BMap.Point(116.365997, 39.913105),
			new BMap.Point(116.365898, 39.913104),
			new BMap.Point(116.365639, 39.91312),
			new BMap.Point(116.364605, 39.913094),
			new BMap.Point(116.364127, 39.913087),
			new BMap.Point(116.364126, 39.913086),
			new BMap.Point(116.363917, 39.913193),
			new BMap.Point(116.363827, 39.913262),
			new BMap.Point(116.363797, 39.913311),
			new BMap.Point(116.363767, 39.913391),
			new BMap.Point(116.363767, 39.913541),
			new BMap.Point(116.363757, 39.913771),
			new BMap.Point(116.363757, 39.913821),
			new BMap.Point(116.363737, 39.91432),
			new BMap.Point(116.363687, 39.914809),
			new BMap.Point(116.363647, 39.915039),
			new BMap.Point(116.363617, 39.915198),
			new BMap.Point(116.363567, 39.915377),
			new BMap.Point(116.363527, 39.915527),
			new BMap.Point(116.363487, 39.915676),
			new BMap.Point(116.363438, 39.915905),
			new BMap.Point(116.363436, 39.915905),
			new BMap.Point(116.363327, 39.916154),
			new BMap.Point(116.363327, 39.916154),
			new BMap.Point(116.363356, 39.916444),
			new BMap.Point(116.363376, 39.916694),
			new BMap.Point(116.363504, 39.917936),
			new BMap.Point(116.363404, 39.918974),
			new BMap.Point(116.363384, 39.919494),
			new BMap.Point(116.363374, 39.919673),
			new BMap.Point(116.363374, 39.919723),
			new BMap.Point(116.363374, 39.919763),
			new BMap.Point(116.363374, 39.919813),
			new BMap.Point(116.363364, 39.920293),
			new BMap.Point(116.363364, 39.920393),
			new BMap.Point(116.363273, 39.922131),
			new BMap.Point(116.363263, 39.922441),
			new BMap.Point(116.363192, 39.92405),
			new BMap.Point(116.363183, 39.92422),
			new BMap.Point(116.363183, 39.9243),
			new BMap.Point(116.363173, 39.92446),
			new BMap.Point(116.363122, 39.925599),
			new BMap.Point(116.363112, 39.925818),
			new BMap.Point(116.363072, 39.926798),
			new BMap.Point(116.363052, 39.927057),
			new BMap.Point(116.363042, 39.927277),
			new BMap.Point(116.363002, 39.928407),
			new BMap.Point(116.362952, 39.929326),
			new BMap.Point(116.362952, 39.929466),
			new BMap.Point(116.362942, 39.929486),
			new BMap.Point(116.362842, 39.931454),
			new BMap.Point(116.362822, 39.931694),
			new BMap.Point(116.362822, 39.931834),
			new BMap.Point(116.362802, 39.932393),
			new BMap.Point(116.362772, 39.933293),
			new BMap.Point(116.362763, 39.933313),
			new BMap.Point(116.362683, 39.934892),
			new BMap.Point(116.362603, 39.936531),
			new BMap.Point(116.362544, 39.93769),
			new BMap.Point(116.362524, 39.93804),
			new BMap.Point(116.362514, 39.938159),
			new BMap.Point(116.362514, 39.938209),
			new BMap.Point(116.362514, 39.938259),
			new BMap.Point(116.362504, 39.938509),
			new BMap.Point(116.362465, 39.939329),
			new BMap.Point(116.362405, 39.940878),
			new BMap.Point(116.362376, 39.941888),
			new BMap.Point(116.362366, 39.942108),
			new BMap.Point(116.362366, 39.942138),
			new BMap.Point(116.362356, 39.942398),
			new BMap.Point(116.362337, 39.942597),
			new BMap.Point(116.362307, 39.943287),
			new BMap.Point(116.362277, 39.943637),
			new BMap.Point(116.362218, 39.944746),
			new BMap.Point(116.362219, 39.946046),
			new BMap.Point(116.362219, 39.946196),
			new BMap.Point(116.362219, 39.946226),
			new BMap.Point(116.362209, 39.946276),
			new BMap.Point(116.362209, 39.946326),
			new BMap.Point(116.362199, 39.946436),
			new BMap.Point(116.36218, 39.946876),
			new BMap.Point(116.36213, 39.947595),
			new BMap.Point(116.36227, 39.948167),
			new BMap.Point(116.36235, 39.948339),
			new BMap.Point(116.362509, 39.948611),
			new BMap.Point(116.362738, 39.948914),
			new BMap.Point(116.362928, 39.949197),
			new BMap.Point(116.363167, 39.94943),
			new BMap.Point(116.363416, 39.949604),
			new BMap.Point(116.363913, 39.949851),
			new BMap.Point(116.36469, 39.950052),
			new BMap.Point(116.365595, 39.950366),
			new BMap.Point(116.365794, 39.950429),
			new BMap.Point(116.366093, 39.950524),
			new BMap.Point(116.366242, 39.950556),
			new BMap.Point(116.367266, 39.950863),
			new BMap.Point(116.368162, 39.951127),
			new BMap.Point(116.371174, 39.952029),
			new BMap.Point(116.371324, 39.952072),
			new BMap.Point(116.372029, 39.952315),
			new BMap.Point(116.372417, 39.952482),
			new BMap.Point(116.37365, 39.953024),
			new BMap.Point(116.374435, 39.953378),
			new BMap.Point(116.374833, 39.953536),
			new BMap.Point(116.374962, 39.953598),
			new BMap.Point(116.375588, 39.95386),
			new BMap.Point(116.375585, 39.95386),
			new BMap.Point(116.375903, 39.953895),
			new BMap.Point(116.376171, 39.95396),
			new BMap.Point(116.376399, 39.954035),
			new BMap.Point(116.376648, 39.954099),
			new BMap.Point(116.376727, 39.954131),
			new BMap.Point(116.377026, 39.954216),
			new BMap.Point(116.377085, 39.954237),
			new BMap.Point(116.377354, 39.954312),
			new BMap.Point(116.377642, 39.954358),
			new BMap.Point(116.377821, 39.954391),
			new BMap.Point(116.378198, 39.954458),
			new BMap.Point(116.378377, 39.954491),
			new BMap.Point(116.378536, 39.954504),
			new BMap.Point(116.378586, 39.954505),
			new BMap.Point(116.378715, 39.954517),
			new BMap.Point(116.378924, 39.954531),
			new BMap.Point(116.379311, 39.954558),
			new BMap.Point(116.379848, 39.954598),
			new BMap.Point(116.379898, 39.954599),
			new BMap.Point(116.380037, 39.954611),
			new BMap.Point(116.380723, 39.954664),
			new BMap.Point(116.381458, 39.954697),
			new BMap.Point(116.381736, 39.954712),
			new BMap.Point(116.382432, 39.954744),
			new BMap.Point(116.38273, 39.954759),
			new BMap.Point(116.38279, 39.95476),
			new BMap.Point(116.383098, 39.954795),
			new BMap.Point(116.383456, 39.954882),
			new BMap.Point(116.383536, 39.954903),
			new BMap.Point(116.383576, 39.954914),
			new BMap.Point(116.383655, 39.954935),
			new BMap.Point(116.383695, 39.954946),
			new BMap.Point(116.383795, 39.954987),
			new BMap.Point(116.383854, 39.955019),
			new BMap.Point(116.383884, 39.955029),
			new BMap.Point(116.383954, 39.95507),
			new BMap.Point(116.384013, 39.955121),
			new BMap.Point(116.384063, 39.955162),
			new BMap.Point(116.384093, 39.955183),
			new BMap.Point(116.384153, 39.955244),
			new BMap.Point(116.384212, 39.955315),
			new BMap.Point(116.384242, 39.955345),
			new BMap.Point(116.384382, 39.955518),
			new BMap.Point(116.384441, 39.955609),
			new BMap.Point(116.384501, 39.95568),
			new BMap.Point(116.384581, 39.955811),
			new BMap.Point(116.38468, 39.955953),
			new BMap.Point(116.384849, 39.956206),
			new BMap.Point(116.384919, 39.956317),
			new BMap.Point(116.385059, 39.956529),
			new BMap.Point(116.385079, 39.95657),
			new BMap.Point(116.385347, 39.956974),
			new BMap.Point(116.385527, 39.957267),
			new BMap.Point(116.385576, 39.957358),
			new BMap.Point(116.385626, 39.957459),
			new BMap.Point(116.385676, 39.95757),
			new BMap.Point(116.385716, 39.95768),
			new BMap.Point(116.385776, 39.957841),
			new BMap.Point(116.385806, 39.958002),
			new BMap.Point(116.385836, 39.958162),
			new BMap.Point(116.385846, 39.958293),
			new BMap.Point(116.385846, 39.958473),
			new BMap.Point(116.385856, 39.958623),
			new BMap.Point(116.385856, 39.958623),
			new BMap.Point(116.385897, 39.960014),
			new BMap.Point(116.385898, 39.960984),
			new BMap.Point(116.385908, 39.961295),
			new BMap.Point(116.385929, 39.961865),
			new BMap.Point(116.385909, 39.962295),
			new BMap.Point(116.38588, 39.963275),
			new BMap.Point(116.385831, 39.963644),
			new BMap.Point(116.385782, 39.964123),
			new BMap.Point(116.385782, 39.964143),
			new BMap.Point(116.385842, 39.964715),
			new BMap.Point(116.385903, 39.965356),
			new BMap.Point(116.385983, 39.966067),
			new BMap.Point(116.386043, 39.966538),
			new BMap.Point(116.386053, 39.966629),
			new BMap.Point(116.386133, 39.96726),
			new BMap.Point(116.386414, 39.969935),
			new BMap.Point(116.386474, 39.970407),
			new BMap.Point(116.386685, 39.9718),
			new BMap.Point(116.386805, 39.972673),
			new BMap.Point(116.386935, 39.973505),
			new BMap.Point(116.386985, 39.973796),
			new BMap.Point(116.386984, 39.973796),
			new BMap.Point(116.387035, 39.974166),
			new BMap.Point(116.387055, 39.974317),
			new BMap.Point(116.387075, 39.974427),
			new BMap.Point(116.387095, 39.974608),
			new BMap.Point(116.387175, 39.975129),
			new BMap.Point(116.387185, 39.975199),
			new BMap.Point(116.387195, 39.975309),
			new BMap.Point(116.387285, 39.975931),
			new BMap.Point(116.387385, 39.976803),
			new BMap.Point(116.387626, 39.978957),
			new BMap.Point(116.387646, 39.979127),
			new BMap.Point(116.387656, 39.979228),
			new BMap.Point(116.387736, 39.979829),
			new BMap.Point(116.387866, 39.981111),
			new BMap.Point(116.387926, 39.981592),
			new BMap.Point(116.388026, 39.982294),
			new BMap.Point(116.388016, 39.982684),
			new BMap.Point(116.388007, 39.982864),
			new BMap.Point(116.387997, 39.982953),
			new BMap.Point(116.387997, 39.983003),
			new BMap.Point(116.387997, 39.983054),
			new BMap.Point(116.387987, 39.983333),
			new BMap.Point(116.387918, 39.983782),
			new BMap.Point(116.387819, 39.984381),
			new BMap.Point(116.3877, 39.984779),
			new BMap.Point(116.386975, 39.987338),
			new BMap.Point(116.386806, 39.987906),
			new BMap.Point(116.386776, 39.988015),
			new BMap.Point(116.386727, 39.988195),
			new BMap.Point(116.386518, 39.988971),
			new BMap.Point(116.386379, 39.989529),
			new BMap.Point(116.386221, 39.990087),
			new BMap.Point(116.386012, 39.990823),
			new BMap.Point(116.385943, 39.991072),
			new BMap.Point(116.385724, 39.991819),
			new BMap.Point(116.385376, 39.992993),
			new BMap.Point(116.385347, 39.993123),
			new BMap.Point(116.385237, 39.993471),
			new BMap.Point(116.385208, 39.993591),
			new BMap.Point(116.385168, 39.99374),
			new BMap.Point(116.385128, 39.993889),
			new BMap.Point(116.385109, 39.993939),
			new BMap.Point(116.385079, 39.994048),
			new BMap.Point(116.384622, 39.995631),
			new BMap.Point(116.384552, 39.99591),
			new BMap.Point(116.384254, 39.996995),
			new BMap.Point(116.384244, 39.997034),
			new BMap.Point(116.383936, 39.997829),
			new BMap.Point(116.383906, 39.997919),
			new BMap.Point(116.383687, 39.998425),
			new BMap.Point(116.383469, 39.998841),
			new BMap.Point(116.38327, 39.999118),
			new BMap.Point(116.382982, 39.999513),
			new BMap.Point(116.382534, 40.000135),
			new BMap.Point(116.382455, 40.000253),
			new BMap.Point(116.382157, 40.000658),
			new BMap.Point(116.382067, 40.000777),
			new BMap.Point(116.381491, 40.001506),
			new BMap.Point(116.381023, 40.002088),
			new BMap.Point(116.380516, 40.002739),
			new BMap.Point(116.380188, 40.003163),
			new BMap.Point(116.379612, 40.003902),
			new BMap.Point(116.379234, 40.004385),
			new BMap.Point(116.378578, 40.005213),
			new BMap.Point(116.377942, 40.006032),
			new BMap.Point(116.377524, 40.006564),
			new BMap.Point(116.376937, 40.007333),
			new BMap.Point(116.376848, 40.007431),
			new BMap.Point(116.376789, 40.00752),
			new BMap.Point(116.376749, 40.007559),
			new BMap.Point(116.376739, 40.007589),
			new BMap.Point(116.376431, 40.007983),
			new BMap.Point(116.376043, 40.008486),
			new BMap.Point(116.374452, 40.010616),
			new BMap.Point(116.373895, 40.011406),
			new BMap.Point(116.373606, 40.011791),
			new BMap.Point(116.372303, 40.013577),
			new BMap.Point(116.371706, 40.014386),
			new BMap.Point(116.371617, 40.014514),
			new BMap.Point(116.37106, 40.015244),
			new BMap.Point(116.37097, 40.015373),
			new BMap.Point(116.370542, 40.015935),
			new BMap.Point(116.369577, 40.017208),
			new BMap.Point(116.369259, 40.017652),
			new BMap.Point(116.368711, 40.018443),
			new BMap.Point(116.368303, 40.018946),
			new BMap.Point(116.367557, 40.019883),
			new BMap.Point(116.36687, 40.020752),
			new BMap.Point(116.366183, 40.02162),
			new BMap.Point(116.364589, 40.023695),
			new BMap.Point(116.363912, 40.024585),
			new BMap.Point(116.363334, 40.025317),
			new BMap.Point(116.362975, 40.025762),
			new BMap.Point(116.362816, 40.025959),
			new BMap.Point(116.362228, 40.026711),
			new BMap.Point(116.361869, 40.027156),
			new BMap.Point(116.36164, 40.027463),
			new BMap.Point(116.361421, 40.02776),
			new BMap.Point(116.361401, 40.02779),
			new BMap.Point(116.361311, 40.027899),
			new BMap.Point(116.361304, 40.027899),
			new BMap.Point(116.361274, 40.027948),
			new BMap.Point(116.361175, 40.028107),
			new BMap.Point(116.361085, 40.028236),
			new BMap.Point(116.360995, 40.028385),
			new BMap.Point(116.360945, 40.028474),
			new BMap.Point(116.360916, 40.028524),
			new BMap.Point(116.360806, 40.028712),
			new BMap.Point(116.360706, 40.028881),
			new BMap.Point(116.360677, 40.028941),
			new BMap.Point(116.360667, 40.028961),
			new BMap.Point(116.360637, 40.02903),
			new BMap.Point(116.360597, 40.02912),
			new BMap.Point(116.360557, 40.029199),
			new BMap.Point(116.360547, 40.029219),
			new BMap.Point(116.360538, 40.029259),
			new BMap.Point(116.360518, 40.029299),
			new BMap.Point(116.360508, 40.029338),
			new BMap.Point(116.360508, 40.029388),
			new BMap.Point(116.360508, 40.029438),
			new BMap.Point(116.360508, 40.029488),
			new BMap.Point(116.360518, 40.029539),
			new BMap.Point(116.360528, 40.029589),
			new BMap.Point(116.360547, 40.029639),
			new BMap.Point(116.360577, 40.029679),
			new BMap.Point(116.360607, 40.02972),
			new BMap.Point(116.360647, 40.02976),
			new BMap.Point(116.360687, 40.029791),
			new BMap.Point(116.360726, 40.029821),
			new BMap.Point(116.360766, 40.029841),
			new BMap.Point(116.360806, 40.029862),
			new BMap.Point(116.360846, 40.029872),
			new BMap.Point(116.360896, 40.029883),
			new BMap.Point(116.360945, 40.029894),
			new BMap.Point(116.360995, 40.029904),
			new BMap.Point(116.361055, 40.029905),
			new BMap.Point(116.361115, 40.029906),
			new BMap.Point(116.361174, 40.029907),
			new BMap.Point(116.361234, 40.029887),
			new BMap.Point(116.361294, 40.029878),
			new BMap.Point(116.361354, 40.029859),
			new BMap.Point(116.361393, 40.02984),
			new BMap.Point(116.361453, 40.029811),
			new BMap.Point(116.361493, 40.029781),
			new BMap.Point(116.361533, 40.029742),
			new BMap.Point(116.361573, 40.029692),
			new BMap.Point(116.361603, 40.029643),
			new BMap.Point(116.361633, 40.029593),
			new BMap.Point(116.361643, 40.029544),
			new BMap.Point(116.361643, 40.029504),
			new BMap.Point(116.361643, 40.029464),
			new BMap.Point(116.361633, 40.029424),
			new BMap.Point(116.361623, 40.029374),
			new BMap.Point(116.361603, 40.029323),
			new BMap.Point(116.361574, 40.029273),
			new BMap.Point(116.361544, 40.029233),
			new BMap.Point(116.361504, 40.029202),
			new BMap.Point(116.361474, 40.029172),
			new BMap.Point(116.361425, 40.029141),
			new BMap.Point(116.361385, 40.029101),
			new BMap.Point(116.361325, 40.02906),
			new BMap.Point(116.361266, 40.02904),
			new BMap.Point(116.361226, 40.029009),
			new BMap.Point(116.361186, 40.028989),
			new BMap.Point(116.361147, 40.028968),
			new BMap.Point(116.361107, 40.028948),
			new BMap.Point(116.361104, 40.028946),
			new BMap.Point(116.360765, 40.028942),
			new BMap.Point(116.360676, 40.028941),
			new BMap.Point(116.360477, 40.028939),
			new BMap.Point(116.360228, 40.028936),
			new BMap.Point(116.359521, 40.028937),
			new BMap.Point(116.359292, 40.028945),
			new BMap.Point(116.358943, 40.028951),
			new BMap.Point(116.358515, 40.028946),
			new BMap.Point(116.358495, 40.028946),
			new BMap.Point(116.357638, 40.028947),
			new BMap.Point(116.357499, 40.028946),
			new BMap.Point(116.357011, 40.028931),
			new BMap.Point(116.356801, 40.028929),
			new BMap.Point(116.356792, 40.028929),
			new BMap.Point(116.356632, 40.028928),
			new BMap.Point(116.355356, 40.028937),
			new BMap.Point(116.355037, 40.028934),
			new BMap.Point(116.354628, 40.028941),
			new BMap.Point(116.354219, 40.028938),
			new BMap.Point(116.353571, 40.028934),
			new BMap.Point(116.353062, 40.028941),
			new BMap.Point(116.352872, 40.02894),
			new BMap.Point(116.351874, 40.028934),
			new BMap.Point(116.350277, 40.028937),
			new BMap.Point(116.347059, 40.028961),
			new BMap.Point(116.346519, 40.028971),
			new BMap.Point(116.343748, 40.028925),
			new BMap.Point(116.343718, 40.028925),
			new BMap.Point(116.343608, 40.028925),
			new BMap.Point(116.342968, 40.028888),
			new BMap.Point(116.341516, 40.028704),
			new BMap.Point(116.340915, 40.028537),
			new BMap.Point(116.340184, 40.028331),
			new BMap.Point(116.337588, 40.02754),
			new BMap.Point(116.337585, 40.027539),
			new BMap.Point(116.337244, 40.027532),
			new BMap.Point(116.336974, 40.027464),
			new BMap.Point(116.336522, 40.027388),
			new BMap.Point(116.336201, 40.027321),
			new BMap.Point(116.33576, 40.027336),
			new BMap.Point(116.335309, 40.02738),
			new BMap.Point(116.334907, 40.027494),
			new BMap.Point(116.334445, 40.027659),
			new BMap.Point(116.334405, 40.027679),
			new BMap.Point(116.333823, 40.027946),
			new BMap.Point(116.333311, 40.028252),
			new BMap.Point(116.332839, 40.028617),
			new BMap.Point(116.331713, 40.029801),
			new BMap.Point(116.331711, 40.0298),
			new BMap.Point(116.330726, 40.031463),
			new BMap.Point(116.330585, 40.031744),
			new BMap.Point(116.330071, 40.032911),
			new BMap.Point(116.330021, 40.033112),
			new BMap.Point(116.329437, 40.035059),
			new BMap.Point(116.329245, 40.035672),
			new BMap.Point(116.329165, 40.035913),
			new BMap.Point(116.329084, 40.036174),
			new BMap.Point(116.328984, 40.036465),
			new BMap.Point(116.328943, 40.036576),
			new BMap.Point(116.328802, 40.037018),
			new BMap.Point(116.328209, 40.038366),
			new BMap.Point(116.327716, 40.039103),
			new BMap.Point(116.32685, 40.040136),
			new BMap.Point(116.326659, 40.040299),
			new BMap.Point(116.325231, 40.041552),
			new BMap.Point(116.323168, 40.043356),
			new BMap.Point(116.323047, 40.043468),
			new BMap.Point(116.323017, 40.043489),
			new BMap.Point(116.322937, 40.04357),
			new BMap.Point(116.322806, 40.043682),
			new BMap.Point(116.322756, 40.043733),
			new BMap.Point(116.322715, 40.043764),
			new BMap.Point(116.322243, 40.044202),
			new BMap.Point(116.322233, 40.044222),
			new BMap.Point(116.321719, 40.044821),
			new BMap.Point(116.321578, 40.044983),
			new BMap.Point(116.320773, 40.046077),
			new BMap.Point(116.320451, 40.046673),
			new BMap.Point(116.319867, 40.047743),
			new BMap.Point(116.319122, 40.049116),
			new BMap.Point(116.31878, 40.049552),
			new BMap.Point(116.317048, 40.051744),
			new BMap.Point(116.316032, 40.053022),
			new BMap.Point(116.315639, 40.053519),
			new BMap.Point(116.313536, 40.056347),
			new BMap.Point(116.313365, 40.05658),
			new BMap.Point(116.313265, 40.056712),
			new BMap.Point(116.313215, 40.056783),
			new BMap.Point(116.313064, 40.056985),
			new BMap.Point(116.31242, 40.058037),
			new BMap.Point(116.311555, 40.059442),
			new BMap.Point(116.311535, 40.059473),
			new BMap.Point(116.311123, 40.06015),
			new BMap.Point(116.311118, 40.06015),
			new BMap.Point(116.311119, 40.06048),
			new BMap.Point(116.310968, 40.060772),
			new BMap.Point(116.310385, 40.061943),
			new BMap.Point(116.310334, 40.062044),
			new BMap.Point(116.310103, 40.062488),
			new BMap.Point(116.310063, 40.062568),
			new BMap.Point(116.310033, 40.062629),
			new BMap.Point(116.309309, 40.064021),
			new BMap.Point(116.308897, 40.064488),
			new BMap.Point(116.308555, 40.064634),
			new BMap.Point(116.308375, 40.064707),
			new BMap.Point(116.308244, 40.064759),
			new BMap.Point(116.307963, 40.064834),
			new BMap.Point(116.30756, 40.064791),
			new BMap.Point(116.306866, 40.064622),
			new BMap.Point(116.304856, 40.064163),
			new BMap.Point(116.303499, 40.063863),
			new BMap.Point(116.302966, 40.06374),
			new BMap.Point(116.302856, 40.063682),
			new BMap.Point(116.302854, 40.063681),
			new BMap.Point(116.302995, 40.063199),
			new BMap.Point(116.303979, 40.060475),
			new BMap.Point(116.303989, 40.060395),
			new BMap.Point(116.304079, 40.060064),
			new BMap.Point(116.30432, 40.05918),
			new BMap.Point(116.304501, 40.058558),
			new BMap.Point(116.304521, 40.058518),
			new BMap.Point(116.304662, 40.058015),
			new BMap.Point(116.304933, 40.057151),
			new BMap.Point(116.305194, 40.056358),
			new BMap.Point(116.305235, 40.056207),
			new BMap.Point(116.305325, 40.055946),
			new BMap.Point(116.305566, 40.055212),
			new BMap.Point(116.305647, 40.054951),
			new BMap.Point(116.305747, 40.05494),
			new BMap.Point(116.305717, 40.05502),
			new BMap.Point(116.305717, 40.055019),
			new BMap.Point(116.305647, 40.05523),
			new BMap.Point(116.305416, 40.055964),
			new BMap.Point(116.305295, 40.056375),
			new BMap.Point(116.305034, 40.057169),
			new BMap.Point(116.304773, 40.058033),
			new BMap.Point(116.304772, 40.058033),
			new BMap.Point(116.306491, 40.058347),
			new BMap.Point(116.306492, 40.058347),
			new BMap.Point(116.306914, 40.05729),
			new BMap.Point(116.306954, 40.05717),
			new BMap.Point(116.306954, 40.05717),
			new BMap.Point(116.306974, 40.057169) 
        ];
		//    map.addOverlay(new BMap.Polyline(arrPois, {strokeColor: '#111'})); //不画线
		map.setViewport(arrPois);
		marker = new BMap.Marker(arrPois[0], {
			icon : new BMap.Icon(
					'http://developer.baidu.com/map/jsdemo/img/car.png',
					new BMap.Size(52, 26), {
						anchor : new BMap.Size(27, 13)
					})
		});
		var label = new BMap.Label("粤A30780", {
			offset : new BMap.Size(0, -30)
		});
		label.setStyle({
			border : "1px solid rgb(204, 204, 204)",
			color : "rgb(0, 0, 0)",
			borderRadius : "10px",
			padding : "5px",
			background : "rgb(255, 255, 255)",
		});
		marker.setLabel(label);
		marker.devId = '18350083305';
		marker.time = '2017-11-08 12:00:00';
		marker.setTitle('设备ID:18350083305');
		marker.setRotation(30);
		
		map.addOverlay(marker);
		BMapLib.LuShu.prototype._move = function(initPos, targetPos, effect) {
			var pointsArr = [ initPos, targetPos ]; //点数组
			var me = this,
			//当前的帧数
			currentCount = 0,
			//步长，米/秒
			timer = 10, step = this._opts.speed / (1000 / timer),
			//初始坐标
			init_pos = this._projection.lngLatToPoint(initPos),
			//获取结束点的(x,y)坐标
			target_pos = this._projection.lngLatToPoint(targetPos),
			//总的步长
			count = Math.round(me._getDistance(init_pos, target_pos) / step);
			//显示折线 syj201607191107
			this._map.addOverlay(new BMap.Polyline(pointsArr, {
				strokeColor : "blue",
				strokeWeight : 5,
				strokeOpacity : 0.5
			})); // 画线  
			//如果小于1直接移动到下一点
			if (count < 1) {
				me._moveNext(++me.i);
				return;
			}
			me._intervalFlag = setInterval(function() {
				//两点之间当前帧数大于总帧数的时候，则说明已经完成移动
				if (currentCount >= count) {
					clearInterval(me._intervalFlag);
					//移动的点已经超过总的长度
					if (me.i > me._path.length) {
						return;
					}
					//运行下一个点
					me._moveNext(++me.i);
				} else {
					currentCount++;
					var x = effect(init_pos.x, target_pos.x, currentCount,count), 
						y = effect(init_pos.y, target_pos.y, currentCount, count), 
						pos = me._projection.pointToLngLat(new BMap.Pixel(x, y));
					//设置marker
					if (currentCount == 1) {
						var proPos = null;
						if (me.i - 1 >= 0) {
							proPos = me._path[me.i - 1];
						}
						if (me._opts.enableRotation == true) {
							me.setRotation(proPos, initPos, targetPos);
						}
						if (me._opts.autoView) {
							if (!me._map.getBounds().containsPoint(pos)) {
								me._map.setCenter(pos);
							}
						}
					}
					//正在移动
					me._marker.setPosition(pos);
					me._marker.setTitle('设备ID:18350083305');
					me._marker.setRotation(30);
					//设置自定义overlay的位置
					me._setInfoWin(pos);
				}
			}, timer);
		};
		lushu = new BMapLib.LuShu(map, arrPois, {
			defaultContent : "粤A30780",
			autoView : true,//是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
			icon : new BMap.Icon(
					'http://developer.baidu.com/map/jsdemo/img/car.png',
					new BMap.Size(52, 26), {
						anchor : new BMap.Size(27, 13)
					}),
			speed : 2500,
			enableRotation : false//是否设置marker随着道路的走向进行旋转
		});

		marker.addEventListener("click", function() {
			marker.enableMassClear(); //设置后可以隐藏改点的覆盖物
			marker.hide();
			lushu.start();
			//map.clearOverlays();	//清除所有覆盖物
		});
		//绑定事件
		$("run").onclick = function() {
			marker.enableMassClear(); //设置后可以隐藏改点的覆盖物
			marker.hide();
			lushu.start();
			// map.clearOverlays();	//清除所有覆盖物
		}
		$("stop").onclick = function() {
			lushu.stop();
		}
		$("pause").onclick = function() {
			lushu.pause();
		}
		$("hide").onclick = function() {
			lushu.hideInfoWindow();
		}
		$("show").onclick = function() {
			lushu.showInfoWindow();
		}
		function $(element) {
			return document.getElementById(element);
		}
	</script> 
</body> 
</html> 