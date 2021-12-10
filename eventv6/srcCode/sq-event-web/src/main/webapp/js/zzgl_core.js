//-----validation start
$.extend($.fn.validatebox.defaults.rules, {
    idcard: {
        validator: function(value, param) {
            return checkIdCard(value);
        },
        message: '身份证号码不合法'
    },
    date: {
        validator: function(value, param) {
            return isDate(value);
        },
        message: '日期格式不正确'
    },
    mobile: {
    	validator: function(value, param) {
    		return isMobile(value);
    	},
    	message: '移动电话号码不正确'
    },
    phone: {
    	validator: function(value, param) {
    		return isPhone(value);
    	},
    	message: '电话号码不正确'
    },
    maxLength: {   
        validator: function(value, param){   
            return checkStrLength(value) <= param[0];   
        },   
        message: '输入的长度不能大于{0}个字符'  
    },
    mobileorphone: {
    	validator: function(value, param) {
    		if(isMobile(value) || isPhone(value)){
    			return true;
    		}
    		return false;
    	},
    	message: '请输入有效手机号码或电话号码'
    },
    email:{
    	validator: function(value, param) {
    		return isEmail(value);
    	},
		message: 'E-MAIL地址不正确'
    },
    character:{
    	validator: function(value, param) {
    		return isCharacter(value);
    	},
		message: '只能输入字符或数字'
    },
    characterCheck: {
    	validator: function(value, param) {
    		var rule = "";
    		var isAdd = true;
    		var basRule = "\<\>\'\"";
    		var msg = "";
    		var defaultMsg = '如下字符为非法字符：';
    		
    		if(param!=undefined && param!=null){
    			if(param.length > 0){
    				rule = param[0];
    				msg = defaultMsg + '{0}';
    			}
    			if(param.length > 1){
    				isAdd = param[1];
    				if(isAdd == true){
    					msg = defaultMsg + mergeStr(param[0], basRule);
    				}
    			}
    		}else{
    			msg = defaultMsg + basRule;
    		}
    		
    		$.fn.validatebox.defaults.rules.characterCheck.message = msg;
    		
    		return isCharacterValidate(value, basRule, rule, isAdd);
    	},
		message: ''
    },
    characterLength:{
    	validator: function(value, param) {
    		if(value.length <= param[0] && isCharacter(value)){
    			return true;
    		}else
    			return false;
    	},
		message: '请输入字符或数字，长度不能大于{0}个字符'
    },
    floatNum:{
    	validator: function(value, param) {
    		return isFloatNum(value);
    	},
		message: '请输入正确的浮点数'
    },
    floatNumLength:{//接收参数：浮点数的位数,浮点数的小数点位数,浮点数的上界
    	validator: function(value, param) {
    		if(!isFloatNum(value)) {
    			$.fn.validatebox.defaults.rules.floatNumLength.message = '请输入正确的浮点数';
    			return false;
    		} else if(isNotBlankParam(param)) {
    			var paramLen = param.length;
    			
    			if(paramLen > 0 && param[0] > 0 && value.length > param[0]) {//比较总位数
        			$.fn.validatebox.defaults.rules.floatNumLength.message = '输入的位数不能超过{0}位';
        			return false;
        		}
    			if(paramLen > 1) {//比较小数点位数
        			var array = value.split(".");
        			if(array.length > 1) {
        				if(array[array.length - 1].length > param[1]) {
        					$.fn.validatebox.defaults.rules.floatNumLength.message = '输入的小数点位数不能超过{1}位';
        	    			return false;
        				}
        			}
        		}
    			if(paramLen > 2) {//比较数值
        			if(parseFloat(value) > parseFloat(param[2])) {
        				$.fn.validatebox.defaults.rules.floatNumLength.message = '输入数值不能超过{2}';
    	    			return false;
        			}
        		}
    		}
    		
    		return true;
    	},
		message: ''
    },
    floatNumStr:{
    	validator: function(value, param) {
    		return isFloatNum2(value,param[0],param[1]);
    	},
		message: ''
    },
    num:{
    	validator: function(value, param) {
    		return isNum(value);
    	},
		message: '请输入正确的整数'
    },
    numLength:{//接收参数：整数位数,整数上界
    	validator: function(value, param) {
    		if(!isNum(value)) {
    			$.fn.validatebox.defaults.rules.numLength.message = '请输入正确的整数';
    			return false;
    		} else if(isNotBlankParam(param)) {
    			var paramLen = param.length;
    			
    			if(paramLen > 0 && value.length > param[0]) {//比较位数
	    			$.fn.validatebox.defaults.rules.numLength.message = '输入的整数位数不能超过{0}位';
	    			return false;
    			}
    			if(paramLen > 1 && parseInt(value,10) > parseInt(param[0],10)) {//比较数值
	    			$.fn.validatebox.defaults.rules.numLength.message = '输入的整数不能超过{1}';
	    			return false;
    			}
    		}
    		
    		return true;
    	},
		message: ''
    },
    maxSize: {
    	validator: function(value, param) {
    		if(!isNum(value)){
		    	$.fn.validatebox.defaults.rules.maxSize.message = '请输入正确的整数！';
		    	return false;
    		}else{
    			if(value>param[0]){
    				$.fn.validatebox.defaults.rules.maxSize.message = '不能大于{0}总层数 ';
    				return false;
    			}else{
    				return true;
    			}
    		}
    	},
    	message: ''
    },
    isBlank: {
        validator: function (value, param) { return $.trim(value) != '' },
        message: '不能为空，全空格也不行'
    }
});

