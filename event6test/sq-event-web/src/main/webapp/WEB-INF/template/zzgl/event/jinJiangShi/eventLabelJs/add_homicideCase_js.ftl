<script type="text/javascript">
	var ID_CARD_TYPE = "1";//身份证证件类型

	function initItem4HomicideCase() {
		<#if homicideCase?? && homicideCase.suspectList?? && (homicideCase.suspectList?size > 0)>
			showSuspectPeople();//若被嫌疑人存在，展示嫌疑人信息
		</#if>

		<#if homicideCase?? && homicideCase.victimList?? && (homicideCase.victimList?size > 0)>
			showVictimPeople();//若受害人存在，展示受害人信息
		</#if>
	}

	function fetchHomicidePersonInfo() {//构建人员信息
		var result=true;
		var reportedObjArray = [],
			reportedObj = {},
			inpVal = "",
			inpId = "";
		var bizTypeArr=[];//命案防控需要判断两种身份的人是否都存在

		//构建命案防控嫌疑人员/受害人人员信息
		$('#suspectInfoDiv table[id=suspectInfo_list] > tbody tr, #victimInfoDiv table[id=victimInfo_list] > tbody tr').each(function() {
			reportedObj = {};

			$(this).find('input').each(function() {
				inpVal = $(this).val();
				inpId = $(this).attr('id');

				if(inpVal != undefined) {
					reportedObj[inpId.substr(0, inpId.indexOf('_'))] = inpVal;
				}
			});
			bizTypeArr.push(reportedObj.bizType);
			reportedObjArray.push(reportedObj);
		});

		if(reportedObjArray.length > 0) {
		
			if(bizTypeArr.indexOf('03')>=0&&bizTypeArr.indexOf('04')>=0){
            	$("#homicideCase_person").val(JSON.stringify(reportedObjArray));
        	}else{
        		result=false;
        	}
		    
		}else{
			result=false;
		}
		
		return result;
	}

    /*var resumeRec = 0;
    var flag8 = 1;
    //peopleType:人员类型 03 嫌疑人 04 受害人
    var peopleType = '';*/
    //若嫌疑人存在，展示嫌疑人信息
    function showSuspectPeople() {
        <#if homicideCase?? && homicideCase.suspectList?? && (homicideCase.suspectList?size > 0)>
            <#list homicideCase.suspectList as list>
                var str = "";
                str = "<tr>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
                    "</td>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
                    "</td>" +
                    "<td style='width:144px'>" +
                        '<input type=\"hidden\" id=\"name_suspect\" readonly=\"readonly\" name=\"name_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" value=\"${list.name}\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
                    "</td>" +
                    "<td style='width:110px'>" +
                        '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType?string}\"/>' +
                        '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
                    "</td>" +
                    "<td style='width:180px'>" +
                        '<input type=\"hidden\" id=\"idCard_suspect\" readonly=\"readonly\" name=\"idCard_suspect\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"${list.idCard}" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 90%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
                    "</td>" +
                    "<td style='width:144px'>" +
                    '<input type=\"hidden\" id=\"tel_suspect\" readonly=\"readonly\" name=\"tel_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox\" style=\"width:120px;\" value=\"${list.tel} \" />' +
                    '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>' +
                    "</td>";
        str +=  "<td style='width:239px'>" +
                <#if list.isMinors?? && list.isMinors == 1>
                    '<input type=\"hidden\" id=\"isMinors_suspect\" name=\"isMinors_suspect\" value=\"1\" />' +
                    '<img title="未成年人" src="${rc.getContextPath()}/images/juveniles.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>'+
                </#if>
                <#if list.isTeenager?? && list.isTeenager == 1>
                    '<input type=\"hidden\" id=\"isTeenager_suspect\" name=\"isTeenager_suspect\" value=\"1\" />' +
                    '<img title="青少年" src="${rc.getContextPath()}/images/youngsters.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>'+
                </#if>
                <#if list.isMentalDisease?? && list.isMentalDisease == 1>
                    '<input type=\"hidden\" id=\"isMentalDisease_suspect\" name=\"isMentalDisease_suspect\" value=\"1\" />' +
                    '<img title="精神病患者" src="${rc.getContextPath()}/images/psychotic.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>'+
                </#if>
                 "</td>";

            str += "<td style='width:100px'>" +
                        '<input type=\"hidden\" id=\"sex_suspect' + resumeRec + '\" value=\"${list.sex}\"/>' +
                        '<input type=\"hidden\" id=\"sexName_suspect' + resumeRec + '\" name=\"sexName_suspect' + resumeRec + '\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 80px;height:30px;\" data-options=\"tipPosition:\'bottom\'\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.sexName??>${list.sexName!}</#if></div>' +
                    "</td>" +
                    "<td style='width:92px;'>" +
                        "<a href='###' class='del-tr bg-gray' style='width:50px;' onclick='delRec(this);'><i class='icon-del'></i>删 除</a>" +
                    "</td>" +
                    "</tr>";
                    $("#suspectInfo_list").append(str);
                    /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
                    var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, ['${list.cardType}']);
                    var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, ['${list.sex}']);
                    cardTypeObj.setDisabled(true);
                    genderObj.setDisabled(true);

                    resumeRec++;
			</#list>
		</#if>
	}
    //若受害人存在，展示受害人信息
	function showVictimPeople() {
		<#if homicideCase?? && homicideCase.victimList?? && (homicideCase.victimList?size > 0)>
			<#list homicideCase.victimList as list>
                var str = "";
                str = "<tr>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
                    "</td>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
                    "</td>" +
                    "<td style='width:144px'>" +
                        '<input type=\"hidden\" id=\"name_suspect\" readonly=\"readonly\" readonly=\"readonly\" name=\"name_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" value=\"${list.name}\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
                    "</td>" +
                    "<td style='width:110px'>" +
                        '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType}\"/>' +
                        '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
                    "</td>" +
                    "<td style='width:180px'>" +
                        '<input type=\"hidden\" id=\"idCard_suspect\" readonly=\"readonly\" readonly=\"readonly\" name=\"idCard_suspect\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"${list.idCard}" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 90%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
                    "</td>" +
                    "<td style='width:150px'>" +
                        '<input type=\"hidden\" id=\"tel_suspect\" readonly=\"readonly\" readonly=\"readonly\" name=\"tel_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox\" style=\"width:120px;\" value=\"${list.tel} \" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>' +
                    "</td>" +
                    "<td style='width:239px;'>" +
                    "</td>" +
                    "<td style='width:100px'>" +
                        '<input type=\"hidden\" id=\"sex_suspect' + resumeRec + '\" value=\"${list.sex}\"/>' +
                        '<input type=\"hidden\" id=\"sexName_suspect' + resumeRec + '\" name=\"sexName_suspect' + resumeRec + '\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 80px;height:30px;\" data-options=\"tipPosition:\'bottom\'\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.sexName??>${list.sexName!}</#if></div>' +
                    "</td>" +
                    "<td style='width:92px;'>" +
                        "<a href='###' class='del-tr bg-gray' style='width:50px;' onclick='delRec(this);'><i class='icon-del'></i>删 除</a>" +
                    "</td>" +
                    "</tr>";
                    $("#victimInfo_list").append(str);
                    /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
                    var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, ['${list.cardType}']);
                    var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, ['${list.sex}']);
                    cardTypeObj.setDisabled(true);
                    genderObj.setDisabled(true);

                    resumeRec++;
			</#list>
		</#if>
	}

	function checkCardType(obj) {//检验证件类型
		var isValid = true, cardType = null, objId = $(obj).attr('id'), index = objId.substr(objId.indexOf('_'));

		cardType = $('#cardType' + index).val();

		if(cardType && cardType == ID_CARD_TYPE) {//选择了身份证
			isValid = checkCardId(index);
		}

		return isValid;
	}

	function checkCardId(index) {//检验身份号码
		var idCard = $("#idCard" + index).val(), isValid = true;

		if(isNotBlankStringTrim(idCard)) {
			if(checkIdCard(idCard)) {
				alterInfo(idCard, index);
			} else {
				isValid = false;
				$.messager.alert('警告','身份证号码不合法！','warning');
			}
		}

		return isValid;
	}

	function alterInfo(idCard, index) {//变更信息
		var birthDay = fetchBirthday(idCard);

		if(isNotBlankStringTrim(birthDay)) {
			_checkBirthday(birthDay,index);
		}
	}

	function _checkBirthday(idCard) {//依据出生日期获取周岁
        //初始化js方法
        if(checkIdCard(idCard)){
            var birthDay = fetchBirthday(idCard);
            var age = -1;

            if(isNotBlankStringTrim(birthDay)) {
                age = fetchAgeByBirthday(birthDay);
            }

            return age;
        }
	}
</script>