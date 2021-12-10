<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>联防组</title>
<script type="text/javascript" src="${uiDomain!''}/web-assets/common/js/jqry-9-1-12-4.min.js"></script>


<#include "/map/arcgis/standardmappage/common/standard_common.ftl" />
</head>
<body style="background-color: #fff;" >
		<div class="con PeopleWatch">
        	<div class="PeopleInfo" style="height:100%;"> 
                <ul>
                    
                    <li style="width:250px;">
                    	<p style="padding-left:0;"><span style="cursor:pointer;" >${data.ORG_NAME!''}</span>
                    	</p>

                        	<span style="color:#000;line-height:24px;font-size:12px;">联队长：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
								${data.PARTY_NAME!''}
							</span><br/>
                        	<span style="color:#000;line-height:24px;font-size:12px;">联系电话：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
								${data.VERIFY_MOBILE!''}
							</span><br/>
                       
                        	<span style="color:#000;line-height:24px;font-size:12px;">人口人数：</span>
							<span style="text-align:left;color:#0075a9;line-height:24px;font-size:12px;">
		                    	${stat.ALL_RS_NUM!'0'}
							</span><br/>
                      
						
                    </li>
                </ul>
                <div class="clear"></div>
            </div>
        </div>
		
</body>
<script  type="text/javascript">
function init(){
	var title = parent.document.getElementById('openTitle');
	title.style.width = 'auto';
}
</script>

</html>