function checkIdCard(card) {

    var vcity = {
        11 : "北京",
        12 : "天津",
        13 : "河北",
        14 : "山西",
        15 : "内蒙古",
        21 : "辽宁",
        22 : "吉林",
        23 : "黑龙江",
        31 : "上海",
        32 : "江苏",
        33 : "浙江",
        34 : "安徽",
        35 : "福建",
        36 : "江西",
        37 : "山东",
        41 : "河南",
        42 : "湖北",
        43 : "湖南",
        44 : "广东",
        45 : "广西",
        46 : "海南",
        50 : "重庆",
        51 : "四川",
        52 : "贵州",
        53 : "云南",
        54 : "西藏",
        61 : "陕西",
        62 : "甘肃",
        63 : "青海",
        64 : "宁夏",
        65 : "新疆",
        71 : "台湾",
        81 : "香港",
        82 : "澳门",
        91 : "国外"
    };
    checkCard = function(card) {
        // 是否为空
        if (card === '') {
            return false;
            //return '* 请输入身份证号，身份证号不能为空';
        }
        // 校验长度，类型
        if (isCardNo(card) === false) {
            return false;
            //return '* 您输入的身份证号码不正确，请重新输入';
        }
        // 检查省份
        if (checkProvince(card) === false) {
            return false;
            //return '* 您输入的身份证号码不正确,请重新输入';
        }
        // 校验生日
        if (checkBirthday(card) === false) {
            return false;
            //return '* 您输入的身份证号码生日不正确,请重新输入';
        }
        // 检验位的检测
        if (checkParity(card) === false) {
            return false;
            //return '* 您的身份证校验位不正确,请重新输入';
        }
        return true;
    };
    // 检查号码是否符合规范，包括长度，类型
    isCardNo = function(card) {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{17}(\d|X)$)/;
        if (reg.test(card) === false) {

            return false;
        }

        return true;
    };
    // 取身份证前两位,校验省份
    checkProvince = function(card) {
        var province = card.substr(0, 2);
        if (vcity[province] == undefined) {
            return false;
        }
        return true;
    };
    //获取性别 0：女性；1：男性；-1：异常
    fetchGender = function(card) {
    	var gender = -1;
    	
    	if(card) {
    		var len = card.length;
        	
        	if(len == '15') {
        		gender = parseInt(card.substring(len - 1), 10);
        	} else if(len == '18') {
        		gender = parseInt(card.substring(len - 2, len - 1), 10);
        	}
    	}
    	
    	return gender%2;
    };
    // 检查生日是否正确
    checkBirthday = function(card) {
        var len = card.length;
        // 身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
        if (len == '15') {
            var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
            var arr_data = card.match(re_fifteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            var birthday = new Date('19' + year + '/' + month + '/' + day);
            return verifyBirthday('19' + year, month, day, birthday);
        }
        // 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
        if (len == '18') {
            var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
            var arr_data = card.match(re_eighteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            var birthday = new Date(year + '/' + month + '/' + day);
            return verifyBirthday(year, month, day, birthday);
        }
        return false;
    };
    //截取生日
    fetchBirthday = function(card) {
        var len = card.length;
        var birthDay = "";
        
        // 身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
        if (len == '15') {
            var re_fifteen = /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/;
            var arr_data = card.match(re_fifteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            birthDay = '19' + year + '-' + month + '-' + day;
        } else if (len == '18') {// 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
            var re_eighteen = /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/;
            var arr_data = card.match(re_eighteen);
            var year = arr_data[2];
            var month = arr_data[3];
            var day = arr_data[4];
            birthDay = year + '-' + month + '-' + day;
        }
        
        return birthDay;
    };
    //通过身份证获取周岁
    fetchAge = function(card) {
    	var age = fetchAgeByBirthday(fetchBirthday(card));
    	
    	return age;
    };
    // 校验日期
    verifyBirthday = function(year, month, day, birthday) {
        var now = new Date();
        var now_year = now.getFullYear();
        // 年月日是否合理
        if (birthday.getFullYear() == year && (birthday.getMonth() + 1) == month && birthday.getDate() == day) {
            // 判断年份的范围（1岁到100岁之间)
            var time = now_year - year;
            if (time >= 1 && time <= 100) {
                return true;
            }
            return false;
        }
        return false;
    };
    // 校验位的检测
    checkParity = function(card) {
        //validate sepcial identityCard
        /*var str = "350102197712030109";
        if (str.match(str)) {
            return true;
        }*/
        // 15位转18位
        card = changeFivteenToEighteen(card);
        var len = card.length;
        if (len == '18') {
            var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
            var cardTemp = 0,
            i, valnum;
            for (i = 0; i < 17; i++) {
                cardTemp += card.substr(i, 1) * arrInt[i];
            }
            valnum = arrCh[cardTemp % 11];
            if (valnum == card.substr(17, 1)) {
                return true;
            }
            return false;
        }
        return false;
    };
    // 15位转18位身份证号
    changeFivteenToEighteen = function(card) {
        if (card.length == '15') {
            var arrInt = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var arrCh = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2');
            var cardTemp = 0,
            i;
            card = card.substr(0, 6) + '19' + card.substr(6, card.length - 6);
            for (i = 0; i < 17; i++) {
                cardTemp += card.substr(i, 1) * arrInt[i];
            }
            card += arrCh[cardTemp % 11];
            return card;
        }
        return card;
    };
    return checkCard(card);
}
//通过生日获取周岁 生日格式为yyyy/mm/dd或者yyyy-mm-dd
function fetchAgeByBirthday(birthdayStr) {
	var age = 0;
	
	if(typeof birthdayStr == 'string' && isNotBlankString(birthdayStr) && isDate(birthdayStr)) {
		birthdayStr = birthdayStr.replace(/-/g, "/");//IE下支持的时间格式为：yyyy/mm/dd
		var now = new Date(),
			birthday = new Date(birthdayStr);
		
		age = now.getFullYear() - birthday.getFullYear() - 1;
    	
    	if(now.getMonth() < birthday.getMonth() || 
    			(now.getMonth() == birthday.getMonth() && now.getDay() <= birthday.getDay())) {
    		age++;
    	}
    	
    	if(age < 0) {
    		age = 0;
    	}
	}
	
	return age;
}

function isDate(str) {
    // 是否为日期（YYYY-MM-DD）类型字符串
    var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (r == null) return false;
    var d = new Date(r[1], r[3] - 1, r[4]);
    return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]);
}
function isMobile(s){   
	var regu =/^0?(13[0-9]|14[57]|15[012356789]|16[6]|17[01236789]|18[012356789]|19[89])[0-9]{8}$/;
	var re = new RegExp(regu);   
	if (re.test(s)) {   
		return true;   
	}else{   
		return false;   
	}   
} 
function isPhone(s){
	var phoneRegWithArea = /^[0][1-9]{2,3}[0-9]{5,8}$/; 
	if( phoneRegWithArea.test(s) ){
			return true; 
		}else{
			return false;
		}   
}
function isEmail(val){
	var rex = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i;
	return rex.test(val);
}
//只能输入字母或数字
function isCharacter(val){
	var rex = /^[0-9a-zA-Z]*$/g;
	return rex.test(val);
}

