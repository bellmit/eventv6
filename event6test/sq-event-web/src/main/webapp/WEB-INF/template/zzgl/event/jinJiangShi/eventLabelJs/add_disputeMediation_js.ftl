<script type="text/javascript">
    var ID_CARD_TYPE = "1";//身份证证件类型

    function initItem4DisputeMediation() {

        AnoleApi.initTreeComboBox("disputeMediation_mediationTypeStr", "disputeMediation_mediationType", "B417", null<#if disputeMediation?? && disputeMediation.mediationType??>, ["${disputeMediation.mediationType}"]</#if>);
        AnoleApi.initTreeComboBox("disputeMediation_disputeScaleStr", "disputeMediation_disputeScale", "B036", null<#if disputeMediation?? && disputeMediation.disputeScale??>, ["${disputeMediation.disputeScale}"]</#if>);

        AnoleApi.initListComboBox("disputeMediation_isSuccessStr", "disputeMediation_isSuccess", null, null,["<#if disputeMediation?? && disputeMediation.isSuccess??>${disputeMediation.isSuccess!''}</#if>"],{
            RenderType : "00",
            DataSrc : [{"name":"是","value":"1"},{"name":"否","value":"0"}]
        });
        
        zzForm.render('checkbox','disputeMediationIsDetection');
        zzForm.on('checkbox(_disputeMediationIsDetection)', function (data) {
            if(data.checked){
                $(this).val('1');
                $('#disputeMediationIsDetection').show();
            }else {
                $(this).val('0');
                $('#disputeMediationIsDetection').hide();
                $('#disputeMediation_mediationType').val('');
                $('#disputeMediation_mediationTypeStr').val('');
            }
            
            var requestParamObj = null;
			var isItem2Hide;
			if(data.checked){
				isItem2Hide=false;
			}else{
				isItem2Hide=true;
			}
			if(isItem2Hide) {
				requestParamObj = $('#disputeMediationIsDetection .requestParam');
			} else {
				requestParamObj = $('#disputeMediationIsDetection .requestParam:visible');
			}
			requestParamObj.each(function(i){
				if($(this).hasClass('comboselector')){
					$(this).comboselector({
						required: !isItem2Hide,
						tipPosition:'bottom'
					});
				}else{
					$(this).validatebox({
						required: !isItem2Hide,
						tipPosition:'bottom'
					});
				}
			});
            
        });

        <#if disputeMediation?? && disputeMediation.suspectList?? && (disputeMediation.suspectList?size > 0)>
            showdisputeMediationParty();//若当事人存在，展示当事人信息
        </#if>
        <#if disputeMediation?? && disputeMediation.peopleInChargeList?? && (disputeMediation.peopleInChargeList?size > 0)>
            showdisputeMediationPeopleInChargeList();//若化解责任人存在，展示责任人信息
        </#if>
    }

    function fetchDisputeMediationPartyInfo() {//构建人员信息
    	var result = true;
        var reportedObjArray = [],
            reportedObj = {},
            inpVal = "",
            inpId = "";
        var bizTypeArr=[];//矛盾纠纷需要判断两种身份的人是否都存在

        //构建命案防控嫌疑人员/受害人人员信息
        $('#partyInfoDiv table[id=partyInfo_list] > tbody tr,#peopleInChargeInfoDiv table[id=peopleInChargeInfo_list] > tbody tr').each(function() {
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
        
        	if(bizTypeArr.indexOf('06')>=0&&bizTypeArr.indexOf('14')>=0){
            	$("#disputeMediation_person").val(JSON.stringify(reportedObjArray));
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
    function showdisputeMediationParty() {
        <#if disputeMediation?? && disputeMediation.suspectList?? && (disputeMediation.suspectList?size > 0)>
            <#list disputeMediation.suspectList as list>
                var str = "";
                str = "<tr>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
                    "</td>" +
                    "<td style=\"display:none;\">" +
                        '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
                    "</td>" +
                    "<td style='width:144px'>" +
                        '<input type=\"hidden\" id=\"name_suspect\" name=\"name_suspect\" readonly=\"readonly\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" value=\"${list.name}\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
                    "</td>" +
                    "<td style='width:145px'>" +
                        '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType?string}\"/>' +
                        '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
                    "</td>" +
                    "<td style='width:239px'>" +
                        '<input type=\"hidden\" id=\"idCard_suspect\" name=\"idCard_suspect\" readonly=\"readonly\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"${list.idCard}" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
                    "</td>" +
                    "<td style='width:145px'>" +
                    '<input type=\"hidden\" id=\"peopleType_suspect' + resumeRec + '\" />' +
                    '<input type=\"text\" id=\"peopleTypeName_suspect' + resumeRec + '\" name=\"peopleTypeName_suspect' + resumeRec + '\" data-options=\"required:true,tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\"  />' +
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

                $("#partyInfo_list").append(str);

                /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
                var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, ['${list.cardType}']);
                var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, ['${list.sex}']);
                cardTypeObj.setDisabled(true);
                genderObj.setDisabled(true);

                AnoleApi.initTreeComboBox('peopleTypeName_suspect' + resumeRec, 'peopleType_suspect' + resumeRec, "B416", null<#if list.peopleType??>, ["${list.peopleType!''}"]</#if>);
                $('#peopleTypeName_suspect'+ resumeRec).validatebox({required:true});

                resumeRec++;
            </#list>
        </#if>
    }

    function showdisputeMediationPeopleInChargeList() {
        <#if disputeMediation?? && disputeMediation.peopleInChargeList?? && (disputeMediation.peopleInChargeList?size > 0)>
        <#list disputeMediation.peopleInChargeList as list>
        var str = "";
        str = "<tr>" +
            "<td style=\"display:none;\">" +
            '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
            "</td>" +
            "<td style=\"display:none;\">" +
            '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"name_suspect\" name=\"name_suspect\" readonly=\"readonly\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" value=\"${list.name}\" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
            "</td>" +
            "<td style='width:145px'>" +
            '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType?string}\"/>' +
            '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
            "</td>" +
            "<td style='width:239px'>" +
            '<input type=\"hidden\" id=\"idCard_suspect\" name=\"idCard_suspect\" readonly=\"readonly\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"${list.idCard}" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"tel_suspect\" name=\"tel_suspect\" readonly=\"readonly\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox\" style=\"width:120px;\" value=\"${list.tel} \" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.tel??>${list.tel!}</#if></div>' +
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

        $("#peopleInChargeInfo_list").append(str);

        /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
        var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, ['${list.cardType}']);
        var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, ['${list.sex}']);
        cardTypeObj.setDisabled(true);
        genderObj.setDisabled(true);

        resumeRec++;
        </#list>
        </#if>
    }
</script>