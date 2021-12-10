	


function initTipCompleteMsg(obj, date){
			
			var completeDate = date;
			var ct = new Date().getTime();
			completeDate = new Date(completeDate.split('-')[0]+'/'+completeDate.split('-')[1]+'/'+completeDate.split('-')[2]).getTime();
			if(ct>completeDate){
				var bb = parseInt((ct-completeDate)/(1000*24*3600));
				if(bb==0){
					obj.css('color','#f00').html('！今日即将过期');
				}else{
					obj.css('color','#f00').html('！超期'+bb+'个工作日');
				}
			}else{
				var bb = parseInt((completeDate-ct)/(1000*24*3600))+1;
				obj.html('！剩余'+bb+'个工作日');
			}
		}