//不能输入非法字符
function isCharacterValidate(val, basRule, exRule, isAdd){
	var rex = "\<\>\'\"";//基本非法字符
	var rexEx = null;
	
	if(basRule!=undefined && basRule!=null && basRule!="" && typeof basRule == "string"){
		rex = basRule;
	}
	if(exRule!=undefined && exRule!=null && exRule!="" && typeof exRule == "string"){//额外非法字符
		if(isAdd == true){//isAdd为true时，合并额外非法字符和基本非法字符
			exRule += rex;
		}
		rexEx = eval("/[" + exRule + "]+/g");
	}else{
		rexEx = eval("/[" + rex + "]+/g");
	}
	return !rexEx.test(val);
}

//--浮点数字或整数
function isFloatNum(value) {
	if(value=="") return true;
	//var floatReg = /^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$/;
	var floatReg = /^-?([1-9]\d*\.\d+|0\.\d+|0)$/;
	var intReg = /^[1-9]\d*$/;
	if (!floatReg.test(value) && !intReg.test(value)) {
		return false;
	}
	return true;
}

//整数
function isNum(value) {
	if(value=="") return true;
	//var reg = /^[0-9]+[0-9]*]*$/;
	var reg = /^([1-9]+\d*|0)$/;
	if(!reg.test(value)) {
		return false;
	}
	return true;
}
//-----validation end


