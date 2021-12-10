
/**
 * @see
 * 级别比例尺信息
 */
var LevelDetail = {
		//默认天地图
	    lods : [
		     	 {"level" : 0, "resolution" : 0.703125, "scale" : 2.958293554545656E8},
		    	 {"level" : 1, "resolution" : 0.3515625, "scale" : 1.479146777272828E8},
		    	 {"level" : 2, "resolution" : 0.17578125, "scale" : 7.39573388636414E7},
		    	 {"level" : 3, "resolution" : 0.087890625, "scale" : 3.69786694318207E7},
		    	 {"level" : 4, "resolution" : 0.0439453125, "scale" : 1.848933471591035E7},
		    	 {"level" : 5, "resolution" : 0.02197265625, "scale" : 9244667.357955175},
		    	 {"level" : 6, "resolution" : 0.010986328125, "scale" : 4622333.678977588},
		    	 {"level" : 7, "resolution" : 0.0054931640625, "scale" : 2311166.839488794},
		    	 {"level" : 8, "resolution" : 0.00274658203125, "scale" : 1155583.419744397},
		    	 {"level" : 9, "resolution" : 0.001373291015625, "scale" : 577791.7098721985},
		    	 {"level" : 10, "resolution" : 0.0006866455078125, "scale" : 288895.85493609926},
		    	 {"level" : 11, "resolution" : 0.00034332275390625, "scale" : 144447.92746804963},
		    	 {"level" : 12, "resolution" : 0.000171661376953125, "scale" : 72223.96373402482},
		    	 {"level" : 13, "resolution" : 8.58306884765625e-005, "scale" : 36111.98186701241},
		    	 {"level" : 14, "resolution" : 4.291534423828125e-005, "scale" : 18055.990933506204},
		    	 {"level" : 15, "resolution" : 2.1457672119140625e-005, "scale" : 9027.995466753102},
		    	 {"level" : 16, "resolution" : 1.0728836059570313e-005, "scale" : 4513.997733376551},
		    	 {"level" : 17, "resolution" : 5.3644180297851563e-006, "scale" : 2256.998866688275},
		    	 {"level" : 18, "resolution" : 2.682209014892578e-006, "scale" : 1128.499433344138},
		    	 {"level" : 19, "resolution" : 1.341104507446289e-006, "scale" : 564.2497166720688}
		    ] 
};
var LevelDetailBackUp = {
		//默认天地图
	    lods : [
		     	 {"level" : 0, "resolution" : 0.703125, "scale" : 2.958293554545656E8},
		    	 {"level" : 1, "resolution" : 0.3515625, "scale" : 1.479146777272828E8},
		    	 {"level" : 2, "resolution" : 0.17578125, "scale" : 7.39573388636414E7},
		    	 {"level" : 3, "resolution" : 0.087890625, "scale" : 3.69786694318207E7},
		    	 {"level" : 4, "resolution" : 0.0439453125, "scale" : 1.848933471591035E7},
		    	 {"level" : 5, "resolution" : 0.02197265625, "scale" : 9244667.357955175},
		    	 {"level" : 6, "resolution" : 0.010986328125, "scale" : 4622333.678977588},
		    	 {"level" : 7, "resolution" : 0.0054931640625, "scale" : 2311166.839488794},
		    	 {"level" : 8, "resolution" : 0.00274658203125, "scale" : 1155583.419744397},
		    	 {"level" : 9, "resolution" : 0.001373291015625, "scale" : 577791.7098721985},
		    	 {"level" : 10, "resolution" : 0.0006866455078125, "scale" : 288895.85493609926},
		    	 {"level" : 11, "resolution" : 0.00034332275390625, "scale" : 144447.92746804963},
		    	 {"level" : 12, "resolution" : 0.000171661376953125, "scale" : 72223.96373402482},
		    	 {"level" : 13, "resolution" : 8.58306884765625e-005, "scale" : 36111.98186701241},
		    	 {"level" : 14, "resolution" : 4.291534423828125e-005, "scale" : 18055.990933506204},
		    	 {"level" : 15, "resolution" : 2.1457672119140625e-005, "scale" : 9027.995466753102},
		    	 {"level" : 16, "resolution" : 1.0728836059570313e-005, "scale" : 4513.997733376551},
		    	 {"level" : 17, "resolution" : 5.3644180297851563e-006, "scale" : 2256.998866688275},
		    	 {"level" : 18, "resolution" : 2.682209014892578e-006, "scale" : 1128.499433344138},
		    	 {"level" : 19, "resolution" : 1.341104507446289e-006, "scale" : 564.2497166720688}
		    ] 
};
var COMMOM = {
	wkid : 4326,
	xmin : -180.0,
	ymin : -90.0,
	xmax : 180.0,
	ymax : 90.0,
	rows : 256,
	cols : 256,
	origin : { "x" : -180,"y" : 90},
	imageFormat : "tiles",
	serviceMode : "KVP",
	tileMatrixSetId : "c"
};

var TianDiTu = {
	// 矢量底图(经纬度坐标系)
	VEC_BASE_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "vec",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
		
	// 矢量注记图层(经纬度坐标系)
	VEC_ANNO_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "cva",
		tileMatrixSetId : COMMOM.tileMatrixSetId		
	},
		
	// 矢量注记图层(经纬度坐标系)-英文注记
	VEC_ANNO_GCS_EN : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "eva",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
	
	// 影像底图(经纬度坐标系)
	IMG_BASE_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "img",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
	
	// 影像注记图层(经纬度坐标系)
	IMG_ANNO_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "cia",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
	
	// 影像注记图层(经纬度坐标系) -英文注记
	IMG_ANNO_GCS_EN : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "eia",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
	
	// 地形底图(经纬度坐标系)
	TER_BASE_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "ter",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	},
	
	// 地形注记图层(经纬度坐标系)
	TER_ANNO_GCS : {
		wkid : COMMOM.wkid,
		xmin : COMMOM.xmin,
		ymin : COMMOM.ymin,
		xmax : COMMOM.xmax,
		ymax : COMMOM.ymax,
		rows : COMMOM.rows,
		cols : COMMOM.cols,
		origin : {"x" : COMMOM.origin.x, "y" : COMMOM.origin.y},
		imageFormat : COMMOM.imageFormat,
		serviceMode : COMMOM.serviceMode,
		layerId : "cta",
		tileMatrixSetId : COMMOM.tileMatrixSetId
	}
};