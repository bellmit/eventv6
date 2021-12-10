<#--人口新增页面-->
<script type="text/javascript" src="${COMPONENTS_URL}/crossDomain/msgClient.jhtml?msgTag=addJuMin"></script>
<script type="text/javascript">
	function initItem4Basic() {
		var typesDictCode = "${typesDictCode!}";
		if(typesDictCode!=null && typesDictCode!="null" && typesDictCode!="") {
			AnoleApi.initTreeComboBox("typeName", "type", {
				"A001093199" : [${typesDictCode!}]
			}, null, ["${event.type!}"], {//0 展示指定的字典；1 去除指定的字典；
				FilterType : "<#if isRemoveTypes?? && isRemoveTypes>1<#else>0</#if>",
				EnabledSearch : true
			});
		} else {
			AnoleApi.initTreeComboBox("typeName", "type", "A001093199", null, ["${event.type!}"], {
				EnabledSearch : true
			});
		}

		AnoleApi.initListComboBox("influenceDegreeName", "influenceDegree", "A001093094", null, ["${event.influenceDegree!'01'}"]);
		AnoleApi.initListComboBox("urgencyDegreeName", "urgencyDegree", "A001093271", null, ["${event.urgencyDegree!'01'}"]);

		if($('#sourceName').length > 0) {
			AnoleApi.initListComboBox("sourceName", "source", "A001093222", null, ["${event.source!'01'}"]);
		}
		
		if($('#bigFileUploadDiv').length > 0) {
			var bigFileUploadOpt = {
				useType: 'add',
				fileExt: '.jpg,.gif,.png,.jpeg,.webp,.zip,.7z,.txt,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.rar',
				maxSingleFileSize: 50,
				attachmentData: {attachmentType:'${EVENT_ATTACHMENT_TYPE!}'},
				module: 'event',
				individualOpt : {
					isUploadHandlingPic : <#if isUploadHandlingPic??>${isUploadHandlingPic?c}<#else>false</#if>
				}
			};
			
			<#if event.eventId?? || attachmentIds??>
				bigFileUploadOpt["useType"] = 'edit';
				bigFileUploadOpt["attachmentData"].eventSeq = "1,2,3";
				
				<#if event.eventId??>
					bigFileUploadOpt["attachmentData"].bizId = '${event.eventId?c}';
				</#if>
				
				<#if attachmentIds??>
					bigFileUploadOpt["attachmentData"].attachmentIds = "${attachmentIds!}";
				</#if>
			</#if>
			
			bigFileUpload_initFileUploadDiv('bigFileUploadDiv', bigFileUploadOpt);
		}

		/*初始化涉及人员表单*/
		<#if involvedPeopleList?? && (involvedPeopleList?size > 0)>
			<#list involvedPeopleList as list>
				$("#involvedPeopleName${list_index}").validatebox({
					required: true,
					validType: ['maxLength[20]'],
					tipPosition: 'bottom'
				});

				$("#cardTypeName${list_index}").validatebox({
					required: true,
					validType: ['maxLength[100]'],
					tipPosition: 'bottom'
				});

				$("#cardNumber${list_index}").validatebox({
					required: true,
					validType: ['maxLength[50]'],
					tipPosition: 'bottom'
				});

				$("#phoneNumber${list_index}").validatebox({
					validType: 'mobileorphone',
					tipPosition: 'bottom'
				});

				AnoleApi.initListComboBox("cardTypeName${list_index}", "cardType${list_index}", "D030001", null, ["${list.cardType!}"]);
			</#list>
		</#if>

		//初始化事发详址控件
	    $("#occurred").anoleAddressRender({
	    	_source : 'XIEJING',//必传参数，数据来源
	        _select_scope : 0,
	        _show_level : 6,//显示到哪个层级
	        _startAddress :"${event.occurred!}",
	        _startDivisionCode : "${START_DIVISION_CODE!}", //默认选中网格，非必传参数

			<#if event.resMarker??>
	        	_addressMap : {//编辑页面可以传这个参数，非必传参数
	            _addressMapShow : true,//是否显示地图标注功能
	            _addressMapIsEdit : true,
	            _addressMapX : "${event.resMarker.x!}",
	            _addressMapY : "${event.resMarker.y!}",
	            _addressMapType : "${event.resMarker.mapType!}"
						},
			</#if>
	        BackEvents : {
	            OnSelected : function(api) {
	                $("#nationality").val(api.addressData._startCountryCode);
	                $("#occurred").val(api.getAddress());
	                $("#x").val(api.getAddressMapX());
	                $("#y").val(api.getAddressMapY());
	                $("#mapType").val(${event.resMarker.mapType!'5'});
	                //获取infoOrgCode，地址库控件选取到网格后自动回填所属网格编码，并且置空默认网格id，保存事件时后台会根据gridCode自动转换出gridId
	                $("#gridCode").val(api.getInfoOrgCode());
	                $("#gridId").val('');

	                if(api.getAddressCode().length >= 12){
	                    var committee = api.addressData._province + api.addressData._county + api.addressData._street + api.addressData._community
	                    if(committee.endsWith("社区")){
	                        committee += "居委会";
	                    }else if(committee.endsWith("村")){
	                        committee += "委会";
	                    }
	                    $("#committee").val(committee);
	                }else{
	                    $("#committee").val("");
	                }
	            },
	            OnCleared : function(api) {
	                //清空按钮触发的事件
	                $("#occurred").val('');
	                $("#committee").val("");
	            }
	        }
	    });

	    $("#advice").width($('#content').outerWidth(true));
	}

	<#if involvedPeopleList??>
    var flag = "${involvedPeopleList?size + 1}";
    <#else >
    var flag = 1;
    </#if>
    function addInvoledPeople(){
        var str = "";
        str = '<tr id="involvedPeopleTr_'+ flag +'">' +
                '<td>' +
                    '<a href="javascript:void(0)" class="NorToolBtn DelBtn" style="background-color: #fec434;border-radius: 3px;white-space:nowrap;margin-left: 10px;margin-top: 4px;" onclick="delInvoledPeople(this);">删除</a>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="involvedPeopleName' + flag + '" class="inp1" style="width: 100px;" onblur="getPerson(' + flag + ')"/>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="cardType' + flag + '" type="hidden" class="inp1"/>' +
                    '<input id="cardTypeName' + flag + '" class="inp1" style="width: 90px;" />' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="cardNumber' + flag + '" class="inp1" style="width: 140px;" onblur="getPerson(' + flag + ')"/>' +
                '</td>' +
                '<td style="width: 25%;">' +
                    '<input id="phoneNumber' + flag + '" class="inp1" style="width: 100px;"/>' +
                '</td>' +
                '<td>' +
                    '<input id="ciRsId' + flag + '" type="hidden"/>' +
                '</td>' +
                '</tr>';
        $("#involvedPeopleList").append(str);

        $("#involvedPeopleName" + (flag)).validatebox({
            required:true,
            validType: ['maxLength[20]'],
            tipPosition: 'bottom'
        });
        $("#cardTypeName" + (flag)).validatebox({
            required:true,
            tipPosition: 'bottom'
        });

        $("#cardNumber" + (flag)).validatebox({
            required:true,
            validType: ['idcard'],
            tipPosition: 'bottom'
        });

        $("#phoneNumber" + (flag)).validatebox({
            validType: 'mobileorphone',
            tipPosition: 'bottom'
        });

        //加载数据字典，(证件类型数据字典)
        AnoleApi.initListComboBox("cardTypeName"+flag, "cardType"+flag, "D030001",function(value,item,inobj) {
            var num = inobj.settings.TargetId.replace(/[^0-9]/ig,"");//inobj当前对象，inobj.settings.TargetId当前对象id值
            getPerson(num);

        },["001"]);

        flag++;
    }

    function delInvoledPeople(obj){
        $(obj).parent().parent().remove();
    }

    //判断涉及人员姓名身份证号是否存在
    function getPerson(num){
        //人员姓名
        var name = $("#involvedPeopleName"+num).val();
        //证件号码
        var cardNumber = $("#cardNumber"+num).val();

        //证件类型证件类型为身份证时验证证件号码类型
        var cardType = $("#cardType"+num).val();
        var options = {required:true, tipPosition: 'bottom',validType:['maxLength[50]']};
        if (cardType == '001') {
            options.validType = ['idcard'];
        }
        $("#cardNumber" + (num)).validatebox(options);

        //姓名和证件号码同时不为空时获取人员信息
        if (name.length == 0 || cardNumber.length == 0 ) {
            return;
        }
        getNewPerson(num);
    }

    function getNewPerson(num){
        var name = $("#involvedPeopleName"+num).val();
        var cardNumber = $("#cardNumber"+num).val();
        var cardType = $("#cardType"+num).val();


        if (name.length == 0) {
            $.messager.alert('提示','姓名不能为空');
            return;
        }
        if (cardNumber.length == 0) {
            $.messager.alert('提示','证件号码不能为空');
            return;
        }
        if (cardType.length == 0) {
            $.messager.alert('提示','证件类型不能为空');
            return;
        }

        //判断涉事人员是否存在，存在的话提示并清除本条记录
        //涉事人员信息
        if (!involvedPeopleList()) {
            involvedPeopleList();
        } else {
            $("#involvedPeopleName"+(num)).val('') ;
            $("#cardTypeName"+(num)).val('') ;
            $("#cardNumber"+(num)).val('') ;
            $("#phoneNumber"+(num)).val('') ;
            $("#ciRsId"+(num)).val('') ;
            return;
        }

        modleopen();
        $.ajax({
            type:"POST",
            url:'${rc.getContextPath()}/zhsq/event/eventDisposalController/findPerson.json',
            data:{name:name,cardNumber:cardNumber,cardType:cardType},
            dataType:"json",
            success:function (data) {
                modleclose();
                if(data.entity==null){
                    //查询不到人员信息，置空联系方式（）
                    $("#phoneNumber"+(num)).val() == "" ;
                    return;
                }
                //人员信息回填
                $("#cardType"+(num)).val(data.entity.certType) ;
                if ($("#phoneNumber"+(num)).val() == '') {
                    $("#phoneNumber"+(num)).val(data.entity.mobilePhone) ;
                }
                $("#ciRsId"+(num)).val(data.entity.partyId) ;
            },
            error:function(data){
                $.messager.alert('提示','连接超时！');
                //modleclose();
            }

        });
    }

    /*涉及人员数据列表*/
    function involvedPeopleList() {
        var peopleList = [];
        $("#involvedPeopleList").find("tr").each(function () {
            var tdArr = $(this).children();

            var name = tdArr.eq(1).find('input').val(),//姓名
                    cardType = tdArr.eq(2).find('input').val(),//证件类型
                    idCard = tdArr.eq(3).find('input').val(),//证件号码
                    tel = tdArr.eq(4).find('input').val();//联系方式
                    ciRsId = tdArr.eq(5).find('input').val();//联系方式

            if (name != undefined) {
                var obj = new Object();
                obj.name = name;
                obj.cardType = cardType;
                obj.idCard = idCard;
                obj.tel = tel;
                obj.ciRsId = ciRsId;
                peopleList.push(obj);
            }

        });
        //判断涉及人员信息是否重复添加
        var peopleExitence = false;
        for(var i = 0,len = peopleList.length;i < len;i++){
            for(var j = i+1; j < len;j++){
                if (peopleList[i].idCard == peopleList[j].idCard) {
                    $.messager.alert('提示','证件号码为['+peopleList[i].idCard+']姓名为['+peopleList[i].name+']的人员信息已经添加，请勿重复添加！');
                    peopleExitence = true;
                }
            }
        }
        var peopleListJson = JSON.stringify(peopleList);
        $("#peopleListJson").val(peopleListJson);
        return peopleExitence;
    }

    /********************************************相关涉及人员选择公用js部分开始********************************************/
    var resumeRec = 0;
    var flag8 = 1;
    //peopleType:人员类型 03 命案防控-嫌疑人； 04 命案防控-受害人； 05 涉及路案事件-主要嫌疑人
    var peopleType = '';

    //人口新增完成，将人口信息回填至嫌疑人/受害人列表
    function fetchPartyIndividualInfo(type,obj){
    
    console.log(obj);
        var str = "";

        str = "<tr>" +
            "<td style=\"display:none;\">" +
            '<input id=\"bizType_'+resumeRec+'\" name=\"bizType_'+resumeRec+'\" value=\"'+ type +'\" class=\"inp1 InpDisable\"/>'+
            "</td>" +
            "<td style=\"display:none;\">" +
            '<input id=\"partyId_\" name=\"partyId_\" value=\"'+ obj.partyId+'\" class=\"inp1 InpDisable\"/>'+
            "</td>" +
            "<td style='width:144px'>" +
                '<input type=\"hidden\" id=\"name_suspect\" name=\"name_suspect\"  data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[80]\',\'characterCheck\']\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\" readonly=\"readonly\" onblur=\"checkCardType(this);\" value=\"'+obj.partyName+'\" />' +
                '<div class="Check_Radio FontDarkBlue" style="width: 86%;"><a onclick="detailPartyIndividual('+obj.partyId+')" style="cursor: pointer">'+obj.partyName+'</a></div>' +
            "</td>" +
            "<td style='width:110px'>" +
                '<input type=\"hidden\" id=\"cardType_suspect'+resumeRec+'\" value=\"' + obj.certType + '\"/>' +
                '<input type=\"hidden\" id=\"cardTypeName_suspect'+resumeRec+'\" name=\"cardTypeName_suspect'+resumeRec+'\" data-options=\"tipPosition:\'bottom\'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:90px;\"  />' +
                '<div class="Check_Radio FontDarkBlue" style="width: 86%;">'+obj.certTypeCN+'</div>' +
            "</td>" +
            "<td style='width:180px'>" +
                '<input type=\"hidden\" id=\"idCard_suspect\" name=\"idCard_suspect\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 130px;\" readonly=\"readonly\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[24]\',\'characterCheck\']\" value=\"' +obj.identityCard+ '\" />' +
                '<div class="Check_Radio FontDarkBlue" style="width: 90%;">'+obj.identityCard+'</div>' +
            "</td>";
        //不是矛盾纠纷当事人时回填人员联系方式
        if('06' != type){
            str +=  "<td style='width:150px'>" +
                        '<input type=\"hidden\" id=\"tel_suspect\" name=\"tel_suspect\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 120px;\" readonly=\"readonly\" data-options=\"tipPosition:\'bottom\',validType:[\'maxLength[32]\',\'characterCheck\']\" value=\"' + obj.mobilePhone + '\" />' +
                        '<div class="Check_Radio FontDarkBlue" style="width: 86%;">'+obj.mobilePhone+'</div>' +
                    "</td>";
        }
        //矛盾纠纷当事人-人员类别
        if('06' == type){
            str += "<td style='width:144px'>" +
                        '<input type=\"hidden\" id=\"peopleType_suspect'+resumeRec+'\" />' +
                        '<input type=\"text\" id=\"peopleTypeName_suspect'+resumeRec+'\" name=\"peopleTypeName'+resumeRec+'\"  data-options=\"tipPosition:\'bottom\',required:true\" class=\"inp1 easyui-validatebox requestParam\" style=\"width:120px;\"  />' +
                    "</td>" ;
        }
        //命案防控-犯罪嫌疑人人员类型
        if('03' == type || '04' == type){
            //青少年
            var youngstersPic = "";
            var isTeenager = "";
            //未成年人
            var juvenilesPic = "";
            var isMinors = "";
            //精神病患者
            var psychoticPic = "";
            //是否是精神病患者
            var isMentalDisease = "";

            var age = -1;

            if(obj.identityCard != undefined){
                age = _checkBirthday(obj.identityCard);
            }

            if('03' == type){
                if(age >= 0 && age < 18){
                    isMinors = '<input type=\"hidden\" id=\"isMinors_suspect\" name=\"isMinors_suspect\" value=\"1\" />' ;
                    juvenilesPic = '<img title="未成年人" src="${rc.getContextPath()}/images/juveniles.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>';
                }
                if(age >= 6 && age < 25){
                    isTeenager = '<input type=\"hidden\" id=\"isTeenager_suspect\" name=\"isTeenager_suspect\" value=\"1\" />' ;
                    youngstersPic = '<img title="青少年" src="${rc.getContextPath()}/images/youngsters.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>';
                }

                isMentalDisease = '<input type=\"hidden\" id=\"isMentalDisease_suspect'+ resumeRec +'\" name=\"isMentalDisease_suspect\" value=\"0\" />' ;
                psychoticPic = '<img title="精神病患者" id=\"isMentalDisease_picture'+resumeRec +'\" hidden=\"hidden\" src="${rc.getContextPath()}/images/psychotic.png" style="margin:0 10px 0 0; width:28px; height:28px;"/>';

            }

            str +=  "<td style='width:239px'>" +
                        isTeenager + youngstersPic +
                        isMinors + juvenilesPic+
                        isMentalDisease + psychoticPic +
                    "</td>";
        }

        str += "<td style='width:100px'>" +
                    '<input type=\"hidden\" id=\"sex_suspect'+resumeRec+'\" />' +
                    '<input type=\"hidden\" id=\"sexName_suspect'+resumeRec+'\" name=\"sexName_suspect'+resumeRec+'\" class=\"inp1 easyui-validatebox requestParam\" style=\"width: 80px;height:30px;\" data-options=\"tipPosition:\'bottom\'\"/>' +
                    '<div class="Check_Radio FontDarkBlue" style="width: 86%;">'+obj.genderCN+'</div>' +
                "</td>" +
                "<td style='width:92px;'>" +
                    "<a href='###' class='del-tr bg-gray'  style='width:50px; ' onclick='delRec(this);'><i class='icon-del'></i>删 除</a>" +
                "</td>" +
                "</tr>";

        if('03' == type){//命案防控嫌疑人
            $("#suspectInfo_list").append(str);
            //判断是否是精神病患者
            mentalDisease(obj.partyId,resumeRec);
        }else if('04' == type){//命案防控受害人
            $("#victimInfo_list").append(str);
        }else if('12' == type){//涉及线路案事件
            $("#majorSuspectInfo_list").append(str);
        }else if('06' == type){//矛盾纠纷当事人
            $("#partyInfo_list").append(str);
        }else if('13' == type){//涉及师生安全的案（事）件
            $("#primeSuspectIn_list").append(str);
        }else if('14' == type){//矛盾纠纷化解责任人
            $("#peopleInChargeInfo_list").append(str);
        }

        /*AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", function() {checkCardType($('#cardType_' + resumeRec));}, [obj.certType]);*/
        var cardTypeObj = AnoleApi.initListComboBox('cardTypeName_suspect' + resumeRec, 'cardType_suspect' + resumeRec, "D030001", null, [obj.certType]);
        var genderObj = AnoleApi.initListComboBox('sexName_suspect' + resumeRec, 'sex_suspect' + resumeRec, "D060002", null, [obj.gender]);
        cardTypeObj.setDisabled(true);
        genderObj.setDisabled(true);

        if('06' == type){
            AnoleApi.initTreeComboBox('peopleTypeName_suspect' + resumeRec, 'peopleType_suspect' + resumeRec, "B416", null, null);
            $('#peopleTypeName_suspect'+ resumeRec).validatebox({required:true});
        }
        resumeRec++;

    }
    /*删除一行记录*/
    function delRec(obj){
        $.messager.confirm("提示","确定要删除本条记录吗?",function (r) {
            if(r){$(obj).parent().parent().remove();}
        });

    }
    //判断是否是精神病患者
    function mentalDisease(partyId,resumeRec) {
        if(partyId != undefined && partyId > 0){
            $.ajax({
                type:"POST",
                url:'${rc.getContextPath()}/zhsq/involvedPeople/isUniqueRel.json',
                data:{partyId:partyId},
                dataType:'json',
                success:function (data) {
                    if(data.isUniqueRel != undefined && data.isUniqueRel == false){
                      $('#isMentalDisease_suspect' + resumeRec).val(1);
                      $('#isMentalDisease_picture' + resumeRec).removeAttr("hidden");
                    }
                }
            });
        }
    }
    function addPartyIndividual(peopleType) {
        //获取嵌入的人口iframe
        var rsIframeObj = document.getElementById("baseInfoIframe").contentWindow;
        var partyId = $('#partyId').val();
        //监听人口子页面，新增保存后的方法
        gmMsgClient.addObserver(rsIframeObj,addFinishMyself,"addFinishMyself");
        //监听人口子页面，新增已存在人口时刷新页面的方法
        //gmMsgClient.addObserver(rsIframeObj,flashBaseMyself,"flashBaseMyself");
        //监听人口子页面，新增保存后刷新列表的方法
        //gmMsgClient.addObserver(rsIframeObj,flashListMyself,"flashListMyself");
        //监听人口子页面，点击取消的方法
        gmMsgClient.addObserver(rsIframeObj,cancelSaveResident,"cancelSaveResident");
        initBaseInfoIframe(peopleType,partyId);
    }

    var height = $(window).height();

    function initBaseInfoIframe(type,partyId) {
        var baseUrl = "${RS_DOMAIN}/cirs/rsBaseInfoPage.jhtml";
        peopleType = type;
        if (partyId) {
            baseUrl += "?partyId=" + partyId
        }
        showMaxJqueryWindow("新增人员信息", baseUrl,1000,height);
    }
    //人口新增回调方法
    function addFinishMyself(partyIndividual){
        if(partyIndividual != undefined){
            //console.log(partyIndividual);
            //判断命案嫌疑人/受害人是否重复
            if(!isInvolvedPeopleAdded(peopleType,partyIndividual)){
                fetchPartyIndividualInfo(peopleType,partyIndividual);
            }
        }
        //回填完成之后关闭人口新增页面
        cancelSaveResident();
    }
    //关闭人口新增页面
    function cancelSaveResident(){
        closeMaxJqueryWindow();
    }
    //判断涉及人员是否重复添加：根据人员证件类型 + 证件号 判断
    //peopleType:涉及人员类型
    //partyIndividual:将要添加的人口信息
    function isInvolvedPeopleAdded(peopleType,partyIndividual) {

        //将要添加的人口信息 partyId
        var peoplePartyId = '';
        //证件类型
        var peoplecardType = '';
        //证件号码
        var peopleIdCard = '';
        //已经添加过的人口信息列表
        var peopleList = [];
        //涉及人员列表；命案嫌疑人/受害人
        var peopleListId = '';

        //console.log(partyIndividual);

        if(partyIndividual != undefined && partyIndividual.partyId != undefined){
            peoplePartyId = partyIndividual.partyId;
        }
        if(partyIndividual != undefined && partyIndividual.identityCard != undefined){
            peopleIdCard = partyIndividual.identityCard;
        }
        if(partyIndividual != undefined && partyIndividual.certType != undefined){
            peoplecardType = partyIndividual.certType;
        }

        if(peopleType != undefined){
            if('03' == peopleType){
                peopleListId = 'suspectInfo_tbody';
            }else if('04' == peopleType){
                peopleListId = 'victimInfo_tbody';
            }else if('12' == peopleType){
                peopleListId = 'majorSuspectInfo_tbody';
            }else if('06' == peopleType){
                peopleListId = 'partyInfo_tbody';
            }else if('13' == peopleType){
                peopleListId = 'primeSuspectIn_tbody';
            }else if('14' == peopleType){
                peopleListId = 'peopleInChargeInfo_tbody';
            }
        }

        $("#" + peopleListId).find("tr").each(function () {
            var tdArr = $(this).children();

            var name = tdArr.eq(2).find('input').val(),//姓名
                cardType = tdArr.eq(3).find('input').val(),//证件类型
                idCard = tdArr.eq(4).find('input').val(),//证件号码
                sex = tdArr.eq(6).find('input').val();//联系方式
            partyId = tdArr.eq(1).find('input').val();//联系方式

            if (name != undefined) {
                var obj = new Object();
                obj.name = name;
                obj.cardType = cardType;
                obj.idCard = idCard;
                obj.sex = sex;
                obj.partyId = partyId;
                peopleList.push(obj);
            }

        });
        //判断命案防控嫌疑人/受害人信息是否重复添加
        //默认未重复添加
        var peopleExitence = false;
        for(var i = 0,len = peopleList.length;i < len;i++){
            if (peopleList[i].idCard == peopleIdCard && peopleList[i].cardType == peoplecardType) {
                $.messager.alert('提示','证件号码为[ '+peopleIdCard+' ]姓名为['+peopleList[i].name+']的人员信息已经添加，请勿重复添加！');
                //重复添加
                peopleExitence = true;
                break;
            }
        }
        return peopleExitence;
    }
    
    
    //查看人口信息
    function detailPartyIndividual(partyId) {
        //var url = "${SQ_ZZGRID_URL}/resident/detail/"+ciRsId+".jhtml";
        var url = "${RS_DOMAIN}/cirs/viewResidentSnippet.jhtml?partyId=" + partyId;

        if(typeof(parent.showCustomEasyWindow) != 'undefined') {
            parent.showCustomEasyWindow("人员基本信息",url,900,520);
        } else if(typeof(parent.showMaxJqueryWindow) != 'undefined') {
            parent.showMaxJqueryWindow("人员基本信息",url,900,520);
        } else if(typeof(showCustomEasyWindow) != 'undefined') {
            showCustomEasyWindow("人员基本信息",url,900,520);
        } else if(typeof(showMaxJqueryWindow) != 'undefined') {
            showMaxJqueryWindow("人员基本信息",url,900,520);
        } else {
            $.messager.alert('错误','人员基本信息查看失败！', 'error');
        }
    }
    
    /********************************************相关涉及人员选择公用js部分结束********************************************/

</script>