//---------

//打开遮罩
function modleopen() {
	$("<div class='datagrid-mask'></div>").css( {
		display : "block",
		width : "100%",
		height : $(window).height()
	}).appendTo("body");
	$("<div class='datagrid-mask-msg'></div>").html("正在处理，请稍候。。。").appendTo(
			"body").css( {
		display : "block",
		left : ($(window).width() - 190) / 2,
		top : ($(window).height() - 45) / 2
	});
	document.body.scroll="no";//除去滚动条
}
//关闭遮罩
function modleclose() {
	$(".datagrid-mask").css( {
		display : "none"
	});
	$(".datagrid-mask-msg").css( {
		display : "none"
	});
	$(".datagrid-mask").remove();
	$(".datagrid-mask-msg").remove();
	document.body.scroll="auto";//开启滚动条
}

//序列化form查询条件
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//限制文件选择
function checkFile(file, type, str){
	if(file!=null && file.value != ''){
		var lastPos = file.value.lastIndexOf('.');
		var pos, fileType;
		if(lastPos > -1){
			pos = lastPos + 1;
			fileType = file.value.substring(pos, file.value.length);
			fileType = fileType.toLowerCase();
			if(type.indexOf(fileType) > -1){
				
			}else{
				alert(str);
				var file = $("#"+file.id);
			    file.after(file.clone().val(""));   
			    file.remove();  
			}
		}
	}
}

//中英文长度计算
function checkStrLength(str){
	if(str == null || str=="") 
		return 0;
    else  
    	return str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length ;
}

