/**************************************************************************************************************

**************************************************************************************************************/
	//初始化图片轮播
	function initNbspSlider(){
		var slider = $("#slider");
		
		if(slider.length > 0) {
			slider.nbspSlider({
				widths:         "310px",        // 幻灯片宽度
				heights:        "310px",
				effect:	         "vertical",
				numBtnSty:       "square",
				speeds:          300,
				autoplay:       1,
				delays:         4000,
				preNexBtnShow:   0,
				altOpa:         0.3,            // ALT区块透明度
				altBgColor:     '#ccc',         // ALT区块背景颜色
				altHeight:      '20px',         // ALT区块高度
				altShow:         1,             // ALT区块是否显示(1为是0为否)
				altFontColor:    '#000',        // ALT区块内的字体颜色
				prevId: 		'prevBtn',      // 上一张幻灯片按钮ID
				nextId: 		'nextBtn'		// 下一张幻灯片按钮I
			});
		}
	}

	function getImages(bizId, attachmentType, eventSeqNameObj){
		$("#slider").html('<ul></ul>');
		var url = rootPath + '/zhsq/att/getList.jhtml';
		var data = {'bizId':bizId,'attachmentType':attachmentType};
		$.post(url,data,function(result) {
			var picFileTotal = 0;
			
			if (result && result.length>0) {
				if(typeof(imgsCallBack)=='function'){
					picFileTotal = imgsCallBack(result, eventSeqNameObj);
				}
			}
			
			if(picFileTotal > 0) {//有图片、音频文件时，才进行控件初始化
				initNbspSlider();
				$("#showImageType").show();
			} else {
				$("#slider ul").append('<li style="padding-left: 17%;padding-top: 11%;"><span class="noimg" style="display: block;" ></span></li>');
				$("#slider").css({"width": "310px", "height": "310px"});
				
			}
		},"json");
	}

	var titleArray = new Array();
	var allPicArray = new Array();
	var firstTitleArray = new Array();
	var firstPicArray = new Array();
	var secondTitleArray = new Array();
	var secondPicArray = new Array();
	var lastTitleArray = new Array();
	var lastPicArray = new Array();
	var seqPicArray = new Array();
	function imgsCallBack(result, eventSeqNameObj) {
		var picFileTotal = 0;
		
		if(result.length > 0) {
			var firstNum = 0, secondNum = 0, lastNum = 0,
				firstTitleName = "处理前", secondTitleName = "处理中", lastTitleName = "处理后",
				sliderImgDiv = $("#slider ul"),
				suffixStartIndex = -1,
				imgSuffix = '',
				imgStr = "png,jpg,gif,jpeg,webp",
				audioStr = "amr,mp3",
				videoStr = "mp4,mov,avi",
				fileShowSrc = "",
				width = 0,
				height = 0,
				imageLi = '';
				imgSrc = '',
				fileTitleName = "",
				eventSeq = '',
				imgFileIndex = 0;
			
			for(var i= 0, len = result.length; i < len; i++) {
				imageLi = '';
				fileTitleName = "";
				width = result[i].imgWidth;
				height = result[i].imgHeight;
				imgSrc = downPath + result[i].filePath,
				eventSeq = result[i].eventSeq;
				suffixStartIndex = imgSrc.lastIndexOf('.');
				
				if(suffixStartIndex >= 0) {
					imgSuffix = imgSrc.substr(suffixStartIndex + 1).toLowerCase();
				}
				
				if(eventSeqNameObj && typeof eventSeqNameObj === 'object') {
					fileTitleName = eventSeqNameObj[eventSeq];
					firstTitleName = eventSeqNameObj["1"] || firstTitleName;
					secondTitleName = eventSeqNameObj["2"] || secondTitleName;
					lastTitleName = eventSeqNameObj["3"] || lastTitleName;
				}
				
				if(!fileTitleName) {
					switch(eventSeq) {
						case '1': {
							
							fileTitleName = firstTitleName;
							break;
						}
						case '2': {
							fileTitleName = secondTitleName;
							break;
						}
						case '3': {
							fileTitleName = lastTitleName;
							break;
						}
					}
				}
					
				if(audioStr.indexOf(imgSuffix) >= 0 || videoStr.indexOf(imgSuffix) >= 0) {
					fileShowSrc = rootPath + '/scripts/updown/swfupload/images/thumbnail/audio.jpg';
					var downloadUrl = rootPath + '/zhsq/att/toSeeVideo.jhtml?attachmentId=' + result[i].attachmentId + '&videoType=',
						titleName = '音频',
						fileName = result[i].fileName || '',
						videoType = 2;
					
					if(videoStr.indexOf(imgSuffix) >= 0) {
						titleName = '视频';
						videoType = 1;
					}
					
					downloadUrl += videoType;
					fileTitleName += titleName;
					
					imageLi = '<li><a style="cursor:pointer;" title="点击播放 '+ fileName +'" target="_blank" href="'+ downloadUrl +'"><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,300,this)" alt="'+ fileTitleName +'" src="'+fileShowSrc+'" /></a></li>';
					
				} else if(imgStr.indexOf(imgSuffix) >= 0) {//只有图片才展示
					var imageSrc = imgSrc;
					
					fileTitleName += '图片';
					
					if(imageSrc.indexOf('?') < 0){
						imageSrc += '?t=' + Math.random();
					}
					
					imageLi = '<li><a style="cursor:pointer;" title="点击放大图片" onclick=showMix("playImg",'+ (imgFileIndex++) +')><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,300,this)" alt="'+ fileTitleName +'" src="'+imageSrc+'" /></a></li>';
				} else {
					var downloadUrl = rootPath + '/upFileServlet?method=down&attachmentId=' + result[i].attachmentId,
						  titleName = result[i].fileName || '';
					
					fileTitleName += '附件';
					fileShowSrc = rootPath + '/scripts/updown/swfupload/images/thumbnail/default.png';
					
					imageLi = '<li><a style="cursor:pointer;" title="点击下载 '+ titleName +'" target="_blank" href="'+ downloadUrl +'"><img class="pic" style="vertical-align:middle;" onload="AutoResizeImage(300,300,this)" alt="'+ fileTitleName +'" src="'+fileShowSrc+'" /></a></li>';
				}
				
				if(imageLi) {
					switch(eventSeq) {
						case '1': {
							firstTitleArray.push(fileTitleName);
							firstPicArray.push(imgSrc);
							seqPicArray.push(firstTitleName);
							firstNum ++;
							break;
						}
						case '2': {
							secondTitleArray.push(fileTitleName);
							secondPicArray.push(imgSrc);
							seqPicArray.push(secondTitleName);
							secondNum ++;
							break;
						}
						case '3': {
							lastTitleArray.push(fileTitleName);
							lastPicArray.push(imgSrc);
							seqPicArray.push(lastTitleName);
							lastNum ++;
							break;
						}
					}
					
					if(imgStr.indexOf(imgSuffix) >= 0) {
						titleArray.push(fileTitleName);
						allPicArray.push(imgSrc);
					}
					
					sliderImgDiv.append(imageLi);
				}
			}
			
			picFileTotal = firstNum + secondNum + lastNum;
			
			if(picFileTotal > 0) {
				ImageViewApi.initImageView("playImg",allPicArray,false,true,titleArray);
				if(firstNum > 0){
					$("#firstImgNum").html(firstNum);
					ImageViewApi.initImageView("firstImgCode11",firstPicArray);
					$('#firstImgCode1').click(function(){
						var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=firstImgCode1";
						var name = "图片查看";
						openPostWindow(url, firstPicArray, firstTitleArray);
					});
				}else{
					$("#firstImgCode1").attr("style", "display:none");
					$("#firstImgCodeNone").removeAttr("style");
				}
				if(secondNum > 0){
					$("#secondImgNum").html(secondNum);
					ImageViewApi.initImageView("secondImgCode11",secondPicArray);
					$('#secondImgCode1').click(function(){
						var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=secondImgCode1";
						var name = "图片查看";
						openPostWindow(url, secondPicArray, secondTitleArray);
					});
				}else{
					$("#secondImgCode1").attr("style", "display:none");
					$("#secondImgCodeNone").removeAttr("style");
				}
				if(lastNum > 0){
					$("#lastImgNum").html(lastNum);
					ImageViewApi.initImageView("lastImgCode11",lastPicArray);
					$('#lastImgCode1').click(function(){
						var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId=lastImgCode1";
						var name = "图片查看";
						openPostWindow(url, lastPicArray, lastTitleArray);
					});
				}else{
					$("#lastImgCode1").attr("style", "display:none");
					$("#lastImgCodeNone").removeAttr("style");
				}
			}
			
		}
		
		return picFileTotal;
	}
	
	function ffcs_viewImg_win(fieldId, index){
		if(allPicArray.length != 0){
			var url = contextPath + "/zhsq/showImage/indexOfPath.jhtml?fieldId="+ fieldId + "&index="+index+"&paths="+allPicArray+"&titles=";
			var name = "图片查看";
			//openPostWindow(url, allPicArray, titleArray);
			window.open(url,name);
		}
	}
	
	/**
	 * 调整图片高宽
	 * @param maxWidth	图片展示最大宽度
	 * @param maxHeight	图片展示最大高度
	 * @param objImg	需要调整高宽的图片对象
	 */
	function AutoResizeImage(maxWidth, maxHeight, objImg) {
		var img = new Image();
		img.src = objImg.src;
		var hRatio;
		var wRatio;
		var Ratio = 1;
		var w = img.width;
		var h = img.height;
		wRatio = maxWidth / w;
		hRatio = maxHeight / h;
		if (maxWidth == 0 && maxHeight == 0) {
			Ratio = 1;
		} else if (maxWidth == 0) {//
			if (hRatio < 1)
				Ratio = hRatio;
		} else if (maxHeight == 0) {
			if (wRatio < 1)
				Ratio = wRatio;
		} else if (wRatio < 1 || hRatio < 1) {
			Ratio = (wRatio <= hRatio ? wRatio : hRatio);
		}
		if (Ratio < 1) {
			w = w * Ratio;
			h = h * Ratio;
		}
		objImg.height = h;
		objImg.width = w;
	}
	

	
	function openPostWindow(url, data, titles){
		var tempForm = document.createElement("form");
		tempForm.id="tempForm1";
		tempForm.method="post";
		tempForm.action=url;
		tempForm.target="图片查看";
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "paths";
		hideInput.value= data;
		tempForm.appendChild(hideInput);
		var hideInput = document.createElement("input");
		hideInput.type="hidden";
		hideInput.name= "titles";
		hideInput.value= titles;
		tempForm.appendChild(hideInput);
		tempForm.submit(function(){
			openWindow("图片查看");
		});
// 		tempForm.attachEvent("onsubmit",function(){
// 			openWindow(name);
// 		});
		document.body.appendChild(tempForm);
		//tempForm.fireEvent("onsubmit");
		tempForm.submit();
		document.body.removeChild(tempForm);
	}

	function openWindow(name){
		window.open('about:blank',name,'height=400, width=400, top=0, left=0, toolbar=yes, menubar=yes, scrollbars=yes, resizable=yes,location=yes, status=yes');
	}