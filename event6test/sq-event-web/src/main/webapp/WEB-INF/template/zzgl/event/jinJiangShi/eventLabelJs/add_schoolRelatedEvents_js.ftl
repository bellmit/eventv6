<script type="text/javascript">
    var ID_CARD_TYPE = "1";//身份证证件类型

    function initItem4SchoolRelatedEvents() {

        AnoleApi.initListComboBox("schoolRelatedEvents_natureName", "schoolRelatedEvents_nature", "B040", null, [<#if schoolRelatedEvents?? && schoolRelatedEvents.nature??>"${schoolRelatedEvents.nature}"<#else>"1"</#if>]);

        //聚焦输入框
        $("#schoolRelatedEvents_ecapeNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });
        //聚焦输入框
        $("#schoolRelatedEvents_arrestedNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });
        //聚焦输入框
        $("#schoolRelatedEvents_crimeNum").next("span").children().eq(1).focus(function(){
            $(this).select();
        });

        <#if schoolRelatedEvents?? && schoolRelatedEvents.suspectList?? && (schoolRelatedEvents.suspectList?size > 0)>
            showPrimeSuspectPeople();//若被嫌疑人存在，展示嫌疑人信息
        </#if>

        zzForm.render('checkbox','schoolRelatedEventsIsDetection');
        zzForm.on('checkbox(_schoolRelatedEventsIsDetection)', function (data) {
            if(data.checked){
                $(this).val('1');
            }else {
                $(this).val('0');
            }
        });
    }

    function fetchSchoolRelatedEventsPersonInfo() {//构建人员信息
    	var result = true;
        var reportedObjArray = [],
            reportedObj = {},
            inpVal = "",
            inpId = "";

        //构建命案防控嫌疑人员/受害人人员信息
        $('#primeSuspectInfoDiv table[id=primeSuspectIn_list] > tbody tr').each(function() {
            reportedObj = {};

            $(this).find('input').each(function() {
                inpVal = $(this).val();
                inpId = $(this).attr('id');

                if(inpVal != undefined) {
                    reportedObj[inpId.substr(0, inpId.indexOf('_'))] = inpVal;
                }
            });

            reportedObjArray.push(reportedObj);
        });

        if(reportedObjArray.length > 0) {
            $("#schoolRelatedEvents_person").val(JSON.stringify(reportedObjArray));
        }else{
        	result= false;
        }
        
        return result;
    }

    //若嫌疑人存在，展示嫌疑人信息
    function showPrimeSuspectPeople() {
        <#if schoolRelatedEvents?? && schoolRelatedEvents.suspectList?? && (schoolRelatedEvents.suspectList?size > 0)>
        <#list schoolRelatedEvents.suspectList as list>
        var str = "";
        str = "<tr>" +
            "<td style=\"display:none;\">" +
            '<input id=\"bizType_' + resumeRec + '\" name=\"bizType_' + resumeRec + '\" value=\"${list.bizType}\" class=\"inp1 InpDisable\"/>' +
            "</td>" +
            "<td style=\"display:none;\">" +
            '<input id=\"partyId_\" name=\"partyId_\" value=\"${list.partyId}\" class=\"inp1 InpDisable\"/>'+
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"name_suspect\" name=\"name_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" readonly=\"readonly\" value=\"${list.name}\" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual(<#if list.partyId??>${list.partyId!}</#if>)" style="cursor: pointer"><#if list.name??>${list.name!}</#if></a></div>' +
            "</td>" +
            "<td style='width:145px'>" +
            '<input type=\"hidden\" id=\"cardType_suspect' + resumeRec + '\" value=\"${list.cardType?string}\"/>' +
            '<input type=\"hidden\" id=\"cardTypeName_suspect' + resumeRec + '\" name=\"cardTypeName_suspect' + resumeRec + '\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.cardTypeName??>${list.cardTypeName!}</#if></div>' +
            "</td>" +
            "<td style='width:239px'>" +
            '<input type=\"hidden\" id=\"idCard_suspect\" name=\"idCard_suspect\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" readonly=\"readonly\" value=\"${list.idCard}" />' +
            '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><#if list.idCard??>${list.idCard!}</#if></div>' +
            "</td>" +
            "<td style='width:144px'>" +
            '<input type=\"hidden\" id=\"tel_suspect\" name=\"tel_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox\" style=\"width:120px;\" readonly=\"readonly\" value=\"${list.tel} \" />' +
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

        $("#primeSuspectIn_list").append(str);

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