/*
* 字符串替换方法
* 参数：
* sourceStr：		源字符串，即用于处理的字符串
* targetStr：	 	用于比对的字符串，即在源字符串中查找的字串
* replaceString：	用于将targetStr替换的字符串
* msgFlag：			用于判断是否显示提示信息 true为显示；false为不显示
*/
function replaceStr(sourceStr, targetStr, replaceString, msgFlag){
	if(msgFlag==undefined || msgFlag==null){
		msgFlag = false;
	}
	
	if(sourceStr==undefined || sourceStr==null || sourceStr==""){
		if(msgFlag == true){
			$.messager.alert('提示','请输入需要处理的字符串!','warning');
		}
		return "";
	}
	
	if(targetStr==undefined || targetStr==null || targetStr==""){
		if(msgFlag == true){
			$.messager.alert('提示','请输入用于比对的字符串!','warning');
		}
		return sourceStr;
	}
	
	if(replaceString==undefined || replaceString==null){
		if(msgFlag == true){
			$.messager.alert('提示','请输入用于替换的字符串!','warning');
		}
		return sourceStr;
	}
	
	if(sourceStr.indexOf(targetStr) != -1){
		sourceStr = sourceStr.substr(0, sourceStr.indexOf(targetStr))+replaceString+sourceStr.substr(sourceStr.indexOf(targetStr) + targetStr.length);
	}else{
		if(msgFlag == true){
			$.messager.alert('提示','在'+sourceStr+'中未找到'+targetStr+'!','warning');
		}
		return sourceStr;
	}
	
	return sourceStr;
}

/*
*合并多个字符串
*arguments 为传入的参数数组
*/
function mergeStr(){
	var str = "";
	for (var index = 0; index < arguments.length; index++) {
		str = mergeString(str, arguments[index]);
	}
	
	return str;
}

/*
*合并两个字符串
*/
function mergeString(str1, str2){
	var str = "";
	if(str1!=null && str1!=null && str1!="" && typeof str1 == "string"){
		str = str1;
	}
	
	if(str2!=null && str2!=null && str2!="" && typeof str2 == "string"){
		if(str != ""){//str1有效
			for(var index = 0, len = str2.length; index < len; index++){
				var strChar = str2.charAt(index);
				if(str.indexOf(strChar) == -1){
					str += strChar;
				}
			}
		}else{
			str = str2;
		}
	}
	
	return str;
}

function trimWords(words){//去除首尾的空格 当trim()无法使用时，使用该方法
	if(words!=undefined && words!=null){
		var wordsLength = words.length;
		var startIndex = -1, endIndex = -1;
		for(var index = 0; index < wordsLength; index++){
			if(words.charAt(index)==" " || words.charAt(index)=="\n"){
				startIndex = index;
			}else{
				break;
			}
		}
		
		for(var index = wordsLength-1; index > 0; index--){
			if(words.charAt(index)==" " || words.charAt(index)=="\n"){
				endIndex = index;
			}else{
				break;
			}
		}
		
		if(startIndex > -1){
			startIndex++;
			words = words.substr(startIndex, wordsLength);
		}else{
			startIndex = 0;
		}
		
		if(endIndex>-1 && endIndex>startIndex){
			words = words.substr(0, endIndex-startIndex);
		}
	}else{
		words = "";
	}
	
	return words;
}

//设置下拉框的排斥项，record为当前选中项，id为控件id，exceptValue为排斥项的值，以英文逗号相连
function comboboxSelect(record, id, exceptValue){
	var exceptAttr = [];
	
	if(exceptValue==undefined || exceptValue==null){
		exceptValue = "";
		exceptAttr[0] = "";
	}else if(typeof exceptValue == 'string'){
		exceptAttr = exceptValue.split(',');
	}
	
	if(exceptValue.indexOf(record.value) != -1){
		$('#'+id).combobox("setValue", record.value);
	}else{
		for(var index = 0, len = exceptAttr.length; index < len; index++){
			$('#'+id).combobox("unselect", exceptAttr[index]);
		}
	}
}

//--浮点数字或整数
function isFloatNum2(value,param1,param2) {
	if(value=="") return true;
	var floatReg = /^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$/;
	var intReg = /^[1-9]\d*$/;
	if (!floatReg.test(value) && !intReg.test(value)) {
		$.fn.validatebox.defaults.rules.floatNumStr.message = '请输入正确的浮点数!';
		return false;
	}
		
	if(floatReg.test(value)){//如果是小数类型
		//截取整数和小数部分
		var valueArray=value.split(".");
		if(valueArray[0].length>param1){
			$.fn.validatebox.defaults.rules.floatNumStr.message = '整数位不能超过{0}位!';
			return false;
		}
		if(valueArray[1].length>param2){
			$.fn.validatebox.defaults.rules.floatNumStr.message = '小数位不能超过{1}位!！';
			return false;
		}else if(valueArray[1].length==0){
			$.fn.validatebox.defaults.rules.floatNumStr.message = '请输入正确的浮点数！';
			return false;
		}
	}else if(intReg.test(value)){//如果是整数
		//截取整数和小数部分
		if(value.length>param1){
			$.fn.validatebox.defaults.rules.floatNumStr.message = '整数位不能超过{0}位!';
			return false;
		}
	}
	
	return true;
}

