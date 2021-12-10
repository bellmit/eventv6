
<@override name="queryConditions">
<script>
	var queryParamsEAD = {eventAndDisputeMediation:1};
	var key ="${eventAndDisputeMediation!}";
	var arr=key.split(',');
	for(var i=0,l=arr.length;i<l;i++){
		var k = arr[i].split(":");
		queryParamsEAD[k[0]]=  k[1];
	}
	queryParamsEAD.listStatus=queryParamsEAD.status;
	queryParamsEAD.listOrgId=queryParamsEAD.orgId;
	delete queryParamsEAD.status;
	delete queryParamsEAD.orgId;
	var stime = '${startCreateTime!c}';
	if(stime){
		queryParamsEAD.createTimeStart=stime.substr(0,4)+'-'+stime.substr(4,2)+'-' +stime.substr(6,2);
	}
	var etime = '${endCreateTime!c}';
	if(etime){
		queryParamsEAD.createTimeEnd=etime.substr(0,4)+'-'+etime.substr(4,2)+'-' +etime.substr(6,2);
	}
</script>
</@override>
<@override name="btnConditions">
</@override>
<@override name="sqlConditions">
 var queryParams =queryParamsEAD;
  queryParams.gridId=startGridId;
	 queryParams.itype=itype;
 queryParams.mediationType=mediationType;
 //queryParams.infoOrgCode=InfoOrgCode ;
</@override>
<@override name="sqlConditions2">
 var a = queryParamsEAD;
  a.gridId= queryParams.gridId;
		  a.startHappenTime= queryParams.startHappenTime;
		 a.endHappenTime= queryParams.endHappenTime;
		  a.keyWord= queryParams.keyWord;
		  a.disputeType=  queryParams.disputeType;
	    a.mediationDeadlineStart=  queryParams.mediationDeadlineStart;
	    a.mediationDeadlineEnd=  queryParams.mediationDeadlineEnd;
 a.disputeTypeList=queryParams.disputeTypeList;
	    a.disputeScale =  queryParams.disputeScale;
	$("#list").datagrid('options').queryParams = a;
</@override>
<@extends name="/zzgl/dispute/mediation_9x/jiangxi/index_dispute.ftl" />