//设置下拉框的默认选择项，默认选择空值项
function comboboxUnselect(record, id){
	var values = $('#'+id).combobox("getValues");
	if(values.length == 0){
		$('#'+id).combobox("setValue", "");
	}
}


/**
 * 判断变量是否存在，不是则返回true
 * @param param
 * @returns {Boolean}
 */
function isBlankParam(param) {
	return param==undefined || param==null;
}

/**
 * 判断变量是否存在，是则返回true
 * @param param
 * @returns
 */
function isNotBlankParam(param) {
	return !isBlankParam(param);
}

/**
 * 判断字符串是否为空 是则返回true
 * @param str
 * @returns {Boolean}
 */
function isBlankString(str) {
	return str==undefined || str==null || str=="";
}

/**
 * 判断字符串是否为空 不是则返回true
 * @param str
 * @returns
 */
function isNotBlankString(str) {
	return !isBlankString(str);
}

/**
 * 判断去除空格之后的字符串是否为空 是则返回true
 * @param str
 * @returns {Boolean}
 */
function isBlankStringTrim(str) {
	var isBlank = false;
	if (str!=undefined && str!=null) {
		str = $.trim(str);
		isBlank = str=="";
	} else {
		isBlank = true;
	}
	
	return isBlank;
}

/**
 * 判断去除空格之后的字符串是否为空 不是则返回true
 * @param str
 * @returns {Boolean}
 */
function isNotBlankStringTrim(str) {
	return !isBlankStringTrim(str);
}

/**
 * 获取弹出窗口高度
 * @param optSet 各属性设置均为数值(可整数，可小数)
 * @returns {Number}
 */
function fetchWinHeight(optSet) {
	var opt = {
		"winHeight": $(document).height(),
		"maxHeight": 500,
		"padding_top": 30,
		"padding_bottom": 30
	};
	
	opt = $.extend(opt, isOptNumber(optSet));
	
	var height_ = opt.winHeight - opt.padding_top - opt.padding_bottom;
	
	if(height_ > opt.maxHeight) {
		height_ = opt.maxHeight;
	}
	
	return height_;
}

/**
 * 获取弹出窗口宽度
 * @param optSet 各属性设置均为数值(可整数，可小数)
 * @returns {Number}
 */
function fetchWinWidth(optSet) {
	var opt = {
		"winWidth": $(document).width(),
		"maxWidth": 965,
		"padding_left": 20,
		"padding_right": 20
	};
	
	opt = $.extend(opt, isOptNumber(optSet));
	
	var width_ = opt.winWidth - opt.padding_left - opt.padding_right;
	
	if(width_ > opt.maxWidth) {
		width_ = opt.maxWidth;
	}
	
	return width_;
}

/**
 * 判断设置的属性是否是有效的数值，不是则会将对于的属性剔除
 * @param optSet
 * @returns
 */
function isOptNumber(optSet) {
	var wrongMsg = "";
	
	if(optSet && typeof optSet == 'object') {
		var attValue = null;
		
		for(att in optSet) {
			attValue = optSet[att];
			
			if(isNotBlankString(attValue) && isNaN(attValue)) {
				wrongMsg += att+"["+ attValue +"] 不是有效数值！";
				
				optSet[att] = undefined;//移除无效的属性
			}
		}
		
		if(wrongMsg != "") {//提示无效是属性设置信息
			alert(wrongMsg);//使用alert能够优先展示提示信息，使用$.messager.alert则会被后续的弹框遮挡
		}
	}
	
	return optSet;
}